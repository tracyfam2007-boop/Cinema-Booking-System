/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manager;

/**
 *
 * @author TRACY
 */

import utils.FilePaths;
import model.MovieMg;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagerReportFrame extends JFrame{
    private JTable reportTable; 
    private DefaultTableModel tableModel;
    private JLabel lblTotalRevenue, lblTotalBookings;
    private ArrayList<MovieMg> movieArray = new ArrayList<>();
    private final String BOOKING_FILE = FilePaths.BOOKING;
    private final String MOVIE_FILE = FilePaths.MOVIES;

    public ManagerReportFrame() {

        setTitle("Manager: Booking & Revenue Report");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // ===== SEARCH =====
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel lblSearch = new JLabel("Search: ");
        JTextField txtSearch = new JTextField();

        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        // ===== Top panel : Summary Cards =====
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblTotalRevenue = new JLabel("Total Revenue: RM 0.00", JLabel.CENTER);
        lblTotalRevenue.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalRevenue.setOpaque(true);
        lblTotalRevenue.setBackground(new Color(34, 139, 34));
        lblTotalRevenue.setForeground(Color.WHITE);
        lblTotalRevenue.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        lblTotalBookings = new JLabel("Total Bookings: 0", JLabel.CENTER);
        lblTotalBookings.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalBookings.setOpaque(true);
        lblTotalBookings.setBackground(new Color(70, 130, 180));
        lblTotalBookings.setForeground(Color.WHITE);
        lblTotalBookings.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        summaryPanel.add(lblTotalRevenue);
        summaryPanel.add(lblTotalBookings);
        
        // Combine
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(summaryPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table with Poster Support
        String[] columns = {"Poster", "Movie Title", "Tickets Sold", "Revenue (RM)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? ImageIcon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(120); // Height to fit the poster

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setVerticalAlignment(JLabel.CENTER);

        // Apply centering to text columns
        for (int i = 1; i < reportTable.getColumnCount(); i++) {
            reportTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // ===== SEARCH FUNCTION =====
        txtSearch.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e){ filter(txtSearch.getText()); }
            public void removeUpdate(DocumentEvent e){ filter(txtSearch.getText()); }
            public void changedUpdate(DocumentEvent e){ filter(txtSearch.getText()); }
        });
        
        // --- BOTTOM PANEL: Navigation ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton btnPrevious = new JButton("Previous");
        JButton btnDone = new JButton("Done");

        btnDone.setBackground(new Color(34, 139, 34));
        btnDone.setForeground(Color.WHITE);
        btnDone.setPreferredSize(new Dimension(120, 35));
        btnPrevious.setPreferredSize(new Dimension(120, 35));

        btnPrevious.addActionListener(e -> { dispose(); new ManagerFrame(); });
        btnDone.addActionListener(e -> { dispose(); new ManagerFrame(); });

        bottomPanel.add(btnPrevious);
        bottomPanel.add(btnDone);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load data and show report
        loadMovies();
        generateReport();

        setVisible(true);
    }

    private void filter(String keyword){

        DefaultTableModel filtered = new DefaultTableModel(
            new String[]{"Poster","Movie Title","Tickets Sold","Revenue (RM)"},0){

            @Override
            public Class<?> getColumnClass(int column){
                return (column==0)?ImageIcon.class:Object.class;
            }

            @Override
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };

        for(int i=0;i<tableModel.getRowCount();i++){
            String title = tableModel.getValueAt(i,1).toString().toLowerCase();

            if(title.contains(keyword.toLowerCase())){
                filtered.addRow(new Object[]{
                    tableModel.getValueAt(i,0),
                    tableModel.getValueAt(i,1),
                    tableModel.getValueAt(i,2),
                    tableModel.getValueAt(i,3)
                });
            }
        }

        reportTable.setModel(filtered);
        applyCenterRenderer();
    }
    
    private void loadMovies() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FilePaths.MOVIES))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    movieArray.add(new MovieMg(data[0], data[1], data[2], data[3], data[4], data[5]));
                }else if (data.length >= 5){
                    movieArray.add(new MovieMg(data[0], data[1], data[2], data[3], data[4], "Coming Soon"));
                }
            }
        } catch (IOException e) {
            System.err.println("Movies file not found.");
        }
    }

    private void generateReport() {

    HashMap<String, Integer> movieTickets = new HashMap<>();
    HashMap<String, Double> movieRevenue = new HashMap<>();

    double grandTotalRevenue = 0;
    int grandTotalTickets = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(FilePaths.BOOKING))) {

        String line;

        while ((line = reader.readLine()) != null) {

            String[] data = line.split("\\|");

            if (data.length < 10) continue;
            
            if(!data[7].equalsIgnoreCase("ACTIVE")) continue;

            String title = data[2]; // movie

            double price;

            try {
                price = Double.parseDouble(data[9]);
            } catch (Exception e) {
                continue;
            }

            // COUNT SEATS
            int seatCount = data[6].split(",").length;
            
            movieTickets.put(title, movieTickets.getOrDefault(title, 0) + 1);
            movieRevenue.put(title, movieRevenue.getOrDefault(title, 0.0) + price);

            grandTotalRevenue += price;
            grandTotalTickets++;
            
        }

        // ===== DISPLAY TABLE =====
        for (Map.Entry<String, Integer> entry : movieTickets.entrySet()) {

            String title = entry.getKey();
            int tickets = entry.getValue();
            double revenue = movieRevenue.get(title);

            ImageIcon poster = null;

            for (MovieMg m : movieArray) {
                if (m.getTitle().equalsIgnoreCase(title)) {

                    String path = m.getImagePath();
                    
                    if(path != null && !path.trim().isEmpty()){
                        
                        File imgFile = new File(path);
                        
                        if(imgFile.exists()){
                            
                            ImageIcon icon = new ImageIcon(path);
                            Image img = icon.getImage().getScaledInstance(80, 110, Image.SCALE_SMOOTH);
                            poster = new ImageIcon(img);
                        }else{
                            System.out.println("Image now found: " + path);
                        }
                    }
                    break;
                }
            }

            tableModel.addRow(new Object[]{
                    poster,
                    title,
                    tickets,
                    String.format("%.2f", revenue)
            });
        }

        lblTotalRevenue.setText("Total Revenue: RM " + String.format("%.2f", grandTotalRevenue));
        lblTotalBookings.setText("Total Bookings: " + grandTotalTickets);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error reading booking file!");
        }
    }
    
    private void applyCenterRenderer(){
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        center.setVerticalAlignment(JLabel.CENTER);

        for(int i=1;i<reportTable.getColumnCount();i++){
            reportTable.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }
}
