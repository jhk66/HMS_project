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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author choun
 */
public class ServiceReservationLoad {
    ArrayList<ServiceLoadInfo> serviceReservation = new ArrayList<>();
    
    String paths = System.getProperty("user.dir");
    File serviceReservationFile = new File(paths + "/ServiceReservationList.txt");

    public ServiceReservationLoad() {
    }
    
    public ServiceReservationLoad(DefaultTableModel model){
        serviceReservationLoad();
        for (int i = 0; i < serviceReservation.size(); i++) {
                model.addRow(new Object[]{
                    serviceReservation.get(i).getDate(),
                    serviceReservation.get(i).getTime(),
                    serviceReservation.get(i).getRoomNum(),
                    serviceReservation.get(i).getPart(),
                    serviceReservation.get(i).getMenu(),
                    serviceReservation.get(i).getPrice()});

            }
    }
    
    private void serviceReservationLoad(){
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(serviceReservationFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                serviceReservation.add(new ServiceLoadInfo(data[0], data[1], data[2], data[3], data[4], data[5]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList getList(){
        serviceReservationLoad();
        return serviceReservation;
    }
}
