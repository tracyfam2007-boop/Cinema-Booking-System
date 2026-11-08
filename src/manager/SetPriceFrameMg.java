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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class SetPriceFrameMg extends JFrame{
    private JTable priceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> movieCombo, hallTypeCombo;
    private JTextField txtPrice;
    private JButton btnSet, btnDelete, btnPrevious, btnDone;
    private ArrayList<MovieMg> movieArray = new ArrayList<>();
    private final String PRICE_FILE = FilePaths.TICKET_PRICE;
    private int selectedRowIndex = -1;

    public SetPriceFrameMg() {
        setTitle("Manager: Set Ticket Prices");
        setSize(950, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel lblSearch = new JLabel("Search: ");
        JTextField txtSearch = new JTextField();
        searchPanel.add(lblSearch, BorderLayout.WEST);
        searchPanel.add(txtSearch, BorderLayout.CENTER);

        // --- TOP PANEL: Input Form ---
        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Configure Pricing"));

        movieCombo = new JComboBox<>();
        hallTypeCombo = new JComboBox<>(new String[]{"IMAX", "2D", "4D"});
        txtPrice = new JTextField();
        
        btnSet = new JButton("Set/Update Price");
        btnSet.setBackground(new Color(34, 139, 34));
        btnSet.setForeground(Color.WHITE);

        btnDelete = new JButton("Delete Entry");
        btnDelete.setBackground(new Color(220, 20, 60));
        btnDelete.setForeground(Color.WHITE);

        inputPanel.add(new JLabel("Select Movie:"));
        inputPanel.add(movieCombo);
        inputPanel.add(new JLabel("Hall Type:"));
        inputPanel.add(hallTypeCombo);
        inputPanel.add(btnSet);

        inputPanel.add(new JLabel("Price (RM):"));
        inputPanel.add(txtPrice);
        inputPanel.add(new JLabel("")); 
        inputPanel.add(new JLabel("")); 
        inputPanel.add(btnDelete);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL: Table ---
        String[] columns = {"Poster", "Movie Title", "Hall Type", "Price (RM)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? ImageIcon.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        priceTable = new JTable(tableModel);
        priceTable.setRowHeight(130);
        priceTable.getTableHeader().setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setVerticalAlignment(JLabel.CENTER);

        for (int i = 1; i < priceTable.getColumnCount(); i++) {
            priceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(priceTable);
        
        // --- BOTTOM PANEL: Control Buttons ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        
        btnPrevious = new JButton("Previous");
        btnDone = new JButton("Done");
        btnDone.setBackground(new Color(34, 139, 34));
        btnDone.setForeground(Color.WHITE);
        btnDone.setPreferredSize(new Dimension(100, 35));
        btnPrevious.setPreferredSize(new Dimension(100, 35));

        bottomPanel.add(btnPrevious);
        bottomPanel.add(btnDone);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- LOAD DATA ---
        loadMoviesFromFiles();
        loadExistingPrices();
        
        // ===== SEARCH =====
        txtSearch.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e){ filterTable(txtSearch.getText()); }
            public void removeUpdate(DocumentEvent e){ filterTable(txtSearch.getText()); }
            public void changedUpdate(DocumentEvent e){ filterTable(txtSearch.getText()); }
        });

        // --- ACTIONS ---
        btnSet.addActionListener(e -> saveOrUpdatePrice());
        btnDelete.addActionListener(e -> deletePrice());
        
        btnPrevious.addActionListener(e -> {
            dispose();
            new ManagerFrame();
            // new ManagerFrame(); // Ensure ManagerFrame exists
        });

        btnDone.addActionListener(e -> {
                dispose();
                new ManagerFrame();
        });

        priceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRowIndex = priceTable.getSelectedRow();
                if (selectedRowIndex != -1) {
                    movieCombo.setSelectedItem(tableModel.getValueAt(selectedRowIndex, 1).toString());
                    hallTypeCombo.setSelectedItem(tableModel.getValueAt(selectedRowIndex, 2).toString());
                    String priceStr = tableModel.getValueAt(selectedRowIndex, 3).toString().replace("RM ", "");
                    txtPrice.setText(priceStr);
                    btnSet.setText("Update Selection");
                }
            }
        });

        setVisible(true);
    }

    // ===== SEARCH FUNCTION =====
    private void filterTable(String keyword){

        DefaultTableModel filtered = new DefaultTableModel(
            new String[]{"Poster","Movie Title","Hall Type","Price (RM)"},0){

            public Class<?> getColumnClass(int column){
                return (column==0)?ImageIcon.class:Object.class;
            }

            public boolean isCellEditable(int r,int c){ return false; }
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

        priceTable.setModel(filtered);
        applyCenterRenderer(); 
    }
    
    // ===== REQUIRED LOGIC METHODS =====

    private void loadMoviesFromFiles() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FilePaths.MOVIES))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data.length >= 6) {
                    MovieMg m = new MovieMg(data[0], data[1], data[2], data[3], data[4], data[5]);
                    movieArray.add(m);
                    movieCombo.addItem(m.getTitle());
                }else if (data.length >= 5){
                    MovieMg m = new MovieMg(data[0], data[1], data[2], data[3], data[4], "Coming Soon");
                    movieArray.add(m);
                    movieCombo.addItem(m.getTitle());
                }
            }
        } catch (IOException e) {
            System.err.println("Note: movies.txt not found.");
        }
    }

    private void saveOrUpdatePrice() {
        String movie = (String) movieCombo.getSelectedItem();
        String hall = (String) hallTypeCombo.getSelectedItem();
        String priceText = txtPrice.getText().trim();

        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a price!");
            return;
        }

        try {
            double validatedPrice = Double.parseDouble(priceText);
            String formattedPrice = String.format("%.2f", validatedPrice);
            boolean isUpdate = false;

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 1).equals(movie) && tableModel.getValueAt(i, 2).equals(hall)) {
                    tableModel.setValueAt("RM " + formattedPrice, i, 3);
                    isUpdate = true;
                    break;
                }
            }

            if (!isUpdate) {
                ImageIcon icon = null;
                for (MovieMg m : movieArray) {
                    if (m.getTitle().equals(movie)) {
                        icon = new ImageIcon(new ImageIcon(m.getImagePath()).getImage().getScaledInstance(90, 120, Image.SCALE_SMOOTH));
                        break;
                    }
                }
                tableModel.addRow(new Object[]{icon, movie, hall, "RM " + formattedPrice});
            }
            saveAllToFile();
            resetFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price format!");
        }
    }

    private void deletePrice() {
        if (selectedRowIndex != -1) {
            tableModel.removeRow(selectedRowIndex);
            saveAllToFile();
            resetFields();
        }
    }

    private void saveAllToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(PRICE_FILE))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                out.println(tableModel.getValueAt(i, 1) + "," + tableModel.getValueAt(i, 2) + "," + 
                            tableModel.getValueAt(i, 3).toString().replace("RM ", ""));
            }
        } catch (IOException e) { }
    }

    private void loadExistingPrices() {
        File file = new File(PRICE_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    ImageIcon icon = null;
                    for (MovieMg m : movieArray) {
                        if (m.getTitle().equals(data[0])) {
                            icon = new ImageIcon(new ImageIcon(m.getImagePath()).getImage().getScaledInstance(90, 120, Image.SCALE_SMOOTH));
                            break;
                        }
                    }
                    tableModel.addRow(new Object[]{icon, data[0], data[1], "RM " + data[2]});
                }
            }
        } catch (IOException e) { }
    }

    private void resetFields() {
        txtPrice.setText("");
        btnSet.setText("Set/Update Price");
        selectedRowIndex = -1;
    }
    
    private void applyCenterRenderer(){
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        center.setVerticalAlignment(JLabel.CENTER);

        for(int i=1;i<priceTable.getColumnCount();i++){
            priceTable.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }
}
