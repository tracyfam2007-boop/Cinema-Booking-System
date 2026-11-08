/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import cinemabookingsystem.WelcomeFrame;
import javax.swing.*; 
import java.awt.*;  

public class CustomerLogout extends JFrame{
    public CustomerLogout() {
        
        setTitle("Customer Logout"); 
        setSize(400, 300); 
        setLocationRelativeTo(null); 

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        panel.setBackground(Color.WHITE); 
        
        JButton backBtn = new JButton("Back");
        JButton logoutBtn = new JButton("Confirm"); 

        backBtn.addActionListener(e -> { 
            new WelcomeFrame().setVisible(true); 
            dispose(); 
        }); 
        
        logoutBtn.addActionListener(e -> { 
            JOptionPane.showMessageDialog(this, "Logout Successful!"); 
            new CustomerLoginFrame().setVisible(true); 
            dispose(); 
        }); 
        
        panel.add(new JLabel("Do you want to logout?"));  
        panel.add(logoutBtn); 
        panel.add(backBtn);
        
        add(panel);
    }

}


