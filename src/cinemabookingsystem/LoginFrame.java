/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinemabookingsystem;

/**
 *
 * @author TRACY
 */

import clerk.ClerkFrame;
import manager.ManagerFrame;
import manager.FileManager;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginFrame extends JFrame {
    public LoginFrame(String role) {
        setTitle(role + " Login");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        JTextField idField = new JTextField();
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        loginBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String pass = new String(passField.getPassword());

            if (id.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
                return;
            }

            try {
                if (FileManager.validateStaff(id, pass, role)) {
                    if (role.equals("Manager"))
                        new ManagerFrame().setVisible(true);
                    else
                        new ClerkFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Login!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        registerBtn.addActionListener(e -> {
            new StaffRegisterFrame(role).setVisible(true);
            dispose();
        });

        backBtn.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });

        panel.add(new JLabel("Staff ID:"));
        panel.add(idField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(loginBtn);
        panel.add(registerBtn);
        panel.add(backBtn);

        add(panel);
    }        
}
