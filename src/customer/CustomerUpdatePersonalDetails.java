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

public class CustomerUpdatePersonalDetails extends JFrame{
    public CustomerUpdatePersonalDetails(Customer customer) {
        
    // SET TITLE
        setTitle("Update Personal Details"); 
        setSize(400, 400); 
        setLocationRelativeTo(null); 
  
        // TEXT PANEL
        JPanel textPanel = new JPanel(new GridLayout(5, 2, 10, 10)); 
        textPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        textPanel.setBackground(Color.WHITE); 

        JTextField newFullNameField = new JTextField(customer.getFullName()); 
        JTextField newPhoneNumberField = new JTextField(customer.getPhoneNumber());
        JTextField newDateOfBirthdayField = new JTextField(customer.getDateOfBirthday());
        
        textPanel.add(new JLabel("Full Name:")); 
        textPanel.add(newFullNameField); 
        textPanel.add(new JLabel("Phone Number:")); 
        textPanel.add(newPhoneNumberField); 
        textPanel.add(new JLabel("Date of Birth:")); 
        textPanel.add(newDateOfBirthdayField); 
        
        
        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        buttonPanel.setBackground(Color.WHITE); 
        
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            
            String fullName = newFullNameField.getText().trim();
            String phoneNumber = newPhoneNumberField.getText().trim();
            String dateOfBirthday = newDateOfBirthdayField.getText().trim();
        
            // IF FIELD IS EMPTY
            if(fullName.isEmpty() || phoneNumber.isEmpty() || dateOfBirthday.isEmpty()){
                JOptionPane.showMessageDialog(this,"All fields must be filled.");
                return;
           }
            
            // UPDATE CUSTOMER OBJECT
            customer.setFullName(fullName);
            customer.setPhoneNumber(phoneNumber);
            customer.setDateOfBirthday(dateOfBirthday);
    
            // CALL CUSTOMERSERVICE UPDATEDETAILS
            CustomerService cs = new CustomerService();
            cs.updateDetails(customer);
        
            // SHOW MESSAGE
            JOptionPane.showMessageDialog(this, "Details Updated!"); 
            new CustomerDashboard(customer.getCustomerId()).setVisible(true);
            dispose();
        });
        
        JButton backBtn = new JButton("Back");  
        backBtn.addActionListener(e -> { 
            new CustomerProfile(customer.getCustomerId()).setVisible(true); 
            dispose(); 
        }); 
        
        buttonPanel.add(saveBtn); 
        buttonPanel.add(backBtn); 
        

        // SET LAYOUT
        setLayout(new BorderLayout());
        add(textPanel, BorderLayout.CENTER);     
        add(buttonPanel, BorderLayout.SOUTH);
        
    } 

}
