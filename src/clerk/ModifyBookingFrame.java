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
import service.SeatServiceCm;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ModifyBookingFrame extends JFrame{
    
    private JTextField bookingField;
    private JTextField movieField;
    private JTextField showtimeField;
    private JTextField hallTypeField;
    private JTextField seatField;

    private JPanel seatPanel;

    private String currentHallType = "";
    private String currentShowtime = "";
    private String currentBookingID = "";
    
    private Set<String> selectedSeats = new HashSet<>();
    private int requiredSeatCount = 0;
    private String originalSeats = "";

    public ModifyBookingFrame() {

        setTitle("Modify Booking");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel leftPanel = new JPanel(new GridLayout(8, 1, 15, 15));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Modify Booking", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        bookingField = createField("Booking ID");
        movieField = createField("Movie");
        showtimeField = createField("Showtime");
        hallTypeField = createField("Hall Type");
        seatField = createField("New Seat");

        movieField.setEditable(false);
        showtimeField.setEditable(false);
        hallTypeField.setEditable(false);

        JButton loadBtn = new JButton("Load Booking");
        JButton modifyBtn = new JButton("Confirm Modify");
        JButton backBtn = new JButton("Back");

        loadBtn.addActionListener(e -> loadBookingDetails());
        modifyBtn.addActionListener(e -> modifyBooking());

        backBtn.addActionListener(e -> {
            new CancelModifyMenu().setVisible(true);
            dispose();
        });

        leftPanel.add(bookingField);
        leftPanel.add(loadBtn);
        leftPanel.add(movieField);
        leftPanel.add(showtimeField);
        leftPanel.add(hallTypeField);
        leftPanel.add(seatField);
        leftPanel.add(modifyBtn);
        leftPanel.add(backBtn);

        add(leftPanel, BorderLayout.WEST);

        seatPanel = new JPanel();
        add(seatPanel, BorderLayout.CENTER);
    }

    // LOAD BOOKING
    private void loadBookingDetails(){

        String id = bookingField.getText().trim();

        try(BufferedReader br = new BufferedReader(new FileReader(FilePaths.BOOKING))){

            String line;

            while((line = br.readLine()) != null){

                String[] data = line.split("\\|");

                if(data.length >= 10 && data[0].equals(id)){

                    if(data[7].equals("CANCELED")){
                        JOptionPane.showMessageDialog(this,"Cannot modify canceled booking!");
                        return;
                    }

                    movieField.setText(data[2]);
                    showtimeField.setText(data[3]);
                    hallTypeField.setText(data[4]);

                    currentHallType = data[4];
                    currentShowtime = data[3];
                    currentBookingID = id;

                    // 🔥 store original seat
                    originalSeats = data[6];
                    requiredSeatCount = originalSeats.split(",").length;

                    selectedSeats.clear();
                    seatField.setText("");

                    generateSeatLayout(data[4]);

                    return;
                }
            }

            JOptionPane.showMessageDialog(this,"Booking not found");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // SEAT LAYOUT
    private void generateSeatLayout(String hallType){

        int rows,cols;

        switch(hallType){
            case "IMAX": rows=10; cols=9; break;
            case "4D": rows=6; cols=8; break;
            default: rows=8; cols=8;
        }

        seatPanel.removeAll();
        seatPanel.setLayout(new GridLayout(rows+1,cols+1,5,5));

        Set<String> bookedSeats = getBookedSeats();

        for(int r=0;r<=rows;r++){
            for(int c=0;c<=cols;c++){

                if(r==0 && c==0){
                    seatPanel.add(new JLabel(""));
                }
                else if(r==0){
                    JLabel colLabel = new JLabel(String.valueOf(c), JLabel.CENTER);
                    colLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    seatPanel.add(colLabel);
                }
                else if(c==0){
                    JLabel rowLabel = new JLabel(String.valueOf((char)('A'+r-1)), JLabel.CENTER);
                    rowLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    seatPanel.add(rowLabel);
                }
                else{

                    String seatID = (char)('A'+r-1)+""+c;
                    JButton btn = new JButton(seatID);
                    btn.setFont(new Font("Arial", Font.BOLD, 12));

                    if(bookedSeats.contains(seatID)){
                        btn.setEnabled(false);
                        btn.setBackground(Color.RED);
                    }else{

                        btn.addActionListener(e -> {

                            if(selectedSeats.contains(seatID)){
                                selectedSeats.remove(seatID);
                                btn.setBackground(null);
                            }else{

                                if(selectedSeats.size() >= requiredSeatCount){
                                    JOptionPane.showMessageDialog(this,
                                            "You must select exactly " + requiredSeatCount + " seats");
                                    return;
                                }

                                selectedSeats.add(seatID);
                                btn.setBackground(Color.GREEN);
                            }

                            seatField.setText(String.join(",", selectedSeats));
                        });
                    }

                    seatPanel.add(btn);
                }
            }
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    // GET BOOKED SEATS
    private Set<String> getBookedSeats(){

        Set<String> seats = new HashSet<>();

        try(BufferedReader br = new BufferedReader(new FileReader(FilePaths.BOOKING))){

            String line;

            while((line = br.readLine()) != null){

                String[] p = line.split("\\|");

                if(p.length >= 10 &&
                   p[3].equals(currentShowtime) &&
                   p[7].equals("ACTIVE") &&
                   !p[0].equals(currentBookingID)){

                    for(String s : p[6].split(",")){
                        seats.add(s);
                    }
                }
            }

        }catch(Exception e){}

        return seats;
    }

    // ✅ MODIFY BOOKING + UPDATE SEAT FILE
    private void modifyBooking(){

        String id = bookingField.getText().trim();

        if(selectedSeats.size() != requiredSeatCount){
            JOptionPane.showMessageDialog(this,
                    "You must select exactly " + requiredSeatCount + " seats");
            return;
        }

        String newSeat = String.join(",", selectedSeats);

        File inputFile = new File(FilePaths.BOOKING);
        File tempFile = new File("data/temp.txt");

        boolean found = false;

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile))){

            String line;

            while((line = reader.readLine()) != null){

                String[] parts = line.split("\\|");

                if(parts.length >= 10 && parts[0].equals(id)){

                    found = true;

                    // UPDATE BOOKING
                    parts[6] = newSeat;

                    writer.println(String.join("|", parts));

                } else {
                    writer.println(line);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error modifying booking");
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        // UPDATE BOOKED SEAT FILE
        SeatServiceCm seatService = new SeatServiceCm();

        // remove old seats
        String showtimeId = getShowtimeId(movieField.getText(), currentShowtime);
        seatService.removeSeat(showtimeId, originalSeats);

        // add new seats
        seatService.recordSeat(showtimeId, newSeat);

        if(found){
            JOptionPane.showMessageDialog(this,"Booking Updated!");
        }else{
            JOptionPane.showMessageDialog(this,"Booking not found");
        }
    }

    // helper method
    private String getShowtimeId(String movie, String time){

        try(BufferedReader br = new BufferedReader(new FileReader(FilePaths.SHOWTIMES))){

            String line;

            while((line = br.readLine()) != null){

                String[] data = line.split(",");

                if(data.length >= 6 &&
                   data[1].equalsIgnoreCase(movie) &&
                   data[3].equalsIgnoreCase(time)){

                    return data[0];
                }
            }

        }catch(Exception e){}

        return null;
    }

    private JTextField createField(String title){
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        return field;
    }

}