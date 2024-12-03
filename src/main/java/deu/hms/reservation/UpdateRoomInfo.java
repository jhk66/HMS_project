/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import deu.hms.room.Room;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author choun
 */
public class UpdateRoomInfo {

    ArrayList<Room> roomList = new ArrayList<>();
    ArrayList<Reservation> reservationList = new ArrayList<>();

    String paths = System.getProperty("user.dir");
    File roomListFile = new File(paths + "/RoomList.txt");
    File reservationFile = new File(paths + "/ReservationList.txt");

    private String selectRoomNum = null;
    private String selectName = null;

    public UpdateRoomInfo(Reservation save) {
        ReadroomFile();
        selectRoomNum = save.getRoomNum();
        selectName = save.getName();
        for (int i = 0; i < roomList.size(); i++) {
            if (roomList.get(i).getRoomNum().equals(selectRoomNum)) {
                roomList.get(i).setIsBook("1");
                roomList.get(i).setName(selectName);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(roomListFile))) {
            for (int i = 0; i < roomList.size(); i++) {
                String data = roomList.get(i).getRoomNum() + "\t" + roomList.get(i).getIsBook() + "\t" + roomList.get(i).getName();
                bw.write(data);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UpdateRoomInfo(String name) {

        selectName = name;
        System.out.println(name);
        ReadReservationFile();
        for (int i = 0; i < reservationList.size(); i++) {
            if (reservationList.get(i).getName().equals(selectName)) {
                reservationList.get(i).setState("취소");
                break;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(reservationFile))) {
            for (int i = 0; i < reservationList.size(); i++) {
                String reservationData = String.join("\t",
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
                );
                bw.write(reservationData);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ReadroomFile() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(roomListFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                if (!data[1].equals("0")) {
                    roomList.add(new Room(data[0], data[1], data[2]));
                } else {
                    roomList.add(new Room(data[0], data[1], null));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ReadReservationFile() {
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
}
