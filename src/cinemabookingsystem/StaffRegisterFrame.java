/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cinemabookingsystem;

/**
 *
 * @author TRACY
 */

import manager.FileManager;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class StaffRegisterFrame extends JFrame{
    public StaffRegisterFrame(String role) {
        setTitle(role + " Register");
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(Color.WHITE);

        JTextField idField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JPasswordField confirmField = new JPasswordField();

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        registerBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String pass = new String(passField.getPassword());
            String confirm = new String(confirmField.getPassword());
            
            if (id.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
                return;
            }

            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!");
                return;
            }

            try {
                FileManager.saveStaff(id, pass, role);
                JOptionPane.showMessageDialog(this, "Account Created!");
                new LoginFrame(role).setVisible(true);
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
                backBtn.addActionListener(e -> {
            new LoginFrame(role).setVisible(true);
            dispose();
        });

        panel.add(new JLabel("Staff ID:"));
        panel.add(idField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmField);
        panel.add(registerBtn);
        panel.add(backBtn);

        add(panel);
    }
}
