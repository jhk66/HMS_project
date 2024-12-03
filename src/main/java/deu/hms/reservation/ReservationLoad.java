/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author choun
 */
public class ReservationLoad {

    ArrayList<Reservation> reservationList = new ArrayList<>();

    String paths = System.getProperty("user.dir");
    File reservationFile = new File(paths + "/ReservationList.txt");

    public ReservationLoad() {
        nextNum = 0;
        ReservationFileRead();
    }

    public ReservationLoad(DefaultTableModel model) {
        nextNum = 0;
        ReservationFileRead();
        System.out.println("reL" + reservationList.size());
        for (int i = 0; i < reservationList.size(); i++) {
            model.addRow(new Object[]{
                reservationList.get(i).getNumber(),
                reservationList.get(i).getName(),
                reservationList.get(i).getAddress(),
                reservationList.get(i).getTel(),
                reservationList.get(i).getPeople(),
                reservationList.get(i).getRoomNum(),
                reservationList.get(i).getPayType(),
                reservationList.get(i).getPrice(),
                reservationList.get(i).getCheckInDate(),
                reservationList.get(i).getCheckOutDate(),
                reservationList.get(i).getState()
            });
        }

    }

    private int nextNum = 0;
    private void ReservationFileRead() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(reservationFile))) {
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split("\t");
                if (data.length == 11) {
                    reservationList.add(new Reservation(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10]));
                    nextNum += 1;
                } else {
                    System.out.println("Invalid data line: " + line);
                }
            }
            nextNum += 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNextNum() {
        return String.valueOf(nextNum);
    }
    
    public ArrayList getList(){
        return reservationList;
    }
}
