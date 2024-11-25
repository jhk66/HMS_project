package deu.hms.check;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HotelCheckInOutSystem extends JFrame {
    private JTextField searchField;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private Map<String, String[]> roomData; // 객실 정보 저장 (객실번호 -> [예약상태, 고객명])

    public HotelCheckInOutSystem() {
        // 프레임 설정
        setTitle("체크인/체크아웃 시스템");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 객실 데이터 불러오기
        roomData = new HashMap<>();
        loadRoomData();

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
        gbc.weightx = 0.1; // 비율 설정
        gbc.insets = new Insets(5, 5, 5, 5); // 여백 설정
        searchBar.add(searchOptions, gbc);

        // 검색 필드 (가운데)
        gbc.gridx = 1;
        gbc.weightx = 0.7; // 비율 설정
        searchBar.add(searchField, gbc);

        // 검색 버튼 (오른쪽)
        gbc.gridx = 2;
        gbc.weightx = 0.2; // 비율 설정
        searchBar.add(searchButton, gbc);

        searchPanel.add(searchBar, BorderLayout.CENTER);

        // 예약 테이블 설정
        String[] columnNames = {"예약 번호", "고객명", "객실 정보", "체크아웃 날짜", "상태", "체크인/아웃"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reservationTable = new JTable(tableModel);

        // 테이블 헤더 중앙 정렬 설정
        JTableHeader header = reservationTable.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // 헤더 중앙 정렬

        // 테이블 데이터 셀 중앙 정렬 설정
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER); // 데이터 중앙 정렬
        for (int i = 0; i < reservationTable.getColumnCount(); i++) {
            reservationTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane tableScrollPane = new JScrollPane(reservationTable);

        // 하단 버튼 패널 설정
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton checkInButton = new JButton("체크인");
        JButton checkOutButton = new JButton("체크아웃");
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);

        // 프레임에 컴포넌트 추가
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
            String input = JOptionPane.showInputDialog("예약번호나 고객명을 입력하세요:");
            if (input != null && !input.trim().isEmpty()) {
                searchRoomData("예약번호", input);  // 예약번호로 먼저 검색
                if (reservationTable.getRowCount() == 0) {
                    searchRoomData("고객명", input);  // 일치하는 예약번호가 없으면 고객명으로 검색
                }
                if (reservationTable.getRowCount() > 0) {
                    String roomNumber = (String) tableModel.getValueAt(0, 0);
                    tableModel.setValueAt("체크인 완료", 0, 4); // 상태를 체크인 완료로 설정
                    updateRoomListFile(roomNumber, "1"); // RoomList.txt 상태 업데이트
                    JOptionPane.showMessageDialog(null, "체크인이 완료되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "예약 정보를 찾을 수 없습니다.");
                }
            }
        });

        // 체크아웃 버튼 동작 설정
        checkOutButton.addActionListener(e -> {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow != -1) {
                String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
                tableModel.setValueAt("체크아웃 완료", selectedRow, 4);
                updateRoomListFile(roomNumber, "0"); // RoomList.txt 상태 업데이트
                JOptionPane.showMessageDialog(null, roomNumber + "호 체크아웃이 완료되었습니다.");
            } else {
                JOptionPane.showMessageDialog(null, "체크아웃할 예약을 선택하세요.");
            }
        });
    }

    // 객실 리스트 파일에서 데이터 로드
    private void loadRoomData() {
        String paths = System.getProperty("user.dir");
        File roomListFile = new File(paths + "/RoomList.txt");

        System.out.println("파일 경로: " + roomListFile.getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(roomListFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split("\\s+");
                String reservationNumber = data[0];
                String status = data[1];
                String customerName = data.length > 2 ? data[2] : "";
                roomData.put(reservationNumber, new String[]{customerName, status});
            }
        } catch (IOException e) {
            System.err.println("파일 처리 중 오류 발생: " + e.getMessage());
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
            System.err.println("파일 읽기 중 오류 발생: " + e.getMessage());
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(roomListFile))) {
            bw.write(updatedContent.toString());
        } catch (IOException e) {
            System.err.println("파일 쓰기 중 오류 발생: " + e.getMessage());
        }
    }

    // 검색 기능
    private void searchRoomData(String option, String value) {
        tableModel.setRowCount(0); // 이전 검색 결과 초기화
        for (Map.Entry<String, String[]> entry : roomData.entrySet()) {
            String reservationNumber = entry.getKey();
            String[] info = entry.getValue();
            String customerName = info[0];
            String status = info[1];

            boolean match = false;
            if (option.equals("예약번호") && reservationNumber.equals(value)) {
                match = true;
            } else if (option.equals("고객명") && customerName.equals(value)) {
                match = true;
            }

            if (match) {
                tableModel.addRow(new Object[]{reservationNumber, customerName, "객실 정보 없음", "미정", status});
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
