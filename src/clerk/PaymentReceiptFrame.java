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

public class PaymentReceiptFrame extends JFrame{
    
    private JTextField bookingIDField;
    private JTextArea receiptArea;

    public PaymentReceiptFrame() {

        setTitle("Payment & Receipt");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        bookingIDField = new JTextField();
        bookingIDField.setBorder(
                BorderFactory.createTitledBorder("Enter Booking ID"));

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);

        // ✅ CHANGE BUTTON NAME
        JButton payBtn = new JButton("Process Payment");
        JButton backBtn = new JButton("Back");

        payBtn.addActionListener(e -> processPayment());

        backBtn.addActionListener(e -> {
            new ClerkFrame().setVisible(true);
            dispose();
        });

        add(bookingIDField, BorderLayout.NORTH);
        add(new JScrollPane(receiptArea), BorderLayout.CENTER);
        add(payBtn, BorderLayout.WEST);
        add(backBtn, BorderLayout.SOUTH);
    }

    private void processPayment() {

        String bookingID = bookingIDField.getText().trim();

        if (bookingID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Booking ID!");
            return;
        }

        File inputFile = new File(FilePaths.BOOKING);
        File tempFile = new File("data/temp.txt");

        boolean found = false;

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile))
        ) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|");

                if (data.length >= 10 && data[0].equalsIgnoreCase(bookingID)) {

                    found = true;

                    // ✅ KEEP EXISTING PAYMENT (NO POPUP)
                    // data[8] already contains payment method

                    // KEEP STATUS ACTIVE
                    data[7] = "ACTIVE";

                    line = String.join("|", data);

                    generateReceipt(data);
                }

                pw.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing payment!");
            return;
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Booking not found!");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        JOptionPane.showMessageDialog(this, "Payment Successful!");
    }

    private void generateReceipt(String[] data) {

        try {

            DecimalFormat df = new DecimalFormat("0.00");

            double price = Double.parseDouble(data[9]);
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
                    "Status: " + data[7] + "\n" +
                    "Payment Method: " + data[8] + "\n" +
                    "Price: RM " + formattedPrice + "\n" +
                    "=====================================";

            receiptArea.setText(receipt);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error showing receipt!");
        }
    }

}
