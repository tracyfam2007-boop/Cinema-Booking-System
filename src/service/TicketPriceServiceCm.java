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

public class TicketPriceServiceCm {
    
    public double getPriceByHallType(String movieTitle, String hallType) {

        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.TICKET_PRICE))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 3) {
                    String fileMovie = data[0];
                    String fileHall = data[1];
                    double price = Double.parseDouble(data[2]);

                    if (fileMovie.equalsIgnoreCase(movieTitle)
                            && fileHall.equalsIgnoreCase(hallType)) {
                        return price;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0; // default if not found
    }
}




