/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRACY
 */
public class ShowtimeMg {
    private String showtimeId;
    private String movieTitle;   
    private String showdate;
    private String time;
    private String hallType;
    private String hall;
    
    // constructor 
    public ShowtimeMg(String showtimeId, String movieTitle, String showdate, String time, String hallType, String hall){
        this.showtimeId = showtimeId;
        this.movieTitle = movieTitle;
        this.showdate = showdate;
        this.time = time;
        this.hallType = hallType;
        this.hall = hall;
    }

    // GETTERS
    public String getShowtimeId(){
        return showtimeId;
    }
    
    public String getMovieTitle(){
        return movieTitle;
    }
    
    public String getShowdate(){
        return showdate;
    }
    
    public String getTime(){
        return time;
    }
    
    public String getHallType(){
        return hallType;
    }
    
    public String getHall(){
        return hall;
    }

    // SETTERS
    public void setShowtimeId(String showtimeId){
        this.showtimeId = showtimeId;
    }
    
    public void setMovieTitle(String movieTitle){
        this.movieTitle = movieTitle;
    }
    
    public void setShowdate(String showdate){
        this.showdate = showdate;
    }
    
    public void setTime(String time){
        this.time = time;
    }
    
    public void setHallType(String hallType){
        this.hallType = hallType;
    }
    
    public void setHall(String hall){
        this.hall = hall;
    }

    // Display in GUI (only time)
    @Override
    public String toString(){
        return time; // ONLY SHOW TIME
    }

    // Save full data to file
    public String toFileString(){
        return showtimeId + "," + movieTitle + "," + showdate + "," + time + "," + hallType + "," + hall;
    }
}
