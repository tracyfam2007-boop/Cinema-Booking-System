/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import service.CustomerService;
import javax.swing.*; 
import java.awt.*; 

public class CustomerRegisterFrame extends JFrame{
    public CustomerRegisterFrame() { 
        
        // SET TITLE
        setTitle("Customer Register"); 
        setSize(500, 350); 
        setLocationRelativeTo(null); 

        // TEXT PANEL
        JPanel textPanel = new JPanel(new GridLayout(4, 2, 10, 10)); 
        textPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        textPanel.setBackground(Color.WHITE); 

        JTextField fullNameField = new JTextField();
        JTextField phoneNumberField = new JTextField(); 
        JTextField dateOfBirthdayField = new JTextField();
        
        textPanel.add(new JLabel("Full Name:")); 
        textPanel.add(fullNameField);
        textPanel.add(new JLabel("Phone Number:")); 
        textPanel.add(phoneNumberField); 
        textPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):")); 
        textPanel.add(dateOfBirthdayField); 
        
        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
        
        JButton registerBtn = new JButton("Register"); 
        registerBtn.addActionListener(e -> { 
        
            String fullName = fullNameField.getText().trim(); 
            String phoneNumber = phoneNumberField.getText().trim(); 
            String dateOfBirthday = dateOfBirthdayField.getText().trim(); 

            // IF FIELD IS EMPTY
            if (fullName.isEmpty() || phoneNumber.isEmpty() || dateOfBirthday.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!"); 
                return; 
            } 

            // CALL CUSTOMERSERVICE REGISTER
            CustomerService cs = new CustomerService();
            String newId = cs.register(fullName, phoneNumber, dateOfBirthday);
            
            // SHOW MESSAGE
            JOptionPane.showMessageDialog(this, 
                    "Customer Registered!\nYour Customer id : " + newId); 
            new CustomerLoginFrame().setVisible(true); 
            dispose();
        }); 
        
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> { 
            new CustomerLoginFrame().setVisible(true); 
            dispose(); 
        }); 
        
        buttonPanel.add(registerBtn); 
        buttonPanel.add(backBtn);
        
        // SET LAYOUT
        setLayout(new BorderLayout());
        add(textPanel, BorderLayout.CENTER);     
        add(buttonPanel, BorderLayout.SOUTH);
   
    } 

}

