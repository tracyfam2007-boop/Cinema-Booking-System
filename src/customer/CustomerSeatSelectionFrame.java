/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import model.ShowtimeMg;
import service.SeatServiceCm;
import service.TicketPriceServiceCm;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CustomerSeatSelectionFrame extends JFrame{
    private final String customerId;
    private final String showtimeId;
    private final String hall;
    private final String price;
    
    private List<String> selectedSeats = new ArrayList<>();
    
    public CustomerSeatSelectionFrame(String customerId, ShowtimeMg showtime) {
        
        this.customerId = customerId;
        this.showtimeId = showtime.getShowtimeId();
        this.hall = showtime.getHallType();

        // LOAD PRICE FROM ticketprice.txt
        TicketPriceServiceCm priceService = new TicketPriceServiceCm();
        double priceValue = priceService.getPriceByHallType(
                showtime.getMovieTitle(),
                showtime.getHallType()
        );

        this.price = String.valueOf(priceValue);
        
        // SET TITLE
        setTitle ("Select Seats - " + hall);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        topPanel();
        seatPanel();
        footerPanel();
    }

    
    private void topPanel(){
        // TOP PANEL (BACK BUTTON + SCREEN)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // BACK BUTTON
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new CustomerMovieList(customerId).setVisible(true); // go back
            dispose();
        });
        
        // SCREEN LABEL
        JLabel screenLbl = new JLabel("SCREEN", JLabel.CENTER);
        screenLbl.setFont(new Font("Arial", Font.BOLD, 22));
        screenLbl.setOpaque(true);
        screenLbl.setBackground(Color.LIGHT_GRAY);
        screenLbl.setForeground(Color.BLACK);
        screenLbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        topPanel.add(backBtn, BorderLayout.WEST); // back button on left
        topPanel.add(screenLbl, BorderLayout.CENTER); // screen in center

        add(topPanel, BorderLayout.NORTH); 
    }
    
    // LOAD BOOKED SEATS
    private void seatPanel(){
        
        // READ BOOKED SEAT
        Set<String> bookedSeats = SeatServiceCm.getBookedSeats(showtimeId);
        
        // DETERMINE ROWS & COLUMNS BASED ON HALL
        int rows = 8, columns = 8; 
        switch (hall){ 
            case "IMAX": rows = 10; columns = 9; 
            break; 
            case "2D": rows = 6; columns = 8; 
            break; 
            case "4D": rows = 8; columns = 8; 
            break; 
        }
        
        // SEAT GRID PANEL
        JPanel seatGridPanel = new JPanel(new GridLayout(rows + 1, columns + 1, 5, 5));
        seatGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        for (int r = 0; r <= rows; r++){ 
            for (int c = 0; c <= columns; c++){ 
                if (r == 0 && c == 0){ 
                    seatGridPanel.add(new JLabel("")); 
                } else if (r == 0){ 
                    JLabel columnLbl = new JLabel(String.valueOf(c), JLabel.CENTER); 
                    seatGridPanel.add(columnLbl); 
                } else if (c == 0){ 
                    JLabel rowLbl = new JLabel(String.valueOf((char)('A' + r - 1)), JLabel.CENTER); 
                    seatGridPanel.add(rowLbl); 
                } else { 
                    String seatId = "" + (char)('A' + r - 1) + c; 
                    JButton seatBtn = new JButton(seatId); 
                    if(bookedSeats.contains(seatId)){ 
                        seatBtn.setEnabled(false); 
                        seatBtn.setBackground(new Color(200,200,200)); 
                        seatBtn.setOpaque(true); 
                        seatBtn.setBorderPainted(false); 
                    }else{ 
                        seatBtn.setBackground(new Color(180, 230, 180)); 
                        seatBtn.setOpaque(true); 
                        seatBtn.addActionListener (e -> toggleSeat(seatBtn)); 
                    } 
                    seatGridPanel.add(seatBtn); 
                } 
            } 
        }

        // WARP IN SCROLLPANE FOR IMAX 
        JScrollPane scrollPane = new JScrollPane(seatGridPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void footerPanel(){
        // FOOTER PANEL
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton confirmBtn = new JButton("Confirm Booking");
        confirmBtn.setPreferredSize(new Dimension(200,40));
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 16));
        
        confirmBtn.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one seat.");
                return;
            }
            
            JOptionPane.showMessageDialog(this, "Seats confirmed: " + selectedSeats);
            dispose();
            new CustomerMakePayment(this.customerId, 
                                    showtimeId,
                                    hall,
                                    String.join(",", selectedSeats),
                                    price
            ).setVisible(true); 
            dispose();
        });
        
        footerPanel.add(confirmBtn, BorderLayout.SOUTH);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    // TOGGLESEAT SELECTION
    private void toggleSeat(JButton seatBtn){
        
        String seatId = seatBtn.getText();
        
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            seatBtn.setBackground(new Color(180, 230, 180)); // green
        } else {
            selectedSeats.add(seatId);
            seatBtn.setBackground(new Color(120, 170, 240)); // blue
        }
    }

}
