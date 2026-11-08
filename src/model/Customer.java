/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TRACY
 */
public class Customer {
    // ENCAPSULATION (SHOULD NOT BE MODIFIED)
    private String customerId;
    private String fullName;
    private String phoneNumber;
    private String dateOfBirthday;
    private int points;

    // CONSTRUCTOR (INITIALIZES OBJECT VALUES)
    public Customer (String customerId, String fullName, 
            String phoneNumber, String dateOfBirthday, int points){
        
        this.customerId = customerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirthday = dateOfBirthday;
        this.points = points;
 
    }
    
    // GETTER (READ ONLY)
    public String getCustomerId() {
        return customerId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getDateOfBirthday() {
        return dateOfBirthday;
    }
    
    public int getPoints() {
        return points;
    }
    
    // SETTER (ALLOW MODIFY)
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setDateOfBirthday(String dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
    
    // METHOD OVERRIDING
    @Override
    public String toString() {
        return "ID: " + this.customerId + " | Name: " + this.fullName;
    }
}

