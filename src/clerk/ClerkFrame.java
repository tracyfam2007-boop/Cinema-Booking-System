/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clerk;

/**
 *
 * @author TRACY
 */

import cinemabookingsystem.LoginFrame;
import javax.swing.*;
import java.awt.*; 

public class ClerkFrame extends JFrame{
    
    public ClerkFrame(){
        setTitle("Ticketing Clerk Menu");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(8, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel label = new JLabel("Ticketing Clerk Menu", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(new Color(33, 56, 99));

        JButton bookBtn = createButton("Book Ticket");
        JButton cancelBtn = createButton("Cancel / Modify Booking");
        JButton searchBtn = createButton("Search Booking");
        JButton screenBtn = createButton("View Screen Details");
        JButton paymentBtn = createButton("Payment & Receipt");
        JButton viewReceiptBtn = createButton("View Receipt");
        JButton logoutBtn = createButton("Logout");

        // Button Actions

        bookBtn.addActionListener(e -> {
            new BookTicketFrame().setVisible(true);
            dispose();
        });
        
        cancelBtn.addActionListener(e -> {
            new CancelModifyMenu().setVisible(true);
            dispose();
        });
        
        searchBtn.addActionListener(e -> {
            new SearchBookingFrame().setVisible(true);
            dispose();
        });
        
        screenBtn.addActionListener(e -> {
            new ScreenDetailsFrame().setVisible(true);
            dispose();
        });
        
        paymentBtn.addActionListener(e -> {
            new PaymentReceiptFrame().setVisible(true);
            dispose();
        });
        
        viewReceiptBtn.addActionListener(e -> {
            new ViewReceiptFrame().setVisible(true);
            dispose();
        });
        
        logoutBtn.addActionListener(e -> {
            new LoginFrame("Clerk").setVisible(true);
            dispose();
        });

        panel.add(label);
        panel.add(bookBtn);
        panel.add(cancelBtn);
        panel.add(searchBtn);
        panel.add(screenBtn);
        panel.add(paymentBtn);
        panel.add(viewReceiptBtn);
        panel.add(logoutBtn);

        add(panel);
    }

    // Reusable button styling
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 91, 160));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }


}
