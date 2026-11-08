/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clerk;

/**
 *
 * @author TRACY
 */

import utils.FilePaths;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;

public class ViewReceiptFrame extends JFrame{
    
    private JTextField bookingField;
    private JTextArea receiptArea;

    public ViewReceiptFrame() {

        setTitle("Search Receipt");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        bookingField = new JTextField();
        bookingField.setBorder(
                BorderFactory.createTitledBorder("Enter Booking ID"));

        JButton searchBtn = new JButton("Search");
        JButton backBtn = new JButton("Back");

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(bookingField, BorderLayout.CENTER);
        topPanel.add(searchBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> searchReceipt());

        backBtn.addActionListener(e -> {
            new ClerkFrame().setVisible(true);
            dispose();
        });
    }

    private void searchReceipt() {

        String bookingID = bookingField.getText().trim();

        if (bookingID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Booking ID first!");
            return;
        }

        try (BufferedReader br =
                     new BufferedReader(new FileReader(FilePaths.BOOKING))) {

            String line;

            while ((line = br.readLine()) != null) {

                // ✅ FIXED: use |
                String[] data = line.split("\\|");

                if (data.length >= 10 &&
                        data[0].equalsIgnoreCase(bookingID)) {

                    String status = data[7];   // ACTIVE / CANCELED
                    String payment = data[8];

                    double price = Double.parseDouble(data[9]);
                    DecimalFormat df = new DecimalFormat("0.00");
                    String formattedPrice = df.format(price);

                    String receipt =
                            "=========== CINEMA RECEIPT ===========\n" +
                            "Booking ID: " + data[0] + "\n" +
                            "Name: " + data[1] + "\n\n" +
                            "Movie: " + data[2] + "\n" +
                            "Showtime: " + data[3] + "\n" +
                            "Hall Type: " + data[4] + "\n" +
                            "Hall Number: " + data[5] + "\n" + 
                            "Seat: " + data[6] + "\n\n" +
                            "Status: " + status + "\n" +
                            "Payment Method: " + payment + "\n" +
                            "Price: RM " + formattedPrice + "\n" +
                            "=====================================";

                    receiptArea.setText(receipt);

                    // ✅ SAVE RECEIPT
                    saveReceiptToFile(data[0], receipt);

                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Receipt not found!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error reading booking file!");
        }
    }

    private void saveReceiptToFile(String bookingID, String receipt) {
        try {

            File file = new File(FilePaths.RECEIPT);

            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(receipt);
            bw.newLine();
            bw.write("----------------------------------------");
            bw.newLine();
            bw.newLine();

            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}