/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author choun
 */
public class ReservationAutoCancel {

    ArrayList<Reservation> reservationList = new ArrayList<>();
    private Map<String, String> cardMap = new HashMap<>();

    String paths = System.getProperty("user.dir");

    public ReservationAutoCancel() {

        LoadReservationList();
        LoadCardList();

        LocalDate localNowDate = LocalDate.now();
        LocalTime localNowTime = LocalTime.now();

        String nowDate = localNowDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String nowTime = localNowTime.format(DateTimeFormatter.ofPattern("HH"));

        int time = Integer.parseInt(nowTime);

        for (int i = 0; i < reservationList.size(); i++) {
            if (time >= 18 && reservationList.get(i).getCheckInDate().equals(nowDate) && reservationList.get(i).getPayType().equals("카드") && !reservationList.get(i).getState().equals("취소")) {
                String cancelerName = reservationList.get(i).getName();
                if (cardMap.containsKey(cancelerName) && cardMap.get(cancelerName).equals("null")) {
                    new UpdateRoomInfo(cancelerName);
                    JOptionPane.showMessageDialog(null, "취소된 예약이 있습니다, 변경사항을 확인하세요.");
                }
            }
        }
    }

    private void LoadReservationList() {
        File reservationFile = new File(paths + "/ReservationList.txt");
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(reservationFile))) {
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split("\t");
                reservationList.add(new Reservation(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadCardList() {
        File reservationFile = new File(paths + "/CardInfo.txt");
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(reservationFile))) {
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split("\t");
                if (data[1] == null) {
                    cardMap.put(data[0], null);
                } else {
                    cardMap.put(data[0], data[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
