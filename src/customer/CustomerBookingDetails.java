/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.BookingCm;
import model.PaymentCm;
import service.PaymentServiceCm;
import javax.swing.*;
import java.awt.*;

public class CustomerBookingDetails extends JFrame{
    
    public CustomerBookingDetails(BookingCm booking, String customerId) {
        
        PaymentCm payment = PaymentServiceCm.getPaymentByBookingId(booking.getBookingId());

        // SET TITLE
        setTitle("Booking Details");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));      

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10)); 

        // USE BOOKING DATA DIRECTLY
        detailsPanel.add(new JLabel("Movie Title:"));
        detailsPanel.add(new JLabel(booking.getMovie()));

        detailsPanel.add(new JLabel("Show Time:"));
        detailsPanel.add(new JLabel(booking.getTime()));

        detailsPanel.add(new JLabel("Hall:"));
        detailsPanel.add(new JLabel(booking.getHallType() + " " + booking.getHall()));

        detailsPanel.add(new JLabel("Seat:"));
        detailsPanel.add(new JLabel(booking.getSeat()));

        detailsPanel.add(new JLabel("Payment Date:"));
        detailsPanel.add(new JLabel(payment != null ? payment.getPaymentDate() : "-"));

        detailsPanel.add(new JLabel("Payment Time:"));
        detailsPanel.add(new JLabel(payment != null ? payment.getPaymentTime() : "-"));

        detailsPanel.add(new JLabel("Payment Method:"));
        detailsPanel.add(new JLabel(payment != null ? payment.getPaymentMethod() : "-"));

        detailsPanel.add(new JLabel("Total Price:"));
        detailsPanel.add(new JLabel("RM " + String.format("%.2f", booking.getTotal())));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton cancelBtn = new JButton("Cancel Booking"); 
        cancelBtn.setPreferredSize(new Dimension(150, 35));

        cancelBtn.addActionListener(e -> { 
            JOptionPane.showMessageDialog(this, "Cancel Sucessful!"); 
            new service.BookingServiceCm().cancelBooking(booking.getBookingId());
            new CustomerViewBookingHistory(customerId).setVisible(true);
            dispose();
        }); 

        if (!booking.getBookingStatus().equals("ACTIVE")) {
            cancelBtn.setEnabled(false);
        }

        JButton backBtn = new JButton("Back"); 
        backBtn.setPreferredSize(new Dimension(150, 35));

        backBtn.addActionListener(e -> { 
            new CustomerViewBookingHistory(customerId).setVisible(true); 
            dispose(); 
        }); 

        buttonPanel.add(cancelBtn);
        buttonPanel.add(backBtn);

        mainPanel.add(detailsPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

}



