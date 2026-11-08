/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRACY
 */
public class MovieMg {
    
    // Encapsulation
    private String movieId;
    private String title;
    private String genre;
    private String duration;
    private String imagePath;
    private String status;
    
    // Abstraction
    // Constructor
    public MovieMg(String movieId, String title, String genre, String duration, String imagePath, String status){
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.imagePath = imagePath;
        this.status = status;
    }
    
    // Overloaded constructor (if image is not available)
    public MovieMg(String movieId, String title){
        this(movieId, title, "Unknown", "0", "default.png", "Coming Soon");
    }
    
    // GETTERS - read data
    public String getMovieId(){
        return movieId;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getGenre(){
        return genre;
    }
    
    public String getDuration(){
        return duration;
    }
    
    public String getImagePath(){
        return imagePath;
    }
    
    public String getStatus(){
        return status;
    }
    
    // SETTERS - update data
    public void setMovieId(String movieId){
        this.movieId = movieId;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setGenre(String genre){
        this.genre = genre;
    }
    
    public void setDuration(String duration){
        this.duration = duration;
    }
    
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    @Override
    // Polymorphism
    // convert to String
    public String toString(){   
        return movieId + "," + title + "," + genre + "," + duration + "," + imagePath + "," + status;
    }
}