/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.restaurant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author choun
 */
public class RoomNumLoad {
    ArrayList<String> roomnum = new ArrayList<>();
    
    String paths = System.getProperty("user.dir");
    File roomListFile = new File(paths + "/RoomList.txt");

    public RoomNumLoad() {
    }
    
    public RoomNumLoad(DefaultComboBoxModel model){
        roomListRead();
        for(int i = 0; i < roomnum.size(); i++){
            model.addElement(roomnum.get(i));
        }
    }
    
    private void roomListRead(){
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(roomListFile))){
            while((line = br.readLine()) != null){
                String[] data = line.split("\t");
                if(data[1].equals("2")){
                    roomnum.add(data[0]);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public String getClientName(String selectRoom) {
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(roomListFile))){
            while((line = br.readLine()) != null){
                String[] data = line.split("\t");
                if(data[0].equals(selectRoom)){
                    return data[2];
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    
}
