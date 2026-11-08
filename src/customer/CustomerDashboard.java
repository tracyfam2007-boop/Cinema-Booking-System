/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*; 

// INHERITANCE (GUI FUNCTIONALITIES)
// REUSE AND ADD OWN VARIABLES AND METHODS
public class CustomerDashboard extends JFrame{
    private String customerId;
    
    public CustomerDashboard(String customerId){
        
        this.customerId = customerId;
        
        // SET TITLE
        setTitle("Customer Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null); 
        
        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 15, 15)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50)); 
        buttonPanel.setBackground(Color.WHITE); 
    
        // BUTTON 
        JButton profileBtn = new JButton("My Profile"); 
        JButton movieBtn = new JButton("Movies");  
        JButton historyBtn = new JButton("Booking History"); 
        JButton logoutBtn = new JButton("Logout"); 

        // POLYMERPHISM
        profileBtn.addActionListener(e -> {
            new CustomerProfile(this.customerId).setVisible(true);
            dispose();
        });

        movieBtn.addActionListener(e -> {
            new CustomerMovieList(this.customerId).setVisible(true);
            dispose();
        });
        
        historyBtn.addActionListener(e -> {
            new CustomerViewBookingHistory(customerId).setVisible(true);
            dispose();
        });
        
        logoutBtn.addActionListener(e -> {
            new CustomerLogout().setVisible(true);
            dispose();
        });

        // ADD BUTTON INTO PANEL
        buttonPanel.add(profileBtn);
        buttonPanel.add(movieBtn);
        buttonPanel.add(historyBtn);
        buttonPanel.add(logoutBtn);

        add(buttonPanel);
    }

}

