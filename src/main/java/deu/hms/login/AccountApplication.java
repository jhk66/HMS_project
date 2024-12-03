/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.login;

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
public class AccountApplication {

    ArrayList<Account> accountdata = new ArrayList<>();

    String paths = System.getProperty("user.dir");
    File accountFile = new File(paths + "/LoginAccount.txt");

    public String AccountCheck(String id, String passward) {
        fileRead();
        for (int i = 0; i < accountdata.size(); i++) {
            if (accountdata.get(i).getId().equals(id) && accountdata.get(i).getPassward().equals(passward)) {
                if (accountdata.get(i).getPosition().equals("관리자")) {
                    return "관리자";
                } else if (accountdata.get(i).getPosition().equals("직원")) {
                    return "직원";
                }
            }
        }
        return null;
    }

    public void AccountLoad(DefaultTableModel model) {
        fileRead();
        for (int i = 0; i < accountdata.size(); i++) {
            model.addRow(new Object[]{
                accountdata.get(i).getId(),
                accountdata.get(i).getPassward(),
                accountdata.get(i).getPosition()});
        }
    }

    private void fileRead() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(accountFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                accountdata.add(new Account(data[0], data[1], data[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
