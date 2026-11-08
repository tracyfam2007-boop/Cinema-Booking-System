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
import model.BookingCm;
import service.MovieServiceMg;
import service.PaymentServiceCm;
import java.util.List;
import java.util.ArrayList;

public class BookingServiceCm {
    private static final String BOOKING_FILE = FilePaths.BOOKING;
    private static final String BOOKEDSEAT_FILE = FilePaths.BOOKED_SEAT;
    
    // APPEND BOOKING RECORD INTO BOOKING.TXT
    public String recordBooking (String customerName,
                             String movie,
                             String time,
                             String hallType,
                             String hall,
                             String seat,
                             double total,
                             String bookingStatus,
                             String paymentMethod) {
        
        String bookingId = IDGeneratorCm.generateBookingId();
        
        String record = bookingId + "|" +
            customerName + "|" +
            movie + "|" +
            time + "|" +
            hallType + "|" +
            hall + "|" +
            seat + "|" +
            bookingStatus + "|" +
            paymentMethod + "|" +
            total;
        
        FileServiceCm.appendRecord(BOOKING_FILE, record); 
        
        return bookingId;
    }
    
    // GET BOOKING RECORD BY CUSTOMERID
    public List<BookingCm> getBookings(String customerId) {
        
        List<String[]> bookings = FileServiceCm.readFile(BOOKING_FILE);
        List<BookingCm> result = new ArrayList<>();
        
        // get customer name from ID
        String customerName = new PaymentServiceCm().getCustomerName(customerId);
                
        for (String[] data : bookings){
                
            if (customerId != null && data.length >= 10 && 
                data[1].trim().equals(customerName.trim())) {   
                double price = Double.parseDouble(data[9]);
                result.add(new BookingCm(
                        data[0].trim(), // bookingId
                        customerId,
                        data[2].trim(), // movie
                        data[3].trim(), // time
                        data[4].trim(), // hallType
                        data[5].trim(), // hall
                        data[6].trim(), // seat
                        price, // total
                        data[7].trim()  // status
                ));
            }
        } 
        return result;
    }
    
    // CANCEL BOOKING
    public boolean cancelBooking (String bookingId){
        
        List<String[]> bookings = FileServiceCm.readFile(BOOKING_FILE);

        for (String[] data : bookings) {
            if (data[0].trim().equals(bookingId.trim())) {
                
                // UPDATE STATUS
                String updatedRecord = String.join("|",
                    data[0].trim(),   // bookingId
                    data[1].trim(),   // customerName
                    data[2].trim(),   // movie
                    data[3].trim(),   // time
                    data[4].trim(),   // hallType
                    data[5].trim(),   // hall
                    data[6].trim(),   // seat
                    "CANCELED",       // bookingStatus 
                    data[8].trim(),   // payment method
                    data[9].trim()    // total
                );
                
                FileServiceCm.updateRecord(BOOKING_FILE, bookingId, updatedRecord);
                
                // REMOVE SEAT
                String movie = data[2].trim();
                String time = data[3].trim();
                String seatsToRelease = data[6].trim();

                // find showtimeId
                String showtimeId = MovieServiceMg.getShowtimeId(movie, time); 
                
                if (showtimeId != null) {
                    List<String[]> seatData = FileServiceCm.readFile(BOOKEDSEAT_FILE);

                    for (String[] seatLine : seatData) {
                        if (seatLine.length >= 2 && seatLine[0].equals(showtimeId)) {

                            List<String> bookedSeats = new ArrayList<>(List.of(seatLine[1].split(",")));
                            bookedSeats.removeAll(List.of(seatsToRelease.split(",")));

                            if (bookedSeats.isEmpty()) {
                                FileServiceCm.deleteRecord(BOOKEDSEAT_FILE, showtimeId);
                            } else {
                                seatLine[1] = String.join(",", bookedSeats);
                                FileServiceCm.updateRecord(BOOKEDSEAT_FILE, showtimeId, String.join("|", seatLine));
                            }
                            break;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}

