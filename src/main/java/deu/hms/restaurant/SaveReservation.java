/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.restaurant;

import com.toedter.calendar.JDateChooser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author choun
 */
public class SaveReservation {
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년MM월dd일");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private String reservationDate = null;
    private String roomNum = null;
    private String part = null;
    private String hour = null;
    private String min = null;
    private String totalPrice = null;
    
    String paths = System.getProperty("user.dir");
    File serviceReservationFile = new File(paths + "/ServiceReservationList.txt");
    
    public SaveReservation(JDateChooser dateChooser, JTable table, String roomNum, String part, String hour, String min, int total){
        
        this.roomNum = roomNum;
        this.part = part;
        this.hour = hour;
        this.min = min;
        this.totalPrice = String.valueOf(total);
        reservationDate = dateFormat.format(dateChooser.getDate());
        
        DefaultTableModel orderModel = (DefaultTableModel) table.getModel();
        
        StringBuilder menu = new StringBuilder();
        for(int i = 0; i < orderModel.getRowCount(); i++){
            String menuItem = String.valueOf(orderModel.getValueAt(i, 0));
            String countItem = String.valueOf(orderModel.getValueAt(i, 2));
            if(i == 0){
                menu.append(menuItem).append(" ").append(countItem).append("개");
            } else{
                menu.append("/").append(menuItem).append(" ").append(countItem).append("개");
            }
        }
        
        String data = reservationDate + "\t" + hour + "시 " + min + "분" + "\t" + roomNum + "\t" + part + "\t" + menu + "\t" + totalPrice;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(serviceReservationFile, true))){
            bw.write(data);
            bw.newLine();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
