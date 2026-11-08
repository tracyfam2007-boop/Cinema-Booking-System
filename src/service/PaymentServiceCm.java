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
import model.PaymentCm;
import java.util.List;

public class PaymentServiceCm {
    private static final String PAYMENT_FILE = FilePaths.PAYMENT;
    private static final String CUSTOMER_FILE = FilePaths.CUSTOMER;
    
    // CALCULATE SUBTOTAL
    //Abstraction
    public double calculateSubtotal (double price, int seatCount) {
        return price * seatCount;
    }
    
    // CALCULATE DISCOUNT
    public double calculateDiscount(String seatType, double subtotal){

        double discount = 0;

        switch(seatType){
            case "Student":
                discount = subtotal * 0.20; // 20%
                break;
            case "Senior":
                discount = subtotal * 0.30; // 30%
                break;
            default:
                discount = 0;
        }
        return discount;
    }
    
    // CALCUALTE TOTAL
    public double calculateTotalBeforePoints(double subtotal, double discount){
    return subtotal - discount;
}

    // SAVE PAYMENT
    public String recordPayment (String bookingId, String paymentDate, 
            String paymentTime, double price, String paymentMethod) {
        
        String paymentId = IDGeneratorCm.generatePaymentId();
        
        String record = paymentId + "|" + 
                        bookingId + "|" + 
                        paymentDate + "|" + 
                        paymentTime + "|" +
                        price + "|" + 
                        paymentMethod;
        
        FileServiceCm.appendRecord(PAYMENT_FILE, record);
        
        return paymentId;
    }
    
    // GET PAYMENT RECORD
    public static PaymentCm getPaymentByBookingId(String bookingId){

    List<String[]> rawData = FileServiceCm.readFile(PAYMENT_FILE);
    
    for(String[] data : rawData){
        if(data[1].equals(bookingId)){
            double price = Double.parseDouble(data[4]);
            return new PaymentCm(
                data[0], // paymentId
                data[1], // bookingId
                data[2], // paymentDate
                data[3], // paymentTime
                price, // price
                data[5]  // payment method
            );
        }
    }
    return null;
}
    
    // GET CUSTOMER POINT FROM CUSTOMER.TXT
    public int getCustomerPoints(String customerId) {
        List<String[]> customers = FileServiceCm.readFile(CUSTOMER_FILE);
        
        for (String[] data : customers) {
            if (data[0].equals(customerId)) {
                if (data.length >= 5 && !data[4].isEmpty()) {
                    return Integer.parseInt(data[4]);
                }
            }
        }
        return 0;
    }
    
    // CALCULATE POINTS THAT CUSTOMER CAN USE (10 POINT = RM 1)
    public double calculatePointsUsed (double totalBeforePoints, int customerPoints) {
        double discount = (customerPoints / 10.0) ; 
        if (discount > totalBeforePoints && totalBeforePoints >= 25) {
            discount = totalBeforePoints;
        }
        return discount;
    }
    
    // CALCULATE TOTAL AFTER APPLY POINTUSED (1 POINTS = -RM1)
    public double calculateTotalAfterPoints(double totalBeforePoints, int pointsUsed) {
        return totalBeforePoints - (pointsUsed / 10);
    }
    
    // CALCULATE EARN POINT (RM 10 = 1 POINT)
    public int calculateEarnedPoints(double totalAfterPoints) {
        return (int) (totalAfterPoints / 10);
    }
    
    // UPDATE POINT IN CUSTOMER.TXT
    public void updatePoints(String customerId, int pointsToAdd) {
        
        List<String[]> customers = FileServiceCm.readFile(CUSTOMER_FILE);

        for (String[] data : customers) {
            if (data[0].equals(customerId)) {
                int currentPoints = 0;
                
                if (data.length >= 5 && !data[4].isEmpty()) {
                    currentPoints = Integer.parseInt(data[4]);
                }               
                currentPoints += pointsToAdd;
                data[4] = String.valueOf(currentPoints);
                
                String record = String.join("|", data);

                FileServiceCm.updateRecord(CUSTOMER_FILE, customerId, record);
                break;
            }
        }
    }
    
    public String getCustomerName(String customerId) {

        List<String[]> customers = FileServiceCm.readFile(FilePaths.CUSTOMER);

        for (String[] data : customers) {
            if (data[0].equals(customerId)) {
                return data[1]; // assuming name is column 1
            }
        }
        return customerId; // fallback
    }

}


