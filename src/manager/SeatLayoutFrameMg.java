/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manager;

/**
 *
 * @author TRACY
 */

import javax.swing.*;
import java.awt.*;

public class SeatLayoutFrameMg extends JFrame{
    public SeatLayoutFrameMg(int hallNumber, String hallType){
        
        int rows, cols;
        
        // Set dimensions based on type
        // IMAX is the largest, 2D is standard, 4D is smaller
        switch(hallType){
            case "IMAX":
                rows = 10;
                cols = 9;
                break;
            case "4D":
                rows = 6;
                cols = 8;
                break;
            default: // 2D
                rows = 8;
                cols = 8;
                break;
        }

        setTitle(hallType + " Layout - Hall " + hallNumber);
        setSize(750,700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // ===== SCREEN =====
        JLabel screen = new JLabel("SCREEN", JLabel.CENTER);
        screen.setFont(new Font("Arial", Font.BOLD, 22));
        screen.setOpaque(true);
        screen.setBackground(Color.LIGHT_GRAY);
        screen.setForeground(Color.BLACK);
        screen.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(screen, BorderLayout.NORTH);

        
        // ===== SEAT GRID PANEL =====
        // use rows + 1 and cols + 1 to make room for the labels
        JPanel seatGrid = new JPanel(new GridLayout(rows + 1, cols + 1, 5, 5));
        seatGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Load Icon
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/seat.png"));
        Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon seatIcon = new ImageIcon(img);

        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c <= cols; c++) {
                
                if (r == 0 && c == 0) {
                    // Top-left corner spacer
                    seatGrid.add(new JLabel("")); 
                } 
                else if (r == 0) {
                    // Column Labels (1, 2, 3...)
                    JLabel label = new JLabel(String.valueOf(c), JLabel.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    seatGrid.add(label);
                } 
                else if (c == 0) {
                    // Row Labels (A, B, C...)
                    JLabel label = new JLabel(String.valueOf((char)('A' + r - 1)), JLabel.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 12));
                    seatGrid.add(label);
                } 
                else {
                    // Actual Seat
                    JButton seat = new JButton(seatIcon);
                    seat.setToolTipText("Seat " + (char)('A' + r - 1) + c);
                    seat.setBorderPainted(false);
                    seat.setContentAreaFilled(false);
                    seat.setEnabled(false); // Display only for now
                    seatGrid.add(seat);
                }
            }
        }

        // Wrap in ScrollPane for IMAX halls
        JScrollPane scrollPane = new JScrollPane(seatGrid);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // ===== FOOTER PANEL (Info + Button) =====
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hall Information
        JLabel infoLabel = new JLabel("Hall Type: " + hallType + " | Capacity: " + (rows * cols) + " seats", JLabel.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Done Viewing Button
        JButton btnDone = new JButton("Done viewing");
        btnDone.setPreferredSize(new Dimension(150, 35));
        
        btnDone.addActionListener(e -> {
            dispose(); // Close the layout view and returns to ManageHallFrame
        });

        footerPanel.add(infoLabel, BorderLayout.NORTH);
        footerPanel.add(btnDone, BorderLayout.SOUTH);
        
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
