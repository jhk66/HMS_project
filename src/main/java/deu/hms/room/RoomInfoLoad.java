/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.room;

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
public class RoomInfoLoad {
    
    ArrayList<RoomInfo> roominfo = new ArrayList<>();
    
    String paths = System.getProperty("user.dir");
    File roominfoFile = new File(paths + "/RoomInfo.txt");
    
    public RoomInfoLoad(DefaultTableModel model){
        roominfoRead();
        for(int i = 0; i < roominfo.size() ; i++){
            model.addRow(new Object[]{
                roominfo.get(i).getFloor(),
                roominfo.get(i).getType(),
                roominfo.get(i).getPrice()
            });
        }
    }
    
    private void roominfoRead(){
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(roominfoFile))){
                while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                roominfo.add(new RoomInfo(data[0], data[1], data[2]));
                    System.out.println(data[0]);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
