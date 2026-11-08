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

public class ManageHallFrameMg extends JFrame {
    
    private JComboBox<String> hallCombo;
    private JLabel hallTypeLabel;
    
    public ManageHallFrameMg(){
        
        setTitle("Manage Halls");
        setSize(420, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        hallCombo = new JComboBox<>();
        
        // 10 halls
        for(int i = 1; i <= 10; i++){
            hallCombo.addItem("Hall " + i);
        }
        
        hallTypeLabel = new JLabel("Hall Type: IMAX (Halls 1-3)", JLabel.CENTER);
        hallTypeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JButton btnCheck = new JButton("Check Hall Type");
        JButton btnSeatLayout = new JButton("Open Seat Layout"); 
        JButton btnPrevious = new JButton("Previous");
        
        btnCheck.addActionListener(e -> showHallType());
        
        // Open Seat Layout
        btnSeatLayout.addActionListener(e -> {
            
            int hallNumber = hallCombo.getSelectedIndex() + 1;
            String type;
            
            // Logic for Hall Types
            if(hallNumber <= 3) type = "IMAX";
            else if(hallNumber <= 6) type = "2D";
            else type = "4D";
            
            new SeatLayoutFrameMg(hallNumber, type);
        });
        
        btnPrevious.addActionListener(e -> {
            dispose(); // Close this frame
            new ManagerFrame(); // Open the Manager Dashboard
        });
        
        
        mainPanel.add(new JLabel("Select Hall:", JLabel.LEFT));
        mainPanel.add(hallCombo);
        mainPanel.add(btnCheck);
        mainPanel.add(btnSeatLayout);
        mainPanel.add(btnPrevious);
        mainPanel.add(hallTypeLabel);
        
        add(mainPanel);
        
        setVisible(true);
    }
    
    private void showHallType(){
        
        int hallNumber = hallCombo.getSelectedIndex() + 1;
        
        String type = (hallNumber <= 3) ? "IMAX" : (hallNumber <= 6) ? "2D" : "4D";
        hallTypeLabel.setText("Hall Type: " + type);
 
    }
}
