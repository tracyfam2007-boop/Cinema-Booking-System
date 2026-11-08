/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clerk;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ScreenDetailsFrame extends JFrame{
    //Inheritance
    private JTable table;
    private DefaultTableModel model;
    private JPanel seatPanel;
    private JLabel infoLabel;

    public ScreenDetailsFrame(){

        setTitle("Screen Details & Seating");
        setSize(750,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // ===== TABLE =====
        model = new DefaultTableModel();
        table = new JTable(model);

        model.addColumn("Movie");
        model.addColumn("Date");
        model.addColumn("Time");
        model.addColumn("Hall");
        model.addColumn("Type");

        loadShowtimes();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 180));
        scrollPane.setMinimumSize(new Dimension(750, 100));

        seatPanel = new JPanel(new GridLayout(10,9,5,5));
        seatPanel.setBorder(BorderFactory.createTitledBorder("Seat Layout"));
        seatPanel.setBackground(Color.WHITE);
        seatPanel.setPreferredSize(new Dimension(750, 400));

        //Composition
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            scrollPane,
            seatPanel
        );
        splitPane.setResizeWeight(0.0);
        splitPane.setDividerSize(8);
        add(splitPane, BorderLayout.CENTER);

        // ===== INFO =====
        infoLabel = new JLabel("Select a showtime above", JLabel.CENTER);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new ClerkFrame().setVisible(true);
            dispose();
        });

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(infoLabel, BorderLayout.NORTH);
        bottom.add(backBtn, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        // ===== CLICK EVENT =====
        table.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                updateSeats();
            }
        });

        setVisible(true);
    }

    private void loadShowtimes(){

        try(BufferedReader br =
            new BufferedReader(new FileReader("data/showtimes.txt"))){

            String line;

            while((line = br.readLine()) != null){

                String[] data = line.split(",");

                if(data.length >= 6){

                    model.addRow(new Object[]{
                        data[1], // Movie
                        data[2], // Date
                        data[3], // Time
                        data[5], // Hall
                        data[4]  // Type
                    });
                }
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Error loading showtimes!");
        }
    }

    private void updateSeats(){

        int rowIndex = table.getSelectedRow();
        if(rowIndex == -1) return;

        String movie = model.getValueAt(rowIndex,0).toString();
        String time = model.getValueAt(rowIndex,2).toString();

        Set<String> bookedSeats = new HashSet<>();

        try(BufferedReader br =
            new BufferedReader(new FileReader("data/booking.txt"))){

            String line;

            while((line = br.readLine()) != null){

                String[] data = line.split("\\|");

                if(data.length >= 7 &&
                    data[2].equalsIgnoreCase(movie) &&
                    data[3].equalsIgnoreCase(time)){

                    String[] seats = data[6].split(",");

                    for(String s : seats){
                        bookedSeats.add(s.trim());
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        seatPanel.removeAll();

        char row = 'A';
        int totalSeats = 90;

        for(int i=0;i<10;i++){

            for(int j=1;j<=9;j++){

                String seatID = row + String.valueOf(j);

                JLabel seat = new JLabel(seatID, JLabel.CENTER);
                seat.setOpaque(true);
                seat.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                if(bookedSeats.contains(seatID)){

                    seat.setBackground(Color.RED);
                    seat.setForeground(Color.WHITE);

                }else{

                    seat.setBackground(Color.GREEN);
                    seat.setForeground(Color.BLACK);
                }

                seatPanel.add(seat);
            }

            row++;
        }

        int available = totalSeats - bookedSeats.size();

        infoLabel.setText(
            "Movie: " + movie +
            " | Time: " + time +
            " | Booked: " + bookedSeats.size() +
            " | Available Seats: " + available 
        );

        seatPanel.revalidate();
        seatPanel.repaint();
    }

}
