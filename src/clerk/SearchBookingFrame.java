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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class SearchBookingFrame extends JFrame{
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    public SearchBookingFrame() {

        setTitle("Search & View Bookings");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel(new BorderLayout(10,10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        searchField = new JTextField();
        searchField.setBorder(
                BorderFactory.createTitledBorder("Enter Booking ID"));

        JButton searchBtn = new JButton("Search");
        JButton viewAllBtn = new JButton("View All");
        JButton backBtn = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchBtn);
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(backBtn);

        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        String[] columns = {
                "Booking ID", "Name", "Movie",
                "Showtime", "Seat", "Status", "Payment"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> searchBooking());
        viewAllBtn.addActionListener(e -> loadAllBookings());

        backBtn.addActionListener(e -> {
            new ClerkFrame().setVisible(true);
            dispose();
        });

        loadAllBookings();
    }

    private void loadAllBookings() {

        model.setRowCount(0);

        try (BufferedReader br =
                     new BufferedReader(new FileReader(FilePaths.BOOKING))) {

            String line;

            while ((line = br.readLine()) != null) {

                // ✅ FIXED: use |
                String[] p = line.split("\\|");

                if (p.length >= 10) {

                    model.addRow(new Object[]{
                            p[0], // Booking ID
                            p[1], // Name
                            p[2], // Movie
                            p[3], // Showtime
                            p[6], // Seat
                            p[7], // Status (ACTIVE / CANCELED)
                            p[8]  // Payment
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading bookings!");
        }
    }

    private void searchBooking() {

        String bookingID = searchField.getText().trim();

        if (bookingID.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter Booking ID to search!");
            return;
        }

        model.setRowCount(0);

        try (BufferedReader br =
                     new BufferedReader(new FileReader(FilePaths.BOOKING))) {

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {

                // ✅ FIXED: use |
                String[] p = line.split("\\|");

                if (p.length >= 10 &&
                        p[0].equalsIgnoreCase(bookingID)) {

                    model.addRow(new Object[]{
                            p[0],
                            p[1],
                            p[2],
                            p[3],
                            p[6],
                            p[7],
                            p[8]
                    });

                    found = true;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this,
                        "Booking not found!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error searching booking!");
        }
    }

}
