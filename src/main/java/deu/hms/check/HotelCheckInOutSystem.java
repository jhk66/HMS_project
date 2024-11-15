
package deu.hms.check;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 객실 데이터 불러오기
        roomData = new HashMap<>();
        loadRoomData();

        // 상단 검색 패널 설정
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("체크인/체크아웃", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        searchPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new BorderLayout());
        JComboBox<String> searchOptions = new JComboBox<>(new String[]{"예약번호", "고객명"});
        searchField = new JTextField();
        JButton searchButton = new JButton("검색");
        searchBar.add(searchOptions, BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchButton, BorderLayout.EAST);
        searchPanel.add(searchBar, BorderLayout.SOUTH);

        // 예약 테이블 설정
        String[] columnNames = {"객실 번호", "예약 상태", "고객명"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reservationTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(reservationTable);

        // 하단 버튼 패널 설정
        JPanel buttonPanel = new JPanel();
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
                    tableModel.setValueAt("체크인 완료", 0, 1); // 검색된 첫 번째 결과의 상태를 체크인 완료로 설정
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
                tableModel.setValueAt("체크아웃 완료", selectedRow, 1);
                JOptionPane.showMessageDialog(null, roomNumber + "호 체크아웃이 완료되었습니다.");
            } else {
                JOptionPane.showMessageDialog(null, "체크아웃할 예약을 선택하세요.");
            }
        });
    }

    // 객실 리스트 파일에서 데이터 로드
    private void loadRoomData() {
        try (BufferedReader br = new BufferedReader(new FileReader("RoomList.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                String roomNumber = data[0];
                String reservationStatus = data[1];
                String customerName = data.length > 2 ? data[2] : "";
                roomData.put(roomNumber, new String[]{reservationStatus, customerName});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 검색 기능
    private void searchRoomData(String option, String value) {
        tableModel.setRowCount(0); // 이전 검색 결과 초기화
        for (Map.Entry<String, String[]> entry : roomData.entrySet()) {
            String roomNumber = entry.getKey();
            String[] info = entry.getValue();
            String reservationStatus = info[0];
            String customerName = info[1];

            boolean match = false;
            if (option.equals("예약번호") && roomNumber.equals(value)) {
                match = true;
            } else if (option.equals("고객명") && customerName.equals(value)) {
                match = true;
            }

            if (match) {
                tableModel.addRow(new Object[]{roomNumber, reservationStatus.equals("1") ? "예약됨" : "미예약", customerName});
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
