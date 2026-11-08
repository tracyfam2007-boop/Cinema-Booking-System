/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manager;

/**
 *
 * @author TRACY
 */

import utils.FilePaths;
import java.io.*;

public class FileManager {

    private static final String STAFF_FILE = FilePaths.STAFF;

    // Ensure file exists
    private static void ensureFileExists(String filePath) {
        try {
            File file = new File(filePath);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs(); // create data folder
            }

            if (!file.exists()) {
                file.createNewFile(); // create file
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // SAVE STAFF (REGISTER)
    public static void saveStaff(String id, String password, String role) throws IOException {

        ensureFileExists(STAFF_FILE);

        BufferedWriter bw = new BufferedWriter(new FileWriter(STAFF_FILE, true));
        bw.write(id + "|" + password + "|" + role);
        bw.newLine();
        bw.close();
    }

    // VALIDATE STAFF (LOGIN)
    public static boolean validateStaff(String id, String password, String role) throws IOException {

        ensureFileExists(STAFF_FILE);

        BufferedReader br = new BufferedReader(new FileReader(STAFF_FILE));

        String line;

        while ((line = br.readLine()) != null) {

            String[] parts = line.split("\\|");

            if (parts.length >= 3) {

                String fileId = parts[0].trim();
                String filePass = parts[1].trim();
                String fileRole = parts[2].trim();

                if (id.equals(fileId) &&
                    password.equals(filePass) &&
                    role.equals(fileRole)) {

                    br.close();
                    return true;
                }
            }
        }

        br.close();
        return false;
    }

}
