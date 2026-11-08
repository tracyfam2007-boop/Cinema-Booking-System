/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author TRACY
 */

import utils.FilePaths;
import model.Customer;
import java.util.List;

public class CustomerService {
    private static final String CUSTOMER_FILE = FilePaths.CUSTOMER;
    
    // LOGIN
    public boolean login(String customerId, String phoneNumber) {
        
        List<String[]> customers = FileServiceCm.readFile(CUSTOMER_FILE);
        
        for (String[] data : customers){
            if (data.length >= 3) {
                String fileCustomerId = data[0].trim();
                String filePhoneNumber = data[2].trim();

                if (customerId.equalsIgnoreCase(fileCustomerId) && 
                    phoneNumber.equals(filePhoneNumber)) {
                    return true;
                }
            }
        }
        return false;
    }
 
    // REGISTER
    public String register (String fullName, 
            String phoneNumber, String dateOfBirthday) {
        
        String customerId = IDGeneratorCm.generateCustomerId();
        
        String record = customerId + "|" + 
                        fullName + "|" + 
                        phoneNumber + "|" + 
                        dateOfBirthday + "|0";
        
        FileServiceCm.appendRecord(CUSTOMER_FILE, record);
        
        return customerId;
    }
    
    // GET DETAILS
    public Customer getDetails(String customerId) {
        
        List<String[]> customers = FileServiceCm.readFile(CUSTOMER_FILE);
        
        for (String[] data : customers){
                
            if (customerId != null && data.length >= 5 && data[0].trim().equals(customerId.trim())) {
                
                return new Customer(
                        data[0].trim(), // customerId
                        data[1].trim(), // fullName
                        data[2].trim(), // phoneNumber
                        data[3].trim(), // dateOfBirthday
                        Integer.parseInt(data[4].trim()) // points
                );
            }
        } 
        return null;
    }
    
    // UPDATE CUSTOMER DETAILS
    public void updateDetails (Customer customer) {
        
        String record = customer.getCustomerId() + "|" +
                        customer.getFullName() + "|" +
                        customer.getPhoneNumber() + "|" +
                        customer.getDateOfBirthday() + "|" +
                        customer.getPoints();
        FileServiceCm.updateRecord(CUSTOMER_FILE, customer.getCustomerId(), record);
    }

}

