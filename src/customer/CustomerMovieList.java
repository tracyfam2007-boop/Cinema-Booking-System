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
import service.MovieServiceMg;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.util.List;

public class CustomerMovieList extends JFrame{
    private String customerId;
    
    public CustomerMovieList(String customerId){
        
        this.customerId = customerId;
        
        // SET TITLE
        setTitle("Movie List");
        setSize(900, 600); 
        setLocationRelativeTo(null); 
        
        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbledPane = new JTabbedPane();
        
        // NOW & UPCOMING MOVIE LIST PANEL IN MENU PANEL
        JPanel nowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        JPanel upcomingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        
        // BACK BUTTON IN MENU PANEL (GO BACK DASHBOARD)
        JButton backBtn = new JButton("< Back"); 
        backBtn.addActionListener(e -> { 
            new CustomerDashboard(this.customerId).setVisible(true); 
            dispose(); 
        }); 
        
        // BACK PANEL TO KEEP BACK BUTTON LEFT TOP
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.add(backBtn);
        mainPanel.add(backPanel, BorderLayout.NORTH);
        
        // DISPLAY MOVIE LIST
        List<MovieMg> movies = MovieServiceMg.getMovies();
        
        System.out.println("Movies loaded: " + movies.size()); // debug
        
        for (MovieMg movie : movies){
            // MOVIE POSTER
            ImageIcon icon;

            try {
                icon = new ImageIcon(movie.getImagePath());
            } catch (Exception e) {
                icon = new ImageIcon(); // fallback empty
            }
            Image img = icon.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
            
            JButton posterBtn = new JButton(new ImageIcon(img));
            posterBtn.setPreferredSize(new Dimension(150, 200));
            posterBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            // POSTER BUTTON
            posterBtn.addActionListener(e -> {
                new CustomerMovieDetails(movie, this.customerId).setVisible(true);
                dispose();
            });
            
            // IF MOVIE IS NOW AVAILABLE, DISPLAY ON NOW PANEL
            // ELSE, DISPLAY ON UPCOMING PANEL
            if (movie.getStatus().equalsIgnoreCase("Now Available")) {
                    nowPanel.add(posterBtn);
            }else{
                upcomingPanel.add(posterBtn);
            }
        }

        // TABLE FOR NOW & UPCOMING PANEL ON MENUPANEL
        tabbledPane.addTab("Now Available", new JScrollPane(nowPanel));
        tabbledPane.addTab("Coming Soon", new JScrollPane(upcomingPanel)); 
        mainPanel.add(tabbledPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }   

}
