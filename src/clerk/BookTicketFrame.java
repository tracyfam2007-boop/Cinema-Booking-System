/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clerk;

/**
 *
 * @author TRACY
 */

import utils.FilePaths;
import service.PaymentServiceCm;
import service.SeatServiceCm; 
import model.ShowtimeMg;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class BookTicketFrame extends JFrame{
    
    private JTextField nameField;
    private JTextField seatField;

    private JComboBox<String> movieBox;
    private JComboBox<ShowtimeMg> showtimeBox;
    private JComboBox<String> hallTypeBox;
    private JComboBox<String> hallNumberBox;
    private JComboBox<String> paymentBox;
    private JComboBox<String> userTypeBox;

    private JLabel priceLabel;

    private HashMap<String,String[]> showtimeMap = new HashMap<>();

    public BookTicketFrame(){

        setTitle("Book Ticket");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Book Ticket", JLabel.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,26));
        add(title,BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(6,2,20,15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,40,10,40));

        nameField = new JTextField();
        seatField = new JTextField();
        seatField.setEditable(false);

        movieBox = new JComboBox<>();
        showtimeBox = new JComboBox<>();
        hallTypeBox = new JComboBox<>();
        hallNumberBox = new JComboBox<>();

        paymentBox = new JComboBox<>(new String[]{
            "Debit Card","Credit Card","Online Banking","TnG"
        });

        userTypeBox = new JComboBox<>(new String[]{"Adult","Student","Senior"});

        hallTypeBox.setEnabled(false);
        hallNumberBox.setEnabled(false);

        priceLabel = new JLabel("RM 0.00");
        priceLabel.setFont(new Font("Arial",Font.BOLD,14));

        loadMovies();

        JButton seatBtn = new JButton("Select Seat");

        seatBtn.addActionListener(e -> {
            ShowtimeMg selected = (ShowtimeMg) showtimeBox.getSelectedItem();

            if(selected == null){
                JOptionPane.showMessageDialog(this,"Select showtime first!");
                return;
            }

            String hallType = selected.getHallType();
            String showtimeId = selected.getShowtimeId();

            new SeatSelectionFrame(hallType, showtimeId, seat -> {
                seatField.setText(seat);
                updatePrice();
            });
        });

        movieBox.addActionListener(e -> loadShowtimesByMovie());
        showtimeBox.addActionListener(e -> updateShowtimeDetails());
        userTypeBox.addActionListener(e -> updatePrice());

        mainPanel.add(createRow("Customer Name:",nameField));
        mainPanel.add(createRow("Movie:",movieBox));
        mainPanel.add(createRow("Showtime:",showtimeBox));
        mainPanel.add(createRow("Hall Type:",hallTypeBox));
        mainPanel.add(createRow("Hall Number:",hallNumberBox));
        mainPanel.add(createRow("Customer Type:",userTypeBox));

        JPanel seatPanel = new JPanel(new BorderLayout(5,0));
        seatPanel.add(seatField,BorderLayout.CENTER);
        seatPanel.add(seatBtn,BorderLayout.EAST);

        mainPanel.add(createRow("Seat:",seatPanel));
        mainPanel.add(createRow("Payment Method:",paymentBox));
        mainPanel.add(createRow("Ticket Price:",priceLabel));

        add(mainPanel,BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton confirmBtn = new JButton("Confirm Booking");
        JButton backBtn = new JButton("Back");

        btnPanel.add(confirmBtn);
        btnPanel.add(backBtn);
        add(btnPanel,BorderLayout.SOUTH);
        
        //Polymorphism
        confirmBtn.addActionListener(e -> confirmBooking());

        backBtn.addActionListener(e -> {
            new ClerkFrame().setVisible(true);
            dispose();
        });
    }

    private JPanel createRow(String label,Component field){
        JPanel panel = new JPanel(new BorderLayout(10,0));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(120,25));
        panel.add(lbl,BorderLayout.WEST);
        panel.add(field,BorderLayout.CENTER);
        return panel;
    }

    private void loadMovies(){
        try(BufferedReader br = new BufferedReader(new FileReader(FilePaths.MOVIES))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if(data.length >= 6 && data[5].equals("Now Available")){
                    movieBox.addItem(data[1].trim());
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error loading movies");
        }
    }

    private void loadShowtimesByMovie(){
        showtimeBox.removeAllItems();
        hallTypeBox.removeAllItems();
        hallNumberBox.removeAllItems();

        if(movieBox.getSelectedItem() == null) return;

        String selectedMovie = movieBox.getSelectedItem().toString().trim();

        try(BufferedReader br = new BufferedReader(new FileReader(FilePaths.SHOWTIMES))){
            String line;

            while((line = br.readLine()) != null){

                String[] data = line.split(",");

                if(data.length >= 6){

                    if(data[1].trim().equalsIgnoreCase(selectedMovie)){

                        ShowtimeMg showtime = new ShowtimeMg(
                                data[0].trim(),
                                data[1].trim(),
                                data[2].trim(),
                                data[3].trim(),
                                data[4].trim(),
                                data[5].trim()
                        );

                        showtimeBox.addItem(showtime);
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(this,"Error loading showtime!");
        }
    }

    private void updateShowtimeDetails(){
        ShowtimeMg selected = (ShowtimeMg) showtimeBox.getSelectedItem();

        if(selected == null) return;

        hallTypeBox.removeAllItems();
        hallNumberBox.removeAllItems();

        hallTypeBox.addItem(selected.getHallType());
        hallNumberBox.addItem(selected.getHall());

        updatePrice();
    }

    private void updatePrice(){
        ShowtimeMg selected = (ShowtimeMg) showtimeBox.getSelectedItem();
        if(selected == null) return;

        double basePrice = getMoviePrice(selected.getMovieTitle(), selected.getHallType());
        int seatCount = seatField.getText().isEmpty() ? 1 : seatField.getText().split(",").length;

        PaymentServiceCm ps = new PaymentServiceCm();
        double subtotal = ps.calculateSubtotal(basePrice,seatCount);
        double discount = ps.calculateDiscount(userTypeBox.getSelectedItem().toString(), subtotal);
        double total = ps.calculateTotalBeforePoints(subtotal, discount);

        priceLabel.setText("RM "+String.format("%.2f",total));
    }

    private double getMoviePrice(String movie,String hallType){
        try(BufferedReader br = new BufferedReader(new FileReader(FilePaths.TICKET_PRICE))){
            String line;
            while((line = br.readLine()) != null){
                String[] p = line.split(",");
                if(p[0].equalsIgnoreCase(movie) && p[1].equalsIgnoreCase(hallType)){
                    return Double.parseDouble(p[2]);
                }
            }
        }catch(Exception e){}
        return 0;
    }

    private void confirmBooking(){

        String name = nameField.getText().trim();
        String seat = seatField.getText();

        if(name.isEmpty() || seat.isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill all fields!");
            return;
        }

        ShowtimeMg selected = (ShowtimeMg) showtimeBox.getSelectedItem();

        if(selected == null){
            JOptionPane.showMessageDialog(this,"Showtime data error!");
            return;
        }

        try{
            double basePrice = getMoviePrice(selected.getMovieTitle(), selected.getHallType());
            int seatCount = seat.split(",").length;

            PaymentServiceCm ps = new PaymentServiceCm();

            double subtotal = ps.calculateSubtotal(basePrice,seatCount);
            double discount = ps.calculateDiscount(userTypeBox.getSelectedItem().toString(), subtotal);
            double total = ps.calculateTotalBeforePoints(subtotal, discount);

            String bookingID = generateBookingID();

            PrintWriter writer = new PrintWriter(new FileWriter(FilePaths.BOOKING,true));

            writer.println(
                    bookingID + "|" +
                    name + "|" +
                    selected.getMovieTitle() + "|" +
                    selected.getTime() + "|" +
                    selected.getHallType() + "|" +
                    selected.getHall() + "|" +
                    seat + "|" +
                    "ACTIVE" + "|" +
                    paymentBox.getSelectedItem().toString() + "|" +
                    String.format("%.2f",total)
            );

            writer.close();

            // SAVE SEAT
            SeatServiceCm seatService = new SeatServiceCm();
            seatService.recordSeat(selected.getShowtimeId(), seat);

            JOptionPane.showMessageDialog(this,"Booking Successful!\nBooking ID: " + bookingID);

            clearFields();

        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Booking Error!");
        }
    }

    private String generateBookingID(){
        File file = new File(FilePaths.BOOKING);
        String lastID = null;

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                lastID = line.split("\\|")[0];
            }
        }catch(Exception e){}

        if(lastID == null) return "B001";

        int num = Integer.parseInt(lastID.substring(1));
        return String.format("B%03d",num+1);
    }

    private void clearFields(){
        nameField.setText("");
        seatField.setText("");
    }

    
}