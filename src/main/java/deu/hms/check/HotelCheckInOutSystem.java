
package deu.hms.check;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HotelCheckInOutSystem extends JFrame {
    private JTextField searchField;
    private JTable reservationTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> searchOptions;

    public HotelCheckInOutSystem() {
        // 프레임 설정
        setTitle("체크인/체크아웃 시스템");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 상단 검색 패널 설정
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("체크인/체크아웃", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        searchPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new BorderLayout());
        searchOptions = new JComboBox<>(new String[]{"예약 번호", "고객명"}); // 예약 번호와 고객명만 선택 가능
        searchField = new JTextField();
        JButton searchButton = new JButton("검색");
        searchBar.add(searchOptions, BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchButton, BorderLayout.EAST);
        searchPanel.add(searchBar, BorderLayout.SOUTH);

        // 예약 테이블 설정
        String[] columnNames = {"예약 날짜", "예약 번호", "고객명", "전화번호", "예약 인원", "객실 정보", "체크인/아웃"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reservationTable = new JTable(tableModel);

        // 더미 데이터 추가
        tableModel.addRow(new Object[]{"2024-11-08", "001", "홍길동", "010-1234-5678", "2명", "101호", "체크인 대기"});
        tableModel.addRow(new Object[]{"2024-11-09", "002", "김철수", "010-9876-5432", "1명", "102호", "체크인 완료"});

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
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText().trim();
                int searchColumn = searchOptions.getSelectedIndex() + 1; // 1은 예약 번호, 2는 고객명
                boolean found = false;

                // 테이블에서 선택된 열을 기준으로 검색
                if (!searchText.isEmpty()) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (tableModel.getValueAt(i, searchColumn).toString().contains(searchText)) {
                            reservationTable.setRowSelectionInterval(i, i);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        JOptionPane.showMessageDialog(null, "정보를 찾을 수 없습니다: " + searchText);
                    }
                }
            }
        });

        // 체크인 버튼 동작 설정
        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationTable.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.setValueAt("체크인 완료", selectedRow, 6);
                    JOptionPane.showMessageDialog(null, "체크인이 완료되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "체크인할 예약을 선택하세요.");
                }
            }
        });

        // 체크아웃 버튼 동작 설정
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reservationTable.getSelectedRow();
                if (selectedRow != -1) {
                    String customerName = (String) tableModel.getValueAt(selectedRow, 2); // 고객명 가져오기
                    String roomInfo = (String) tableModel.getValueAt(selectedRow, 5); // 객실 정보 가져오기
                    showCheckOutDialog(customerName, roomInfo, selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "체크아웃할 예약을 선택하세요.");
                }
            }
        });
    }

    // 체크아웃 다이얼로그 창 생성 메서드
    private void showCheckOutDialog(String customerName, String roomInfo, int selectedRow) {
        JDialog checkOutDialog = new JDialog(this, "체크아웃", true);
        checkOutDialog.setSize(400, 300);
        checkOutDialog.setLayout(new BorderLayout());

        // 지불 금액과 피드백 입력 패널
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel customerLabel = new JLabel("고객명: " + customerName);
        JLabel amountLabel = new JLabel("지불 금액:");
        JTextField amountField = new JTextField();
        JLabel feedbackLabel = new JLabel("고객 피드백:");
        JTextArea feedbackArea = new JTextArea(3, 20);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);

        mainPanel.add(customerLabel);
        mainPanel.add(amountLabel);
        mainPanel.add(amountField);
        mainPanel.add(feedbackLabel);
        mainPanel.add(new JScrollPane(feedbackArea));

        // 체크아웃 시간이 오전 11시 이후인지 확인
        JButton submitButton = new JButton("수정");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amount = amountField.getText().trim();
                String feedback = feedbackArea.getText().trim();

                // 현재 시간을 가져옴
                if (amount.isEmpty()) {
                    JOptionPane.showMessageDialog(checkOutDialog, "지불 금액을 입력해주세요.");
                } else {
                    JOptionPane.showMessageDialog(checkOutDialog, "체크아웃이 완료되었습니다.\n총 금액: " + amount + "원\n고객 피드백: " + feedback);

                    // 객실 정보 업데이트 (체크아웃 완료로 업데이트)
                    tableModel.setValueAt("체크아웃 완료", selectedRow, 6);

                    // 객실 정보 업데이트 (예시로 객실을 '빈 방'으로 설정)
                    tableModel.setValueAt("빈 방", selectedRow, 5);

                    checkOutDialog.dispose();
                }
            }
        });

        checkOutDialog.add(mainPanel, BorderLayout.CENTER);
        checkOutDialog.add(submitButton, BorderLayout.SOUTH);
        checkOutDialog.setLocationRelativeTo(this);
        checkOutDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HotelCheckInOutSystem().setVisible(true);
        });
    }
}
