/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.hms.management;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author choun
 */
public class ChangeSaveFile {

    public ChangeSaveFile(JTable table, File file) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                saveToFile(table, file);
                return null;
            }
        };
        worker.execute();
    }

    private void saveToFile(JTable table, File file) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        try (BufferedWriter wr = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    String data = String.valueOf(model.getValueAt(i, j));
                    wr.write(data);
                    if (j < model.getColumnCount() - 1) {
                        wr.write("\t");
                    }
                }
                wr.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
