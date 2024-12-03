/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author choun
 */
public class RoomPriceFinder {
    
    private String price = null;
    
    private static Map<Integer, String> priceMap = new HashMap<>();
    public static String paths = System.getProperty("user.dir");
    public static File roomInfoFile = new File(paths + "/RoomInfo.txt");
    
    public RoomPriceFinder(String roomNum){
        loadRoomInfo();
        
        int floor = Integer.parseInt(roomNum) / 100;
        
        if(priceMap.containsKey(floor)){
            price = priceMap.get(floor);
            ReservationFrame.RoomPriceField(price);
        }
        else{
            price = null;
            ReservationFrame.RoomPriceField(price);
        }
    }
    
    private static void loadRoomInfo(){
        try(BufferedReader br = new BufferedReader(new FileReader(roomInfoFile))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                int floor = Integer.parseInt(data[0]);
                String price = data[2];
                priceMap.put(floor, price);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
