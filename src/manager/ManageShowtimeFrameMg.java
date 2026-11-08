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
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class ManageShowtimeFrameMg extends JFrame{
    private JComboBox<String> movieCombo;
    private JComboBox<String> hallTypeCombo;
    private JComboBox<String> hallNumberCombo;
    
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JComboBox<String> ampmBox;
    
    private DefaultListModel<ShowtimeMg> showtimeListModel;
    private JList<ShowtimeMg> showtimeList;
    
    private ArrayList<ShowtimeMg> showtimeArray = new ArrayList<>();
    
    public ManageShowtimeFrameMg() {
        
        setTitle("Manage Showtimes");
        setSize(850, 600); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // ===== Main Left Container =====
        JPanel leftContainer = new JPanel(new BorderLayout());
        leftContainer.setPreferredSize(new Dimension(300, 0));
        
        // --- Top: Input Form ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(13, 1, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Showtime Details"));
        
        movieCombo = new JComboBox<>();
        hallTypeCombo = new JComboBox<>(new String[]{"IMAX", "2D", "4D"});
        hallNumberCombo = new JComboBox<>();
        
        hourSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        ampmBox = new JComboBox<>(new String[]{"AM", "PM"});
        
        JPanel timePanel = new JPanel();
        timePanel.add(hourSpinner);
        timePanel.add(new JLabel(":"));
        timePanel.add(minuteSpinner);
        timePanel.add(ampmBox);
        
        JButton btnAdd = new JButton("Add Showtime");
        JButton btnUpdate = new JButton("Update Showtime");
        JButton btnDelete = new JButton("Delete Showtime");
        
        formPanel.add(new JLabel("Movie:"));
        formPanel.add(movieCombo);
        formPanel.add(new JLabel("Hall Type:"));
        formPanel.add(hallTypeCombo);
        formPanel.add(new JLabel("Hall Number:"));
        formPanel.add(hallNumberCombo);
        formPanel.add(new JLabel("Time:"));
        formPanel.add(timePanel);
        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);
        
        // --- Bottom: Navigation ---
        JPanel navPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10)); 
        
        JButton btnPrevious = new JButton("Previous");
        JButton btnDone = new JButton("Done");
        
        btnDone.setBackground(new Color(34, 139, 34));
        btnDone.setForeground(Color.WHITE);
        
        navPanel.add(btnPrevious);
        navPanel.add(btnDone);
        
        leftContainer.add(formPanel, BorderLayout.CENTER);
        leftContainer.add(navPanel, BorderLayout.SOUTH);
        
        // ===== Right Panel (Showtime List) =====
        showtimeListModel = new DefaultListModel<>();
        showtimeList = new JList<>(showtimeListModel);
        showtimeList.setCellRenderer(new ShowtimeRendererMg());
        showtimeList.setFixedCellHeight(85);

        JScrollPane scrollPane = new JScrollPane(showtimeList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Showtime List"));

        add(leftContainer, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        
        // ===== Initialization =====
        loadMovies();
        updateHallNumbers();
        loadShowtimes();
        
        // ===== Actions =====
        btnPrevious.addActionListener(e -> {
            dispose();
            new ManagerFrame().setVisible(true);
        });

        btnDone.addActionListener(e -> {
            saveShowtimes();
            dispose(); 
            new ManagerFrame().setVisible(true); 
        });
        
        hallTypeCombo.addActionListener(e -> updateHallNumbers());
        btnAdd.addActionListener(e -> addShowtime());
        btnUpdate.addActionListener(e -> updateShowtime());
        btnDelete.addActionListener(e -> deleteShowtime());
        
        showtimeList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                int index = showtimeList.getSelectedIndex();
                if(index >= 0){
                    ShowtimeMg s = showtimeArray.get(index);
                    movieCombo.setSelectedItem(s.getMovieTitle());
                    hallTypeCombo.setSelectedItem(s.getHallType());
                    hallNumberCombo.setSelectedItem(s.getHall());
                    
                    String[] timeParts = s.getTime().split(" ");
                    String[] hm = timeParts[0].split(":");
                    hourSpinner.setValue(Integer.parseInt(hm[0]));
                    minuteSpinner.setValue(Integer.parseInt(hm[1]));
                    ampmBox.setSelectedItem(timeParts[1]);
                }
            }
        });
        
        setVisible(true);
    }
    
    // GET MOVIE STATUS
    private String getMovieStatus(String movie){
        try(BufferedReader reader = new BufferedReader(new FileReader(FilePaths.MOVIES))){
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data[1].equals(movie)){
                    return (data.length >= 6) ? data[5] : "Coming Soon";
                }
            }
        }catch(Exception e){}
        return "Coming Soon";
    }

    // GENERATE SHOWTIME ID
    private String generateShowtimeID(){
        int max = 0;
        for(ShowtimeMg s : showtimeArray){
            String num = s.getShowtimeId().substring(1);
            int n = Integer.parseInt(num);
            if(n > max) max = n;
        }
        return String.format("S%03d", max + 1);
    }
    
    private void updateHallNumbers(){
        hallNumberCombo.removeAllItems();
        String type = (String) hallTypeCombo.getSelectedItem();
        if(type.equals("IMAX")){
            hallNumberCombo.addItem("1"); 
            hallNumberCombo.addItem("2"); 
            hallNumberCombo.addItem("3");
        } else if(type.equals("2D")){
            hallNumberCombo.addItem("4"); 
            hallNumberCombo.addItem("5"); 
            hallNumberCombo.addItem("6");
        } else {
            hallNumberCombo.addItem("7"); 
            hallNumberCombo.addItem("8"); 
            hallNumberCombo.addItem("9"); 
            hallNumberCombo.addItem("10");
        }
    }

    private void loadMovies(){
        try(BufferedReader reader = new BufferedReader(new FileReader(FilePaths.MOVIES))){
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data.length >= 2) movieCombo.addItem(data[1]);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error loading movies");
        }
    }

    private String getTime(){
        return String.format("%d:%02d %s", (int)hourSpinner.getValue(), (int)minuteSpinner.getValue(), (String)ampmBox.getSelectedItem());
    }
    
    // CONVERT TIME TO MINUTES
    private int convertToMinutes(String time){
        String[] parts = time.split(" ");
        String[] hm = parts[0].split(":");

        int hour = Integer.parseInt(hm[0]);
        int minute = Integer.parseInt(hm[1]);

        if(parts[1].equals("PM") && hour != 12) hour += 12;
        if(parts[1].equals("AM") && hour == 12) hour = 0;

        return hour * 60 + minute;
    }
    
    // GET MOVIE DURATION
    private int getMovieDuration(String movie){
        try(BufferedReader reader = new BufferedReader(new FileReader(FilePaths.MOVIES))){
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data[1].equals(movie)){
                    return Integer.parseInt(data[3]);   // duration column
                }
            }
        }catch (Exception e){
            
        }
        return 120;
    }

    private void addShowtime(){
        String movie = (String) movieCombo.getSelectedItem();
        
        if(getMovieStatus(movie).equals("Coming Soon")){
            JOptionPane.showMessageDialog(this, "Cannot add showtime for Coming Soon movie!");
            return;
        }
        
        String type = (String) hallTypeCombo.getSelectedItem();
        String hall = (String) hallNumberCombo.getSelectedItem();
        String time = getTime();
        
        int newStart = convertToMinutes(time);
        int newEnd = newStart + getMovieDuration(movie);
        
        for(ShowtimeMg s : showtimeArray){
            if(s.getHall().equals(hall)){
                int oldStart = convertToMinutes(s.getTime());
                int oldEnd = oldStart + getMovieDuration(s.getMovieTitle());

                if(newStart < oldEnd && oldStart < newEnd){
                    JOptionPane.showMessageDialog(this, "Time clash in same hall!");
                    return;
                }
            }
        }
        
        String id = generateShowtimeID();
        String showdate = java.time.LocalDate.now().toString();
        ShowtimeMg s = new ShowtimeMg(id, movie, showdate, time, type, hall);
        
        showtimeArray.add(s);
        showtimeListModel.addElement(s);
        saveShowtimes();
    }

    private void updateShowtime(){
        int index = showtimeList.getSelectedIndex();
        if(index < 0) return;
        
        String movie = (String) movieCombo.getSelectedItem();
        
        if(getMovieStatus(movie).equals("Coming Soon")){
            JOptionPane.showMessageDialog(this, "Cannot update to Coming Soon movie!");
            return;
        }
        
        String type = (String) hallTypeCombo.getSelectedItem();
        String hall = (String) hallNumberCombo.getSelectedItem();
        String time = getTime();
        
        int newStart = convertToMinutes(time);
        int newEnd = newStart + getMovieDuration(movie);
        
        for(int i=0; i<showtimeArray.size(); i++){
            if(i == index) continue;
            
            ShowtimeMg s = showtimeArray.get(i);
            
            if(s.getHall().equals(hall)){
                int oldStart = convertToMinutes(s.getTime());
                int oldEnd = oldStart + getMovieDuration(s.getMovieTitle());
                
                if(newStart < oldStart  && oldStart < newEnd){
                    JOptionPane.showMessageDialog(this, "Time clash in same hall");
                    return;
                }
            }
        }

        String id = showtimeArray.get(index).getShowtimeId();
        String showdate = java.time.LocalDate.now().toString();
        ShowtimeMg updated = new ShowtimeMg(id, movie, showdate, time, type, hall);
        
        showtimeArray.set(index, updated);
        showtimeListModel.set(index, updated);
        saveShowtimes();
    }

    private void deleteShowtime(){
        int index = showtimeList.getSelectedIndex();
        if(index < 0) return;
        showtimeArray.remove(index);
        showtimeListModel.remove(index);
        saveShowtimes();
    }

    private void saveShowtimes(){
        try(PrintWriter writer = new PrintWriter(new FileWriter(FilePaths.SHOWTIMES))){
            for(ShowtimeMg s : showtimeArray){
                writer.println(s.toFileString());
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error saving showtimes");
        }
    }

    private void loadShowtimes(){
        File file = new File(FilePaths.SHOWTIMES);
        if(!file.exists()) return;
        showtimeArray.clear();
        showtimeListModel.clear();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if(data.length >= 6){
                    // Format: movie, time, type, hall
                    ShowtimeMg s = new ShowtimeMg(data[0], data[1], data[2], data[3], data[4], data[5]);
                    showtimeArray.add(s);
                    showtimeListModel.addElement(s);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
