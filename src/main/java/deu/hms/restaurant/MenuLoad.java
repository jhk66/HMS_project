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
public class MenuLoad {

    ArrayList<ServiceLoadInfo> serviceLoad = new ArrayList<>();

    String paths = System.getProperty("user.dir");
    File menuFile = new File(paths + "/menuList.txt");

    public MenuLoad(DefaultTableModel model, String part) {
        menuRead();
        for (int i = 0; i < serviceLoad.size(); i++) {
                if (serviceLoad.get(i).getPart().equals(part)) {
                    model.addRow(new Object[]{serviceLoad.get(i).getMenu(),
                        serviceLoad.get(i).getPrice()});
                }
            }
    }

    public void menuRead() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(menuFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                serviceLoad.add(new ServiceLoadInfo(data[0], data[1], data[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
