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
import service.CustomerService;
import javax.swing.*; 
import java.awt.*; 

public class CustomerLoginFrame extends JFrame{
     // FRAME & LAYOUT
    public CustomerLoginFrame() { 
        
        // TITLE
        setTitle("Customer Login"); 
        setSize(400, 300); 
        setLocationRelativeTo(null); 

        // TEXT PANEL (Customer ID & Phone Number)
        JPanel textPanel = new JPanel(new GridLayout(2, 2, 10, 10)); 
        textPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        textPanel.setBackground(Color.WHITE); 

        JTextField customerIdField = new JTextField();
        JTextField phoneNumberField = new JTextField(); 
        
        textPanel.add(new JLabel("Customer ID:")); 
        textPanel.add(customerIdField); 
        textPanel.add(new JLabel("Phone Number:")); 
        textPanel.add(phoneNumberField); 
         
        // BUTTON PANEL (Login, Register, Back)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); 
        buttonPanel.setBackground(Color.WHITE); 
        
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> { 
            
            String customerId = customerIdField.getText().trim(); 
            String phoneNumber = phoneNumberField.getText().trim();
            
            // IF FIELD IS EMPTY
            if (customerId.isEmpty()|| phoneNumber.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!"); 
                return; 
            }
            
            // CALL CUSTOMERSERVICE LOGIN
            CustomerService cs = new CustomerService();
            boolean success = cs.login(customerId, phoneNumber);

            if (success) { 
                JOptionPane.showMessageDialog(this, "Login Successful!"); 
                new CustomerDashboard(customerId).setVisible(true); 
                dispose(); 
            } else { 
                JOptionPane.showMessageDialog(this, "Customer not found!"); 
            } 
        }); 
        
        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> { 
            new CustomerRegisterFrame().setVisible(true); 
            dispose(); 
        });
        
        JButton backBtn = new JButton("Back"); 
        backBtn.addActionListener(e -> { 
            new WelcomeFrame().setVisible(true); 
            dispose(); 
        });
        
        buttonPanel.add(loginBtn); 
        buttonPanel.add(registerBtn); 
        buttonPanel.add(backBtn);
        
        // SET LAYOUT
        setLayout(new BorderLayout());
        add(textPanel, BorderLayout.CENTER);     
        add(buttonPanel, BorderLayout.SOUTH);
    } 

}
