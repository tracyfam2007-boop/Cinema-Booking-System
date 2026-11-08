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
import model.SeatCm;
import java.util.*;
import java.io.*;

public class SeatServiceCm {
    private static final String BOOKEDSEAT_FILE = FilePaths.BOOKED_SEAT;

    public static List<SeatCm> getSeatsByShowtime(String showtimeId) {

        List<String[]> rawData = FileServiceCm.readFile(BOOKEDSEAT_FILE);
        List<SeatCm> seats = new ArrayList<>();

        for (String[] data : rawData) {

            if (data.length >= 2 && data[0].equals(showtimeId)) {
                seats.add(new SeatCm(
                        data[0].trim(), // showtimeId
                        data[1].trim() // seat
                ));
            }
        }
        return seats;
    }
  
    // GET BOOKED SEAT
    public static Set<String> getBookedSeats(String showtimeId) {
        
        Set<String> bookedSeats = new HashSet<>();
        List<String[]> seatLines = FileServiceCm.readFile(BOOKEDSEAT_FILE);
        
        for (String[] data : seatLines) {
            if(data.length >=2 && data[0].equals(showtimeId)){
                String[] seats = data[1].split(","); 
                bookedSeats.addAll(Arrays.asList(seats)); 
            }
        }
        return bookedSeats;
    }
    
    // RECORD BOOK SEAT
    public void recordSeat (String showtimeId, String seat) {
        
        String record = showtimeId + "|" + seat;
        
        FileServiceCm.appendRecord(BOOKEDSEAT_FILE, record);
    }
    
    public void removeSeat(String showtimeId, String seatToRemove) {

        List<String[]> allData = FileServiceCm.readFile(BOOKEDSEAT_FILE);
        List<String> updatedLines = new ArrayList<>();

        for (String[] data : allData) {

            if (data.length >= 2) {

                String id = data[0];
                String seats = data[1];

                if (id.equals(showtimeId)) {

                    // split existing seats
                    List<String> seatList = new ArrayList<>(Arrays.asList(seats.split(",")));

                    // remove selected seats
                    for(String s : seatToRemove.split(",")){
                        seatList.remove(s);
                    }

                    // rebuild line
                    if (!seatList.isEmpty()) {
                        updatedLines.add(id + "|" + String.join(",", seatList));
                    }
                    // if empty → do NOT add (means all seats removed)

                } else {
                    updatedLines.add(id + "|" + seats);
                }
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKEDSEAT_FILE))) {
            for (String line : updatedLines) {
                writer.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
