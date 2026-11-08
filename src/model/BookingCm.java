/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRACY
 */
public class BookingCm {
    private String bookingId;
    private String customerId;
    private String movie;
    private String time;
    private String hallType;
    private String hall;
    private String seat;
    private double total;
    private String bookingStatus;

    // CONSTRUCTOR
    public BookingCm(String bookingId, String customerId,
                 String movie, String time,
                 String hallType, String hall,
                 String seat, double total, String bookingStatus){

    this.bookingId = bookingId;
    this.customerId = customerId;
    this.movie = movie;
    this.time = time;
    this.hallType = hallType;
    this.hall = hall;
    this.seat = seat;
    this.total = total;
    this.bookingStatus = bookingStatus;
    }
    
    // GETTER
    public String getBookingId() {
        return bookingId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public String getMovie() {
        return movie;
    }
    
    public String getTime() {
        return time;
    }
    
    public String getHallType() {
        return hallType;
    }
    
    public String getHall() {
        return hall;
    }
    
    public String getSeat() {
        return seat;
    }
    
    public double getTotal() {
        return total;
    }
    
    public String getBookingStatus() {
        return bookingStatus;
    }
    
    // SETTER
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public void setMovie(String Movie) {
        this.movie = movie;
    }
    
    public void setTime(String Time) {
        this.time = time;
    }
    
    public void setHallType() {
        this.hallType = hallType;
    }
    
    public void setHall() {
        this.hall = hall;
    }
    
    public void setSeat(String seat) {
        this.seat = seat;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}

