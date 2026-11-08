/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author TRACY
 */

import model.MovieMg;
import model.ShowtimeMg;
import utils.FilePaths;

import java.io.*;
import java.util.*;

public class MovieServiceMg {
    // GET ALL MOVIES
    public static List<MovieMg> getMovies() {
        List<MovieMg> movies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.MOVIES))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 6) {
                    movies.add(new MovieMg(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            data[5]
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    // GET MOVIE BY TITLE
    public static MovieMg getMovieByTitle(String title) {
        for (MovieMg movie : getMovies()) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null;
    }

    // GET SHOWTIME BY ID
    public static ShowtimeMg getShowtimeById(String showtimeId) {
        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.SHOWTIMES))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 6 && data[0].equals(showtimeId)) {
                    return new ShowtimeMg(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            data[5]
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    // GET SHOWTIMES BY MOVIE TITLE
    public static List<ShowtimeMg> getShowtimesByMovieTitle(String title) {
        List<ShowtimeMg> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.SHOWTIMES))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 6 && data[1].equalsIgnoreCase(title)) {
                    list.add(new ShowtimeMg(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            data[5]
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // GET SHOWTIMES BY DATE
    public static List<ShowtimeMg> getShowtimesByDate(String title, String date) {
        List<ShowtimeMg> list = new ArrayList<>();

        for (ShowtimeMg s : getShowtimesByMovieTitle(title)) {
            if (s.getShowdate().equals(date)) {
                list.add(s);
            }
        }

        return list;
    }
    
    public static String getShowtimeId(String movie, String time){

        try (BufferedReader br = new BufferedReader(new FileReader(FilePaths.SHOWTIMES))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length >= 6) {

                    String fileMovie = data[1].trim();
                    String fileTime = data[3].trim();

                    if (fileMovie.equalsIgnoreCase(movie.trim()) &&
                        fileTime.equalsIgnoreCase(time.trim())) {

                        return data[0].trim(); // return showtimeId
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
