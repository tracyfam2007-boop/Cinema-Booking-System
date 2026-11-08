/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manager;

/**
 *
 * @author TRACY
 */
import cinemabookingsystem.LoginFrame;
import javax.swing.*;
import java.awt.*;

public class ManagerFrame extends JFrame{
    public ManagerFrame() {
        setTitle("Manager Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        
        // ===== Title =====
        JLabel titleLabel = new JLabel("CINEMA MANAGER DASHBOARD", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0,0));
        add(titleLabel, BorderLayout.NORTH);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton btnAddMovie = new JButton("Add Movie");
        JButton btnManageShowtime = new JButton("Manage Showtime");
        JButton btnManageHall = new JButton("Manage Hall");
        JButton btnSetPrice = new JButton("Set Ticket Price");
        JButton btnViewReport = new JButton("View Report");
        JButton btnLogout = new JButton("Logout");
        
        
        // Style Logout button
        btnLogout.setBackground(new Color(220, 20, 60));
        btnLogout.setBackground(Color.WHITE);
        
        // ===== Button Actions =====

        // Open Add Movie
        btnAddMovie.addActionListener(e -> {
            dispose(); // Close dashboard
            new AddMovieFrameMg();  // Open Add Movie
        });

        // Open Manage Showtime
        btnManageShowtime.addActionListener(e -> {
            dispose(); // Close dashboard
            new ManageShowtimeFrameMg();  // Open manage showtime
        });

        // Open Manage Hall
        btnManageHall.addActionListener(e -> {
            dispose();
            new ManageHallFrameMg();
        });

        // Open Set Ticket Price
        btnSetPrice.addActionListener(e -> {
            dispose();
            new SetPriceFrameMg();
        });
        
        // Open Report
        btnViewReport.addActionListener(e -> {
            dispose();
            new ManagerReportFrame();
        });

        // Logout
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame("Manager").setVisible(true);   // Return to login
        });

        // Add buttons to panel
        buttonPanel.add(btnAddMovie);
        buttonPanel.add(btnManageShowtime);
        buttonPanel.add(btnManageHall);
        buttonPanel.add(btnSetPrice);
        buttonPanel.add(btnViewReport);
        buttonPanel.add(btnLogout);
        
        add(buttonPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
}
