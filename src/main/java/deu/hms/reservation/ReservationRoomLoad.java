/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import deu.hms.room.Room;
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

public class ReservationRoomLoad {
    ArrayList<Room> RoomLoad = new ArrayList<>();

    String paths = System.getProperty("user.dir");
    File RoomFile = new File(paths + "/RoomList.txt");
    
    public ReservationRoomLoad(DefaultTableModel model){
        RoomfileRead();
        for (int i = 0; i < RoomLoad.size(); i++) {
            model.addRow(new Object[]{
                RoomLoad.get(i).getRoomNum(),
                RoomLoad.get(i).getIsBook()
                });
        }
    }

    private void RoomfileRead() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(RoomFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                if(data[1].equals("0")){
                    data[1] = "공실";
                }else if(data[1].equals("1")){
                    data[1] = "예약됨";
                }else if(data[1].equals("2")){
                    data[1] = "체크인됨";
                }
                if(!data[1].equals("공실")){
                    RoomLoad.add(new Room(data[0], data[1], data[2]));
                }else{
                    RoomLoad.add(new Room(data[0], data[1], null));
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
