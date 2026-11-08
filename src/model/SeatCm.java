/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRACY
 */
public class SeatCm {
    private String showTimeId;
    private String seat;

    // CONSTRUCTOR
    public SeatCm (String showTimeId, String seat){
        
        this.showTimeId = showTimeId;
        this.seat = seat;
 
    }
    
    // GETTER
    public String getShowTimeId() {
        return showTimeId;
    }
    
    public String getSeat() {
        return seat;
    }

    // SETTER
    public void setShowTimeId(String showTimeId) {
        this.showTimeId = showTimeId;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Seat: " + this.seat;
    }
}

