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
import service.SeatServiceCm;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class CancelBookingFrame extends JFrame{
    
    private JTextField bookingField;

    public CancelBookingFrame() {

        setTitle("Cancel Booking");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("Cancel Booking", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(33, 56, 99));
        add(title, BorderLayout.NORTH);
        
        bookingField = new JTextField();
        bookingField.setBorder(BorderFactory.createTitledBorder("Enter Booking ID"));

        JButton cancelBtn = new JButton("Confirm Cancel");
        JButton backBtn = new JButton("Back");

        cancelBtn.addActionListener(e -> cancelBooking());

        backBtn.addActionListener(e -> {
            new CancelModifyMenu().setVisible(true);
            dispose();
        });

        panel.add(bookingField);
        panel.add(cancelBtn);
        panel.add(backBtn);

        add(panel);
    }

    private void cancelBooking() {

        String id = bookingField.getText().trim();
        boolean found = false;

        File inputFile = new File(FilePaths.BOOKING);
        File tempFile = new File("data/temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null) {

                // ✅ use |
                String[] parts = line.split("\\|");

                if (parts.length >= 10 && parts[0].equals(id)) {

                    found = true;
                    
                    // GET DATA
                    String movie = parts[2];
                    String time = parts[3];
                    String seat = parts[6];

                    // FIND SHOWTIME ID
                    String showtimeId = getShowtimeId(movie, time);

                    // REMOVE SEAT
                    if(showtimeId != null){
                        SeatServiceCm seatService = new SeatServiceCm();
                        seatService.removeSeat(showtimeId, seat);
                    }

                    // ✅ CHANGE STATUS TO CANCELED
                    parts[7] = "CANCELED";

                    writer.println(String.join("|", parts));

                } else {
                    writer.println(line);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cancelling booking!");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        if (found)
            JOptionPane.showMessageDialog(this, "Booking Canceled Successfully!");
        else
            JOptionPane.showMessageDialog(this, "Booking ID Not Found!");
    }
    
    private String getShowtimeId(String movie, String time) {

        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.SHOWTIMES))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 6) {

                    String fileMovie = data[1].trim();
                    String fileTime = data[3].trim();

                    if (fileMovie.equalsIgnoreCase(movie.trim()) &&
                        fileTime.equalsIgnoreCase(time.trim())) {

                        return data[0].trim(); // ✅ return showtimeId
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
