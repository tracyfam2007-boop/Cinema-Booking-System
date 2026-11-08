/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRACY
 */
//Encapsulation
public class PaymentCm {
    private String paymentId;
    private String bookingId;
    private String paymentDate;
    private String paymentTime;
    private double price;
    private String paymentMethod;

    // CONSTRUCTOR
    public PaymentCm (String paymentId, String bookingId, String paymentDate, 
            String paymentTime, double price, String paymentMethod){
        
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.price = price;
        this.paymentMethod = paymentMethod;
 
    }
    
    // GETTER
    public String getPaymentId() {
        return paymentId;
    }
    
    public String getBookingId() {
        return bookingId;
    }
    
    public String getPaymentDate() {
        return paymentDate;
    }
    
    public String getPaymentTime() {
        return paymentTime;
    }
    
    public double getPrice() {
        return price;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    // SETTER
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
