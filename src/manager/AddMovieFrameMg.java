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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class AddMovieFrameMg extends JFrame{
    
    private JTextField txtTitle, txtGenre, txtDuration;
    private JLabel imageLabel;
    private String selectedImagePath = "";
    
    private JComboBox<String>statusBox;

    private DefaultListModel<MovieMg> movieListModel;
    private JList<MovieMg> movieList;

    private ArrayList<MovieMg> movieArray = new ArrayList<>();

    public AddMovieFrameMg() {

        setTitle("Manage Movies");
        setSize(850, 650); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // close this frame only, do not exit the system

        // ===== LEFT PANEL (FORM) =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(15, 1, 5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Movie Details"));
        
        txtTitle = new JTextField();
        txtGenre = new JTextField();
        txtDuration = new JTextField();
        
        statusBox = new JComboBox<>(new String[]{"Now Available", "Coming Soon"});

        JButton btnChooseImage = new JButton("Choose Image");
        imageLabel = new JLabel("No Image Selected");

        JButton btnAdd = new JButton("Add Movie");
        JButton btnUpdate = new JButton("Update Movie");
        JButton btnDelete = new JButton("Delete Movie");
        
        JButton btnPrevious = new JButton("Previous");
        JButton btnDone = new JButton("Done");
        
        // Style Done button to stand out
        btnDone.setBackground(new Color(34, 139, 34));
        btnDone.setForeground(Color.WHITE);

        leftPanel.add(new JLabel("Title:"));
        leftPanel.add(txtTitle);
        leftPanel.add(new JLabel("Genre:"));
        leftPanel.add(txtGenre);
        leftPanel.add(new JLabel("Duration:"));
        leftPanel.add(txtDuration);
        leftPanel.add(new JLabel("Status:"));
        leftPanel.add(statusBox);
        leftPanel.add(btnChooseImage);
        leftPanel.add(imageLabel);
        leftPanel.add(btnAdd);
        leftPanel.add(btnUpdate);
        leftPanel.add(btnDelete);
        leftPanel.add(btnPrevious); 
        leftPanel.add(btnDone);  
        
        // ===== RIGHT PANEL (MOVIE LIST) =====
        movieListModel = new DefaultListModel<>();
        movieList = new JList<>(movieListModel);
        movieList.setCellRenderer(new MovieRenderer());
        movieList.setFixedCellHeight(110);
        JScrollPane scrollPane = new JScrollPane(movieList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Movie List"));
        
        // ===== TOP PANEL (SEARCH) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblSearch = new JLabel("Search: ");
        JTextField txtSearch = new JTextField();
        topPanel.add(lblSearch, BorderLayout.WEST);
        topPanel.add(txtSearch, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        loadMoviesFromFile();
        
        // ===== NAVIGATION BUTTON ACTIONS =====
        // Search Logic
        txtSearch.getDocument().addDocumentListener(new DocumentListener(){

            public void insertUpdate(DocumentEvent e) {filterMovies(txtSearch.getText());}
            public void removeUpdate(DocumentEvent e) {filterMovies(txtSearch.getText());}
            public void changedUpdate(DocumentEvent e){filterMovies(txtSearch.getText());}
        });
        
        // Previous Button: Just goes back
        btnPrevious.addActionListener(e -> {
            dispose(); // Close this frame
            new ManagerFrame(); // Return to dashboard
        });

        // Done Button: Saves (optional extra save) and goes back
        btnDone.addActionListener(e -> {
            saveMoviesToFile(); // Ensure everything is saved
            dispose(); 
            new ManagerFrame(); 
        });

        // ===== EXISTING BUTTON ACTIONS =====
        btnChooseImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                selectedImagePath = file.getAbsolutePath();
                imageLabel.setText(file.getName());
            }
        });
        
        btnAdd.addActionListener(e -> addMovie());
        btnUpdate.addActionListener(e -> updateMovie());
        btnDelete.addActionListener(e -> deleteMovie());

        movieList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = movieList.getSelectedIndex();
                if (index != -1) {
                    MovieMg m = movieListModel.getElementAt(index);
                    txtTitle.setText(m.getTitle());
                    txtGenre.setText(m.getGenre());
                    txtDuration.setText(m.getDuration());
                    selectedImagePath = m.getImagePath();
                    imageLabel.setText(selectedImagePath.isEmpty() ? "No Image" : new File(selectedImagePath).getName());
                    statusBox.setSelectedItem(m.getStatus());
                }
            }
        });
        
        setVisible(true);
        
    }
    
    // Search function
    private void filterMovies(String keyword){
        
        movieListModel.clear();
        
        for(MovieMg m : movieArray){
            
            if(m.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
               m.getGenre().toLowerCase().contains(keyword.toLowerCase())){
                
                movieListModel.addElement(m);
            }
        }
    }
    
    
    private void addMovie() {
        
        String title = txtTitle.getText().trim();
        String genre = txtGenre.getText().trim();
        String durationText = txtDuration.getText().trim();
        
        if (title.isEmpty() || genre.isEmpty() || durationText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- DURATION VALIDATION START ---
        try {
            int duration = Integer.parseInt(durationText);
            if (duration <= 0) {
                JOptionPane.showMessageDialog(this, "Duration must be greater than 0!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Duration! Please enter numbers only (e.g., 120).", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // --- DURATION VALIDATION END ---

        String id = "M" + (movieArray.size() + 1);
        String status = statusBox.getSelectedItem().toString();
        
        MovieMg m = new MovieMg(id, title, genre, durationText, selectedImagePath, status);
        movieArray.add(m);
        
        saveMoviesToFile();
        refreshList();
        clearFields();
        JOptionPane.showMessageDialog(this, "Movie added successfully!");
    }

    private void updateMovie() {
        int index = movieList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Select a movie to update.");
            return;
        }

        String durationText = txtDuration.getText().trim();
        // Validate duration before updating
        try {
            if (Integer.parseInt(durationText) <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Duration! Numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String status = statusBox.getSelectedItem().toString();
        
        MovieMg m = new MovieMg(movieArray.get(index).getMovieId(), txtTitle.getText(), txtGenre.getText(), durationText, selectedImagePath, status);
        
        movieArray.set(index, m);
        saveMoviesToFile();
        refreshList();
        clearFields();
        JOptionPane.showMessageDialog(this, "Movie updated!");
    }

    private void deleteMovie() {
        int index = movieList.getSelectedIndex();
        
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Select a movie to delete.");
            return;
        }
        
        if (index != -1) {
            movieArray.remove(index);
            saveMoviesToFile();
            refreshList();
            clearFields();
            JOptionPane.showMessageDialog(this, "Movie deleted.");
        }
    }

    private void saveMoviesToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FilePaths.MOVIES))) {
            for (MovieMg m : movieArray) {
                pw.println(m.getMovieId() + "," + m.getTitle() + "," + m.getGenre() + "," + m.getDuration() + "," + m.getImagePath() + "," + m.getStatus());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadMoviesFromFile() {
        File f = new File(FilePaths.MOVIES);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 6) 
                    movieArray.add(new MovieMg(d[0], d[1], d[2], d[3], d[4], d[5]));
                else
                    movieArray.add(new MovieMg(d[0], d[1], d[2], d[3], d[4], "Coming Soon"));
            }
            refreshList();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void refreshList() {
        movieListModel.clear();
        for (MovieMg m : movieArray) movieListModel.addElement(m);
    }

    private void clearFields() {
        txtTitle.setText(""); txtGenre.setText(""); txtDuration.setText("");
        imageLabel.setText("No Image Selected"); selectedImagePath = "";
    }

    class MovieRenderer extends JPanel implements ListCellRenderer<MovieMg> {
        private JLabel lblIcon = new JLabel(), lblTitle = new JLabel(), lblGenre = new JLabel();
        public MovieRenderer() {
            setLayout(new BorderLayout(10, 10));
            JPanel p = new JPanel(new GridLayout(2, 1));
            lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
            p.add(lblTitle); p.add(lblGenre);
            add(lblIcon, BorderLayout.WEST); add(p, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
        @Override
        public Component getListCellRendererComponent(JList<? extends MovieMg> l, MovieMg m, int i, boolean s, boolean f) {
            lblTitle.setText("Title: " + m.getTitle());
            lblGenre.setText("Genre: " + m.getGenre() + " | " + m.getDuration() + " mins");
            if (m.getImagePath() != null && !m.getImagePath().isEmpty()) {  // If image exist
                ImageIcon icon = new ImageIcon(new ImageIcon(m.getImagePath()).getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH));  // load image, resize, smoothen
                lblIcon.setIcon(icon);  // show image
            } else { lblIcon.setIcon(null); }
            setBackground(s ? new Color(200, 220, 255) : Color.WHITE);  // highlight selected item (blue)
            return this;
        }
    }
}
