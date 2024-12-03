/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import deu.hms.restaurant.ServiceLoadInfo;
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
public class ClientCardLoad {
    ArrayList<Card> cardLoad = new ArrayList<>();

    String paths = System.getProperty("user.dir");
    File menuFile = new File(paths + "/CardInfo.txt");
    
    public ClientCardLoad(DefaultTableModel model){
        CardListRead();
        for (int i = 0; i < cardLoad.size(); i++) {
            model.addRow(new Object[]{
                cardLoad.get(i).getName(),
                cardLoad.get(i).getCardNum()
            });
        }
    }

    public void CardListRead() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(menuFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                cardLoad.add(new Card(data[0], data[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
