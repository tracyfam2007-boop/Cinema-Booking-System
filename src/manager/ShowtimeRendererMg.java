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
import model.ShowtimeMg;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class ShowtimeRendererMg extends JPanel implements ListCellRenderer<ShowtimeMg>{
    
    private JLabel lblPoster = new JLabel();
    private JLabel lblTitle = new JLabel();
    private JLabel lblDetails = new JLabel();
    
    public ShowtimeRendererMg(){
        
        setLayout(new BorderLayout(10, 5));

        lblPoster.setPreferredSize(new Dimension(60, 80));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblDetails.setFont(new Font("Arial", Font.PLAIN, 12));

        textPanel.add(lblTitle);
        textPanel.add(lblDetails);

        add(lblPoster, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);

        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends ShowtimeMg> list,
            ShowtimeMg value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        // Movie title
        lblTitle.setText(value.getMovieTitle());
        
        // Show hall type + hall + time
        lblDetails.setText(
                value.getHallType() + " | Hall " +
                value.getHall() + " | Time: " +
                value.getTime()
        );

        // SHOW IMAGE
        lblPoster.setIcon(getMovieImage(value.getMovieTitle()));

        if (isSelected) {
            setBackground(new Color(200, 220, 255));
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }
    
    // METHOD TO LOAD IMAGE FROM movies.txt
    private ImageIcon getMovieImage(String movieTitle) {

        try (BufferedReader br = new BufferedReader(
                new FileReader(FilePaths.MOVIES))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                // data[1] = movie title
                // data[4] = image path
                if (data.length >= 5 && data[1].equals(movieTitle)) {

                    String path = data[4];

                    if (path != null && !path.isEmpty()) {

                        Image img = new ImageIcon(path)
                                .getImage()
                                .getScaledInstance(60, 80, Image.SCALE_SMOOTH);

                        return new ImageIcon(img);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
