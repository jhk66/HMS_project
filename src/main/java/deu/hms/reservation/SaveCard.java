/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.reservation;

import deu.hms.restaurant.ServiceLoadInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author choun
 */
public class SaveCard {

    ArrayList<Card> cardlist = new ArrayList<>();

    String paths = System.getProperty("user.dir");
    File cardFile = new File(paths + "/CardInfo.txt");

    public SaveCard(String name, String cardNum) {

        String cardData = name + "\t" + cardNum;
                
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cardFile, true))) {
            writer.write(cardData);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

    private void CardfileRead() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(cardFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                cardlist.add(new Card(data[0], data[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
