/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author choun
 */
public class ReservationSave {

    String paths = System.getProperty("user.dir");
    File reservationFile = new File(paths + "/ReservationList.txt");

    public ReservationSave(Reservation save) {
        String reservationData = String.join("\t",
                save.getNumber(),
                save.getName(),
                save.getAddress(),
                save.getTel(),
                save.getPeople(),
                save.getRoomNum(),
                save.getPayType(),
                save.getPrice(),
                save.getCheckInDate(),
                save.getCheckOutDate(),
                save.getState()
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reservationFile, true))) {
            writer.write(reservationData);
            writer.newLine();
            new UpdateRoomInfo(save);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
