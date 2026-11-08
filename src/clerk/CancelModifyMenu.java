/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clerk;

/**
 *
 * @author TRACY
 */

import javax.swing.*;
import java.awt.*;

public class CancelModifyMenu extends JFrame {
    public CancelModifyMenu() {

        setTitle("Booking Options");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Cancel / Modify Booking", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JButton cancelBtn = new JButton("Cancel Booking");
        JButton modifyBtn = new JButton("Modify Booking");
        JButton backBtn = new JButton("Back");

        cancelBtn.addActionListener(e -> {
            new CancelBookingFrame().setVisible(true);
            dispose();
        });

        modifyBtn.addActionListener(e -> {
            new ModifyBookingFrame().setVisible(true);
            dispose();
        });

        backBtn.addActionListener(e -> {
            new ClerkFrame().setVisible(true);
            dispose();
        });

        panel.add(title);
        panel.add(cancelBtn);
        panel.add(modifyBtn);
        panel.add(backBtn);

        add(panel);
    }

}
