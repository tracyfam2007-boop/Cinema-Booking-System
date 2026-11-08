/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import model.BookingCm;
import service.BookingServiceCm;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerViewBookingHistory extends JFrame{
    
    public final String customerId;
    private BookingServiceCm bookingService = new BookingServiceCm();
    
    public CustomerViewBookingHistory(String customerId) {
        
        this.customerId = customerId;
        
        // SET TITLE
        setTitle("Booking History"); 
        setSize(600, 450); 
        setLocationRelativeTo(null); 
        
        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbledPane = new JTabbedPane();

        // ACTIVE & PAST BOOKING LIST PANEL IN MENU PANEL
        JPanel activePanel = new JPanel();
        activePanel.setLayout(new BoxLayout(activePanel, BoxLayout.Y_AXIS));
        JPanel pastPanel = new JPanel();
        pastPanel.setLayout(new BoxLayout(pastPanel, BoxLayout.Y_AXIS));
        
        // BACK BUTTON IN MENU PANEL (GO BACK DASHBOARD)
        JButton backBtn = new JButton("< Back"); 
        backBtn.addActionListener(e -> { 
            new CustomerDashboard(this.customerId).setVisible(true); 
            dispose(); 
        }); 

        // BACK PANEL TO KEEP BACK BUTTON LEFT TOP
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backBtn);
        mainPanel.add(backPanel, BorderLayout.NORTH);
        
        // DISPLAY BOOKING LIST
        List<BookingCm> allBookings = bookingService.getBookings(customerId);
        
        for (BookingCm b : allBookings) {        
            
            JButton bookingBtn = new JButton(
                    "Time: " + b.getTime() +
                    " | Movie: " + b.getMovie() +
                    " | Seat: " + b.getSeat() +
                    " | Price: RM " + String.format("%.2f", b.getTotal())
            );

            bookingBtn.addActionListener(e -> {
                new CustomerBookingDetails(b, customerId).setVisible(true);
                dispose();
            });
            
            bookingBtn.setPreferredSize(new Dimension(520, 40));
            bookingBtn.setFont(new Font("Arial", Font.PLAIN, 14));
              
            // IF BOOKING IS ACTIVE, DISPLAY ON ACTIVE PANEL
            // ELSE, DISPLAY ON PAST PANEL
            if (b.getBookingStatus().equals("ACTIVE")) {
                activePanel.add(bookingBtn);
                activePanel.add(Box.createVerticalStrut(10));
            } else {
                pastPanel.add(bookingBtn);
                pastPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        // TABLE FOR NOW & UPCOMING PANEL ON MENUPANEL
        tabbledPane.addTab("Active Booking", new JScrollPane(activePanel));
        tabbledPane.addTab("Past Booking", new JScrollPane(pastPanel)); 
        mainPanel.add(tabbledPane, BorderLayout.CENTER);
        
        add(mainPanel);  
    }

}

