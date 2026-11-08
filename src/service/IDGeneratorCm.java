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
import java.io.*;

public class IDGeneratorCm {
    // BOOKINGID
    public static String generateBookingId() {
        return generateId("B", FilePaths.BOOKING);
    }

    // CUSTOMERID
    public static String generateCustomerId() {
        return generateId("C", FilePaths.CUSTOMER);
    }

    // PAYMENTID
    public static String generatePaymentId() {
        return generateId("P", FilePaths.PAYMENT);
    }

    // SHOWTIMEID
    public static String generateShowtimeId() {
        return generateId("S", FilePaths.SHOWTIMES);
    }

    // MOVIEID
    public static String generateMovieId() {
        return generateId("M", FilePaths.MOVIES);
    }

    private static String generateId(String prefix, String filePath) {
        int lastIdNum = 0;

        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                String lastLine = null;
                while ((line = br.readLine()) != null) {
                    lastLine = line;
                }

                if (lastLine != null && !lastLine.isEmpty()) {
                    String[] parts = lastLine.split("\\|");
                    if (parts.length > 0) {
                        String lastId = parts[0]; 
                        if (lastId.startsWith(prefix)) {
                            lastIdNum = Integer.parseInt(lastId.substring(1));
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int newIdNum = lastIdNum + 1;
        return String.format("%s%03d", prefix, newIdNum);
    }

}
