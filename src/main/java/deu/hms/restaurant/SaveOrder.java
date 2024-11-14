/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.restaurant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author choun
 */
public class SaveOrder {
    private String dateTime = null;
    
    String paths = System.getProperty("user.dir");
    File serviceListFile = new File(paths + "/ServiceOrderList.txt");
    
    public SaveOrder(JTable table, String roomNum, String part, String payType, int totalPrice){
        LocalDateTime now = LocalDateTime.now();
        dateTime = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd/ HH:mm:ss"));
        String price = String.valueOf(totalPrice);

        DefaultTableModel orderModel = (DefaultTableModel) table.getModel();
        
        StringBuilder menu = new StringBuilder();
        for(int i = 0; i < orderModel.getRowCount(); i++){
            String menuItem = String.valueOf(orderModel.getValueAt(i, 0));
            String countItem = String.valueOf(orderModel.getValueAt(i, 2));
            if(i == 0){
                menu.append(menuItem).append(" ").append(countItem).append("개");
            } else{
                menu.append(", ").append(menuItem).append(" ").append(countItem).append("개");
            }
        }
        
        String data = dateTime + "\t" + roomNum + "\t" + part + "\t" + menu + "\t" + payType + "\t" + price;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(serviceListFile, true))){
            bw.write(data);
            bw.newLine();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
    
    
