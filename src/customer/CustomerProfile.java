/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import model.Customer;
import service.CustomerService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*; 

public class CustomerProfile extends JFrame{
    public CustomerProfile(String customerId){
        
        CustomerService cs = new CustomerService();
        Customer customer = cs.getDetails(customerId);
        
        // SET TITLE
        setTitle("Personal Details"); 
        setSize(400, 300); 
        setLocationRelativeTo(null); 
        
        // TEXT PANEL
        JPanel textPanel = new JPanel(new GridLayout(5, 2, 10, 10)); 
        textPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        textPanel.setBackground(Color.WHITE); 
        
        JLabel customerIdLabel = new JLabel(customer.getCustomerId());
        JLabel fullNameLabel = new JLabel(customer.getFullName());
        JLabel phoneNumberLabel = new JLabel(customer.getPhoneNumber());
        JLabel dateOfBirthdayLabel = new JLabel(customer.getDateOfBirthday());
        JLabel rewardPointsLabel = new JLabel(String.valueOf(customer.getPoints()));
        
        textPanel.add(new JLabel("Customer ID:")); 
        textPanel.add(customerIdLabel); 
        textPanel.add(new JLabel("Full Name:")); 
        textPanel.add(fullNameLabel); 
        textPanel.add(new JLabel("Phone Number:")); 
        textPanel.add(phoneNumberLabel); 
        textPanel.add(new JLabel("Date of Birth:")); 
        textPanel.add(dateOfBirthdayLabel); 
        textPanel.add(new JLabel("Reward Points:")); 
        textPanel.add(rewardPointsLabel); 
        
        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        buttonPanel.setBackground(Color.WHITE); 
        
        JButton updateDetailsBtn = new JButton("Update details");
        updateDetailsBtn.addActionListener(e -> { 
            new CustomerUpdatePersonalDetails(customer).setVisible(true); 
            dispose(); 
        }); 
        
        JButton backBtn = new JButton("Back"); 
        backBtn.addActionListener(e -> { 
            new CustomerDashboard(customerId).setVisible(true); 
            dispose(); 
        });
        
        buttonPanel.add(updateDetailsBtn);
        buttonPanel.add(backBtn); 
        
        // SET LAYOUT
        setLayout(new BorderLayout());
        add(textPanel, BorderLayout.CENTER);     
        add(buttonPanel, BorderLayout.SOUTH);
    }

}

