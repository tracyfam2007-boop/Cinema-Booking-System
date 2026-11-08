/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import service.BookingServiceCm;
import service.SeatServiceCm;
import service.PaymentServiceCm;
import model.ShowtimeMg;
import service.MovieServiceMg;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomerMakePayment extends JFrame{
    
    private String customerId;
    private String showtimeId;
    private String hall;
    private String selectedSeats;
    private String price;
    
    private JLabel subtotalLbl = new JLabel("Subtotal: RM 0.00");
    private JLabel discountLbl = new JLabel("Discount: RM 0.00");
    private JLabel pointsUsedLbl = new JLabel("Points Used: 0");
    private JLabel pointsEarnedLbl = new JLabel("Points Earned: 0");
    private JLabel totalLbl = new JLabel("Total   : RM 0.00");
    
    private JComboBox<String> seatTypeCombo;
    private JComboBox<String> paymentMethodCombo;
    private int pointsUsed;
    private int pointsEarned;
    
    public CustomerMakePayment (String customerId, 
                                String showtimeId, 
                                String hall,
                                String selectedSeats,
                                String price){
        
        this.customerId = customerId;
        this.showtimeId = showtimeId;
        this.hall = hall;
        this.selectedSeats = selectedSeats;
        this.price = price;
        
        // SET TITLE
        setTitle("Make Payment");
        setSize(450,400);
        setLocationRelativeTo(null);
        
        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // DETAILS PANEL (movieTitle, showdate, showtime, hall, seat)(get from previous java class)
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(2,2,10,10));
        
        JLabel hallLbl = new JLabel("Hall: " + hall);
        hallLbl.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel seatLbl = new JLabel("Seat: " + selectedSeats);
        seatLbl.setFont(new Font("Arial", Font.BOLD, 16));
        
        detailsPanel.add(hallLbl);
        detailsPanel.add(seatLbl);
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // SELECT PANEL FOR SEAT TYPE (single, student, senior/oku)(based on selection, apply price service)
        seatTypeCombo = new JComboBox<>(new String[]{"Adult", "Student", "Senior/OKU"});
        seatTypeCombo.addActionListener(e -> updatePrice());

        JPanel seatTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        seatTypePanel.add(new JLabel("Seat Type            :"));
        seatTypePanel.add(seatTypeCombo);
        mainPanel.add(seatTypePanel);
        
        // PAYMENT METHOD PANEL
        paymentMethodCombo = new JComboBox<>(new String[]{"Debit Card", "Credit Card", "Online Banking", "TnG"});
       
        JPanel paymentMethodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentMethodPanel.add(new JLabel("Payment Method:"));
        paymentMethodPanel.add(paymentMethodCombo);
        mainPanel.add(paymentMethodPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // PRICE PANEL
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new GridLayout(5,1,5,5));
        pricePanel.add(subtotalLbl);
        pricePanel.add(discountLbl);
        pricePanel.add(pointsUsedLbl);
        pricePanel.add(pointsEarnedLbl);
        pricePanel.add(totalLbl);
        mainPanel.add(pricePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // BUTTON PANEL
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton payBtn = new JButton("Confirm Payment");
        payBtn.setFont(new Font("Arial", Font.BOLD, 16));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 16));
        
        buttonPanel.add(payBtn);
        buttonPanel.add(cancelBtn);
        mainPanel.add(buttonPanel);
        
        updatePrice();
        
        payBtn.addActionListener(e -> {

            PaymentServiceCm paymentService = new PaymentServiceCm();
            BookingServiceCm bookingService = new BookingServiceCm();
            SeatServiceCm seatService = new SeatServiceCm();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String paymentTime = LocalTime.now().format(formatter);
            String paymentMethod = (String) paymentMethodCombo.getSelectedItem();

            String totalPrice = totalLbl.getText().replaceAll("[^0-9.]", "");
            double finalTotalPrice = Double.parseDouble(totalPrice);
            
            ShowtimeMg showtime = MovieServiceMg.getShowtimeById(showtimeId);

            String customerName = paymentService.getCustomerName(customerId);

            String bookingId = bookingService.recordBooking(
                    customerName,   
                    showtime.getMovieTitle(),
                    showtime.getTime(),
                    showtime.getHallType(),
                    showtime.getHall(),
                    selectedSeats,
                    finalTotalPrice,
                    "ACTIVE",
                    paymentMethod
            );

            // record seat
            seatService.recordSeat(showtimeId, selectedSeats);
            
            paymentService.recordPayment(
                    bookingId,
                    LocalDate.now().toString(),
                    paymentTime,
                    finalTotalPrice,
                    paymentMethod
            );
            
            // record points
            paymentService.updatePoints(customerId, -pointsUsed);
            paymentService.updatePoints(customerId, pointsEarned);

            JOptionPane.showMessageDialog(this,"Payment Successful!");
            new CustomerDashboard(customerId).setVisible(true);
            dispose();
        });
        
        cancelBtn.addActionListener(e -> {
            new CustomerMovieList(customerId).setVisible(true); // go back
            dispose();
        });
        
        add(mainPanel);
    }

    // UPDATE PRICEPANEL BY CALLING PAYMNET SERVICE
    private void updatePrice(){

        PaymentServiceCm paymentService = new PaymentServiceCm();

        String seatType = (String) seatTypeCombo.getSelectedItem();
        int seatCount = selectedSeats.split(",").length;
        
        double basePrice;
        try {
            basePrice = Double.parseDouble(price);
            if (basePrice == 0) basePrice = 15.0; // default fallback
        } catch (Exception e) {
            basePrice = 15.0;
        }
        
        double subtotal = paymentService.calculateSubtotal(basePrice, seatCount);
        
        String fixedSeatType = seatType.equals("Senior/OKU") ? "Senior" : seatType;
        
        double discount = paymentService.calculateDiscount(fixedSeatType, subtotal);
        double totalBeforePoints = paymentService.calculateTotalBeforePoints(subtotal, discount);

        int customerPoints = paymentService.getCustomerPoints(customerId);
        
        double pointDiscount = paymentService.calculatePointsUsed(totalBeforePoints, customerPoints);
        pointsUsed = (int)(pointDiscount * 10); // store as points

        double totalAfterPoints = totalBeforePoints - pointDiscount;

        pointsEarned = paymentService.calculateEarnedPoints(totalAfterPoints);

        subtotalLbl.setText("Subtotal                                          : RM " + String.format("%.2f", subtotal));
        discountLbl.setText("Discount                                         : RM " + String.format("%.2f", discount));
        pointsUsedLbl.setText("Points Used (10 point = -RM1)    : " + pointsUsed);
        pointsEarnedLbl.setText("Points Earned (RM 10 = 1 point) : " + pointsEarned);
        totalLbl.setText("Total                                                : RM " + String.format("%.2f", totalAfterPoints));
        
        // call updatepoint from paymentservice to save point record in file
      
    } 

}

