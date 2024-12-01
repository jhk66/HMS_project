import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;



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

        // 테이블 모델 초기화
        String[] columnNames = {"예약 번호", "고객명", "객실 정보", "체크인 날짜", "체크아웃 날짜", "체크인/아웃"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reservationTable = new JTable(tableModel);

        // 초기 데이터 로드
        reservationData = new ArrayList<>();
        roomData = new HashMap<>();

        // 데이터 로드 메서드 호출
        loadReservationData();
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
        JButton closeButton = new JButton("닫기"); // 닫기 버튼 추가
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(closeButton); // 버튼 패널에 추가

        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 검색 버튼 동작 설정
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText().trim();
            String searchOption = (String) searchOptions.getSelectedItem();
            searchRoomData(searchOption, searchValue);
        });


        checkInButton.addActionListener(e -> {
            int selectedRow = reservationTable.getSelectedRow(); // 테이블에서 선택된 행 확인
            if (selectedRow != -1) {
                String roomNumber = (String) tableModel.getValueAt(selectedRow, 2); // 객실 번호 가져오기
                String roomStatus = (String) tableModel.getValueAt(selectedRow, 5); // 상태 확인 (6번째 열)
                if ("체크인".equals(roomStatus)) {
                    JOptionPane.showMessageDialog(this, roomNumber + "호는 이미 체크인된 상태입니다.", "경고", JOptionPane.WARNING_MESSAGE);
                } else if ("예약".equals(roomStatus)) {
                    // RoomList.txt의 2번째 열을 2로 업데이트
                    updateRoomListFile(roomNumber, "2");

                    // ReservationList.txt의 상태를 "체크인"으로 업데이트
                    updateReservationListFile((String) tableModel.getValueAt(selectedRow, 0), "체크인");

                    // 테이블 상태 업데이트
                    tableModel.setValueAt("체크인", selectedRow, 5);

                    JOptionPane.showMessageDialog(this, roomNumber + "호 체크인이 완료되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "예약 상태가 아니어서 체크인할 수 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "체크인할 예약을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });




        checkOutButton.addActionListener(e -> {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow != -1) {
                String roomNumber = (String) tableModel.getValueAt(selectedRow, 2);
                String reservationNumber = (String) tableModel.getValueAt(selectedRow, 0);
                String roomStatus = (String) tableModel.getValueAt(selectedRow, 5);

                if ("체크인".equals(roomStatus)) {
                    int totalPayment = calculateTotalPayment(roomNumber, reservationNumber);

                    // 현재 시간 가져오기
                    LocalDate currentDate = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();
                    String formattedTime = currentTime.withSecond(0).withNano(0).toString().substring(0, 5);

                    // 오전 11시 이후 체크아웃 추가 요금 계산
                    int lateCheckoutFee = getAdditionalLateCheckoutFee(reservationNumber);
                    totalPayment += lateCheckoutFee;

                    // 결과 출력
                    JOptionPane.showMessageDialog(this,
                            "체크아웃 시간: " + currentDate + " " + formattedTime + "\n" +
                                    "총 지불 금액: " + totalPayment + "원",
                            "체크아웃 완료", JOptionPane.INFORMATION_MESSAGE);

                    // RoomList와 ReservationList 상태 업데이트
                    updateRoomListFile(roomNumber, "0"); // RoomList 상태를 0으로 변경
                    updateReservationListFile(reservationNumber, "체크아웃");

                    // 테이블 상태 업데이트
                    tableModel.setValueAt("체크아웃", selectedRow, 5);
                } else {
                    JOptionPane.showMessageDialog(this, "체크인 상태가 아니어서 체크아웃할 수 없습니다.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "체크아웃할 예약을 선택하세요.");
            }
        });

        // 닫기 버튼 동작 설정
        closeButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "창을 닫으시겠습니까?", "닫기 확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // 창 닫기
            }
        });


    }

    private void loadReservationData() {
        String paths = System.getProperty("user.dir");
        File reservationFile = new File(paths + "/ReservationList.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(reservationFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // 공백 기준으로 데이터 분리
                String[] data = line.split("\\s+");

                // 고정된 데이터 열이 8개이므로, 주소 길이를 계산
                int addressStartIndex = 2;
                int fixedColumns = 8; // 예약 번호, 고객명 제외한 나머지 고정 열
                int addressEndIndex = data.length - fixedColumns;

                // 주소를 재구성
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = addressStartIndex; i < addressEndIndex; i++) {
                    addressBuilder.append(data[i]).append(" ");
                }
                String address = addressBuilder.toString().trim();

                // 재구성된 데이터 배열
                String[] formattedData = new String[11];
                formattedData[0] = data[0];  // 예약 번호
                formattedData[1] = data[1];  // 고객명
                formattedData[2] = address;  // 재구성된 주소
                System.arraycopy(data, addressEndIndex, formattedData, 3, fixedColumns);

                reservationData.add(formattedData); // 최종 데이터 추가
            }
        } catch (IOException e) {
            System.err.println("ReservationList.txt 파일 읽기 중 오류: " + e.getMessage());
        }

        // 데이터를 테이블에 추가
        populateTable();
    }

    private void populateTable() {
        tableModel.setRowCount(0); // 기존 데이터 초기화

        for (String[] reservation : reservationData) {
            // 테이블에 표시할 데이터만 선택
            String reservationNumber = reservation[0]; // 예약 번호
            String customerName = reservation[1];      // 고객명
            String roomInfo = reservation[5];          // 객실 정보
            String checkInDate = reservation[8];       // 체크인 날짜
            String checkOutDate = reservation[9];      // 체크아웃 날짜
            String status = reservation[10];           // 상태 ("체크인", "예약", "취소" 등)

            // 테이블 행 추가
            tableModel.addRow(new Object[]{
                    reservationNumber, customerName, roomInfo,
                    checkInDate, checkOutDate, status
            });
        }
    }



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

    private void updateRoomListFile(String roomNumber, String newStatus) {
        String paths = System.getProperty("user.dir");
        File roomListFile = new File(paths + "/RoomList.txt");
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(roomListFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\s+");
                if (parts.length < 2) continue; // 데이터가 불완전한 줄 무시

                if (parts[0].equals(roomNumber)) { // 객실 번호가 일치하면 상태 업데이트
                    parts[1] = newStatus; // 두 번째 열 업데이트
                    if ("0".equals(newStatus) && parts.length > 2) {
                        parts[2] = ""; // 체크아웃 시 고객명 삭제
                    }
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





    private void updateReservationListFile(String reservationNumber, String status) {
        String paths = System.getProperty("user.dir");
        File reservationFile = new File(paths + "/ReservationList.txt");
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(reservationFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split("\\s+");

                // 최소 열 길이 검증 (예약 번호, 고객명, 주소, 객실번호, 상태 등 최소 열 개수 11)
                if (data.length < 11) {
                    System.err.println("잘못된 데이터 형식: " + line);
                    continue;
                }

                // 고정된 데이터 파싱 방식 적용
                int addressStartIndex = 2; // 주소 시작 위치
                int fixedColumns = 8; // 고정 열 개수
                int addressEndIndex = data.length - fixedColumns;

                // 예약 번호가 일치하면 상태 업데이트
                if (data[0].equals(reservationNumber)) {
                    data[data.length - 1] = status; // 마지막 열(상태)을 업데이트
                }

                // 주소 재구성
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = addressStartIndex; i < addressEndIndex; i++) {
                    addressBuilder.append(data[i]).append(" ");
                }
                String address = addressBuilder.toString().trim();

                // 업데이트된 데이터 재구성
                String[] formattedData = new String[11];
                formattedData[0] = data[0]; // 예약 번호
                formattedData[1] = data[1]; // 고객명
                formattedData[2] = address; // 주소
                System.arraycopy(data, addressEndIndex, formattedData, 3, fixedColumns);

                // 파일에 저장할 형식으로 변환
                String updatedLine = String.join("\t", formattedData);
                updatedContent.append(updatedLine).append("\n");
            }
        } catch (IOException e) {
            System.err.println("ReservationList.txt 읽기 오류: " + e.getMessage());
            return;
        }

        // 파일에 업데이트된 내용 쓰기
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
            String checkInOutStatus = reservation[10];

            String roomStatus;
            if ("체크인".equals(checkInOutStatus)) {
                roomStatus = "2";
            } else if ("예약".equals(checkInOutStatus)) {
                roomStatus = "1";
            } else {
                roomStatus = "0";
            }

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

    private int calculateTotalPayment(String roomNumber, String reservationNumber) {
        int reservationAmount = 0;

        // ReservationList에서 예약 금액 가져오기
        for (String[] reservation : reservationData) {
            if (reservation[0].equals(reservationNumber)) {
                reservationAmount = Integer.parseInt(reservation[7]); // 예약 금액
                break;
            }
        }

        System.out.println("Reservation Amount: " + reservationAmount);

        int serviceAmount = 0;
        String paths = System.getProperty("user.dir");
        File serviceOrderFile = new File(paths + "/ServiceOrderList.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(serviceOrderFile))) {
            String line;
            StringBuilder currentLine = new StringBuilder();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 줄을 병합하여 데이터 완성
                currentLine.append(line).append(" ");

                // 데이터가 완성되었는지 확인
                String[] tempData = currentLine.toString().trim().split("\\t");
                if (tempData.length >= 6) { // 최소 6개의 열이 있어야 데이터로 간주
                    String[] rawData = currentLine.toString().trim().split("\\t");

                    if (rawData.length < 6) {
                        currentLine.setLength(0); // 초기화 후 다음 데이터로 진행
                        continue;
                    }

                    String parsedRoomNumber = rawData[1].trim(); // 객실 번호
                    String paymentType = rawData[4].trim();

                    // "객실 청구" 처리
                    if (paymentType.equals("객실 청구") && rawData.length >= 6) {
                        try {
                            int serviceCost = Integer.parseInt(rawData[5].trim()); // 금액 필드
                            if (parsedRoomNumber.equals(roomNumber.trim())) {
                                serviceAmount += serviceCost; // 금액 합산
                                System.out.println("Matching Data: " + Arrays.toString(rawData));
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("ServiceOrderList.txt 금액 변환 오류: " + e.getMessage());
                        }
                    }

                    // 데이터 처리 후 현재 줄 초기화
                    currentLine.setLength(0);
                }
            }
        } catch (IOException e) {
            System.err.println("ServiceOrderList.txt 읽기 오류: " + e.getMessage());
        }


        System.out.println("Service Amount: " + serviceAmount);

        int additionalLateCheckoutFee = 0;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate checkoutDate = LocalDate.parse(reservationData.stream()
                    .filter(res -> res[0].equals(reservationNumber))
                    .findFirst()
                    .map(res -> res[9])
                    .orElse(""), formatter);

            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            if (!currentDate.isBefore(checkoutDate) && currentTime.isAfter(LocalTime.of(11, 0))) {
                int floor = Integer.parseInt(roomNumber) / 100;
                additionalLateCheckoutFee = getFloorFee(floor);
            }
        } catch (DateTimeParseException e) {
            System.err.println("날짜 형식이 잘못되었습니다: " + e.getMessage());
        }

        System.out.println("Additional Late Checkout Fee: " + additionalLateCheckoutFee);

        int totalPayment = reservationAmount + serviceAmount + additionalLateCheckoutFee;
        System.out.println("Total Payment: " + totalPayment);

        return totalPayment;
    }


    private int getAdditionalLateCheckoutFee(String reservationNumber) {
        String checkoutDateStr = null;

        // ReservationList에서 체크아웃 날짜 가져오기
        for (String[] reservation : reservationData) {
            if (reservation[0].equals(reservationNumber)) {
                checkoutDateStr = reservation[9]; // 체크아웃 날짜
                break;
            }
        }

        if (checkoutDateStr == null) {
            System.err.println("예약 번호에 해당하는 체크아웃 날짜를 찾을 수 없습니다.");
            return 0;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, formatter);
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            // 현재 시간이 오전 11시 이후인 경우 추가 요금 발생
            if (!currentDate.isBefore(checkoutDate) && currentTime.isAfter(LocalTime.of(11, 0))) {
                return 10000; // 추가 요금
            }
        } catch (DateTimeParseException e) {
            System.err.println("날짜 형식이 잘못되었습니다: " + checkoutDateStr);
            return 0;
        }

        return 0; // 기본 추가 요금 없음
    }


    private int getFloorFee(int floor) {
        String paths = System.getProperty("user.dir");
        File roomInfoFile = new File(paths + "/RoomInfo.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(roomInfoFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\s+");
                if (Integer.parseInt(data[0]) == floor) {
                    return Integer.parseInt(data[2]); // 3번째 데이터가 추가 금액
                }
            }
        } catch (IOException e) {
            System.err.println("RoomInfo.txt 읽기 오류: " + e.getMessage());
        }

        return 0; // 기본 추가 요금 0
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelCheckInOutSystem().setVisible(true));
    }
}