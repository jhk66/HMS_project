package deu.hms.check;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelCheckInOutSystem extends JFrame {
    private JTextField searchField;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private List<String[]> reservationData; // ReservationList 데이터를 저장
    private Map<String, String[]> roomData; // RoomList 데이터를 저장 (객실번호 -> [상태, 고객명])

    public HotelCheckInOutSystem() {
        // 프레임 설정
        setTitle("체크인/체크아웃 시스템");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 초기 데이터 로드
        reservationData = new ArrayList<>();
        roomData = new HashMap<>();
        loadReservationData(); // ReservationList 데이터 로드
        loadRoomData();        // RoomList 데이터 로드

        // 상단 검색 패널 설정
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("체크인/체크아웃", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        searchPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JComboBox<String> searchOptions = new JComboBox<>(new String[]{"고객명", "예약번호"});
        searchOptions.setPreferredSize(new Dimension(100, 30));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(400, 30));

        JButton searchButton = new JButton("검색");
        searchButton.setPreferredSize(new Dimension(80, 30));

        // 검색 옵션 (왼쪽)
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.insets = new Insets(5, 5, 5, 5);
        searchBar.add(searchOptions, gbc);

        // 검색 필드 (가운데)
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchBar.add(searchField, gbc);

        // 검색 버튼 (오른쪽)
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        searchBar.add(searchButton, gbc);

        searchPanel.add(searchBar, BorderLayout.CENTER);

        // 예약 테이블 설정
        String[] columnNames = {"예약 번호", "고객명", "객실 정보", "체크아웃 날짜", "상태", "체크인/아웃"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reservationTable = new JTable(tableModel);

        JTableHeader header = reservationTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < reservationTable.getColumnCount(); i++) {
            reservationTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane tableScrollPane = new JScrollPane(reservationTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton checkInButton = new JButton("체크인");
        JButton checkOutButton = new JButton("체크아웃");
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);

        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 검색 버튼 동작 설정
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText().trim();
            String searchOption = (String) searchOptions.getSelectedItem();
            searchRoomData(searchOption, searchValue);
        });

        // 체크인 버튼 동작 설정
        checkInButton.addActionListener(e -> {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow != -1) {
                String roomNumber = (String) tableModel.getValueAt(selectedRow, 2);
                String roomStatus = (String) tableModel.getValueAt(selectedRow, 4);
                if ("1".equals(roomStatus)) {
                    tableModel.setValueAt("2", selectedRow, 4);
                    tableModel.setValueAt("체크인", selectedRow, 5);
                    updateRoomListFile(roomNumber, "2");
                    updateReservationListFile((String) tableModel.getValueAt(selectedRow, 0), "체크인");
                    JOptionPane.showMessageDialog(this, roomNumber + "호 체크인이 완료되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "예약 상태가 아니어서 체크인할 수 없습니다.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "체크인할 예약을 선택하세요.");
            }
        });

        // 체크아웃 버튼 동작 설정
        checkOutButton.addActionListener(e -> {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow != -1) {
                String roomNumber = (String) tableModel.getValueAt(selectedRow, 2);
                String roomStatus = (String) tableModel.getValueAt(selectedRow, 4);
                if ("2".equals(roomStatus)) {
                    tableModel.setValueAt("0", selectedRow, 4);
                    tableModel.setValueAt("체크아웃", selectedRow, 5);
                    updateRoomListFile(roomNumber, "0");
                    updateReservationListFile((String) tableModel.getValueAt(selectedRow, 0), "체크아웃");
                    JOptionPane.showMessageDialog(this, roomNumber + "호 체크아웃이 완료되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "체크인 상태가 아니어서 체크아웃할 수 없습니다.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "체크아웃할 예약을 선택하세요.");
            }
        });
    }

    // ReservationList.txt 데이터를 로드
    private void loadReservationData() {
        String paths = System.getProperty("user.dir");
        File reservationFile = new File(paths + "/ReservationList.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(reservationFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split("\\s+");
                reservationData.add(data);
            }
        } catch (IOException e) {
            System.err.println("ReservationList.txt 파일 읽기 중 오류: " + e.getMessage());
        }
    }

    // RoomList.txt 데이터를 로드
    private void loadRoomData() {
        String paths = System.getProperty("user.dir");
        File roomListFile = new File(paths + "/RoomList.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(roomListFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split("\\s+");
                roomData.put(data[0], new String[]{data[1], data.length > 2 ? data[2] : ""});
            }
        } catch (IOException e) {
            System.err.println("RoomList.txt 파일 읽기 중 오류: " + e.getMessage());
        }
    }

    // RoomList.txt 파일 업데이트 메소드
    private void updateRoomListFile(String roomNumber, String newStatus) {
        String paths = System.getProperty("user.dir");
        File roomListFile = new File(paths + "/RoomList.txt");
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(roomListFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts[0].equals(roomNumber)) {
                    parts[1] = newStatus;
                }
                updatedContent.append(String.join("\t", parts)).append("\n");
            }
        } catch (IOException e) {
            System.err.println("RoomList.txt 읽기 오류: " + e.getMessage());
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(roomListFile))) {
            bw.write(updatedContent.toString());
        } catch (IOException e) {
            System.err.println("RoomList.txt 쓰기 오류: " + e.getMessage());
        }
    }

    // ReservationList.txt 파일 업데이트 메소드
    private void updateReservationListFile(String reservationNumber, String status) {
        String paths = System.getProperty("user.dir");
        File reservationFile = new File(paths + "/ReservationList.txt");
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(reservationFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts[0].equals(reservationNumber)) {
                    parts[9] = status; // 체크인/체크아웃 상태 업데이트
                }
                updatedContent.append(String.join("\t", parts)).append("\n");
            }
        } catch (IOException e) {
            System.err.println("ReservationList.txt 읽기 오류: " + e.getMessage());
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reservationFile))) {
            bw.write(updatedContent.toString());
        } catch (IOException e) {
            System.err.println("ReservationList.txt 쓰기 오류: " + e.getMessage());
        }
    }

    private void searchRoomData(String option, String value) {
        tableModel.setRowCount(0);

        for (String[] reservation : reservationData) {
            String reservationNumber = reservation[0];
            String customerName = reservation[1];
            String roomNumber = reservation[5];
            String checkOutDate = reservation[9];
            String checkInOutStatus = reservation[10]; // 체크인/체크아웃 상태

            String[] roomInfo = roomData.get(roomNumber);
            String roomStatus = roomInfo != null ? roomInfo[0] : "0";

            boolean match = false;
            if (option.equals("예약번호") && reservationNumber.equals(value)) {
                match = true;
            } else if (option.equals("고객명") && customerName.equals(value)) {
                match = true;
            }

            if (match) {
                tableModel.addRow(new Object[]{
                        reservationNumber, customerName, roomNumber,
                        checkOutDate, roomStatus, checkInOutStatus
                });
            }
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "정보를 찾을 수 없습니다: " + value);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelCheckInOutSystem().setVisible(true));
    }
}
