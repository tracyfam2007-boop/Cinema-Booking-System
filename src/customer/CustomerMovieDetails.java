/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customer;

/**
 *
 * @author TRACY
 */

import model.MovieMg;
import model.ShowtimeMg;
import service.MovieServiceMg;
import javax.swing.*;
import java.awt.*;
import java.awt.GridLayout;
import java.util.*;
import java.util.List;

public class CustomerMovieDetails extends JFrame{
    private final String customerId;
    
    public CustomerMovieDetails(MovieMg movie, String customerId){
            
        this.customerId = customerId;
        
        // SET TITLE
        setTitle("Movie List");
        setSize(900, 600); 
        setLocationRelativeTo(null);
       
        // MAIN PANEL (POSTER, DETAILS)
        JPanel mainPanel = new JPanel(new GridLayout(1,2,30,30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        
        // POSTER PANEL
        ImageIcon icon = new ImageIcon(movie.getImagePath());
        Image img = icon.getImage().getScaledInstance(300, 420, Image.SCALE_SMOOTH);
        JPanel posterPanel = new JPanel();
        posterPanel.add(new JLabel(new ImageIcon(img)));
        
        // DETAILS PANEL (INFO, SELECT, BUTTON PANEL)
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        
        // INFO PANEL
        JPanel infoPanel = new JPanel(new GridLayout(4,1,10,10));
        
        Font infoFont = new Font("Arial", Font.BOLD, 16);
        infoPanel.add(new JLabel("Movie: " + movie.getTitle())).setFont(infoFont);
        infoPanel.add(new JLabel("Genre: " + movie.getGenre())).setFont(infoFont);
        infoPanel.add(new JLabel("Duration: " + movie.getDuration() + " minutes")).setFont(infoFont);
        infoPanel.add(new JLabel("Status: " + movie.getStatus())).setFont(infoFont);
        
        // SELECT PANEL
        JPanel selectPanel = new JPanel (new GridLayout(2,2,10,10));
        JComboBox <String> dateSelector = new JComboBox<>();
        JComboBox <ShowtimeMg> timeSelector = new JComboBox<>();
        
        // SHOW ONLY AVAILABLE DATE (TODAY)
        List<ShowtimeMg> showtimes = MovieServiceMg.getShowtimesByMovieTitle(movie.getTitle());
        Set<String> dates = new TreeSet<>();
        
        for (ShowtimeMg s : showtimes) {
            dates.add(s.getShowdate());
        }
        
        dateSelector.setModel(new DefaultComboBoxModel<>(dates.toArray(new String[0])));

        // TIME SELECTOR (ONLY SHOW TIME)
        timeSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof ShowtimeMg) {
                    ShowtimeMg st = (ShowtimeMg) value;
                    setText(st.getTime()); // ONLY TIME
                }
                return this;
            }
        });
        
        // UPDATE TIME SELECTOR
        dateSelector.addActionListener(e -> {
            String selectedDate = (String) dateSelector.getSelectedItem();
        
            if (selectedDate != null){
                List<ShowtimeMg> times = MovieServiceMg.getShowtimesByDate(movie.getTitle(), selectedDate);
                DefaultComboBoxModel<ShowtimeMg> model = new DefaultComboBoxModel<>();
                
                for (ShowtimeMg st : times) {
                    model.addElement(st);
                }
                timeSelector.setModel(model);
            }
        });
        
        // trigger first load
        if (dateSelector.getItemCount() > 0) {
            dateSelector.setSelectedIndex(0);
        }
        
        selectPanel.add(new JLabel("Select Show Date:")); 
        selectPanel.add(dateSelector);
        selectPanel.add(new JLabel("Select Show Time:")); 
        selectPanel.add(timeSelector);
        
        
        // BUTTON PANEL
        JPanel buttonPanel = new JPanel (new FlowLayout(FlowLayout.LEFT,10,10));
        JButton seatBtn = new JButton("Choose Seats");
        seatBtn.addActionListener(e -> { 
            
            ShowtimeMg selected = (ShowtimeMg) timeSelector.getSelectedItem();
            if (selected != null) {
                new CustomerSeatSelectionFrame(customerId, selected).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a showtime.");
            }
        });
        
        if (!movie.getStatus().equalsIgnoreCase("Now Available"))  {
            seatBtn.setEnabled(false);
        }
        
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new CustomerMovieList(this.customerId).setVisible(true);
            dispose();
        });
        
        buttonPanel.add(backBtn);
        buttonPanel.add(seatBtn);
        
        // ADD INFO, SELECT, BUTTON PANEL INTO RIGHT PANEL
        detailsPanel.add(infoPanel);
        detailsPanel.add(Box.createVerticalStrut(20));
        detailsPanel.add(selectPanel);
        detailsPanel.add(Box.createVerticalStrut(20));
        detailsPanel.add(buttonPanel);
        
        //ADD POSTER, RIGHT PANEL INTO DETAIL PANEL)
        mainPanel.add(posterPanel);
        mainPanel.add(detailsPanel);
        
        add(mainPanel);
        
    }

}
