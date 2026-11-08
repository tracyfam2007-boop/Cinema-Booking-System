/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinemabookingsystem;

/**
 *
 * @author TRACY
 */

import customer.CustomerLoginFrame;
import javax.swing.*;
import java.awt.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class WelcomeFrame extends JFrame{
    
    public WelcomeFrame() {
        setTitle("Cinema Booking System");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(5, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel label = new JLabel("Welcome to Cinema Booking System", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(33, 56, 99));

        JButton clerkBtn = createButton("Ticketing Clerk");
        JButton managerBtn = createButton("Cinema Manager");
        JButton customerBtn = createButton("Customer");

        clerkBtn.addActionListener(e -> {
            new LoginFrame("Clerk").setVisible(true);
            dispose();
        });
        
        managerBtn.addActionListener(e -> {
            new LoginFrame("Manager").setVisible(true);
            dispose();
        });

        customerBtn.addActionListener(e -> {
            new CustomerLoginFrame().setVisible(true);
            dispose();
        });

        panel.add(label);
        panel.add(clerkBtn);
        panel.add(managerBtn);
        panel.add(customerBtn);

        add(panel);
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 91, 160));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }
}
