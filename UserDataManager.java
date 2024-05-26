// Import necessary classes for input/output operations and utility functions
import java.io.*;
import java.util.*;

// Define a public class named UserDataManager to handle user data operations
public class UserDataManager {
    // Constant variable for the file name where user data will be stored
    private static final String USER_DATA_FILE = "users.txt";

    // Method to save user data (username and password) to the file
    public static boolean saveUserData(String username, String password) throws IOException {
        // Create a list to hold the lines of user data read from the file
        List<String> userLines = new ArrayList<>();
        // Create a File object representing the user data file
        File userFile = new File(USER_DATA_FILE);

        // If the file exists, read its contents
        if (userFile.exists()) {
            // Use BufferedReader to read the file line by line
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                String line;
                // Read each line and add it to the list
                while ((line = reader.readLine()) != null) {
                    userLines.add(line);
                }
            }
        }

        // Check if the username already exists in the file
        for (String userLine : userLines) {
            String[] parts = userLine.split(",");
            if (parts[0].equals(username)) {
                return false; // Username already exists, return false
            }
        }

        // If username is unique, write the new user data to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(userFile, true))) {
            writer.println(username + "," + password + ",1000.0,0,0,0"); // Default values for new user
        }
        return true; // User data saved successfully
    }

    // Method to check if the provided username and password are correct
    public static boolean checkCredentials(String username, String password) throws IOException {
        // Create a File object representing the user data file
        File userFile = new File(USER_DATA_FILE);

        // If the file does not exist, return false
        if (!userFile.exists()) {
            return false;
        }

        // Use BufferedReader to read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            // Read each line and split it by comma to get user data parts
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Check if the username and password match
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true; // Credentials are correct
                }
            }
        }
        return false; // Credentials are incorrect
    }

    // Method to get a User object by username
    public static User getUser(String username) throws IOException {
        // Create a File object representing the user data file
        File userFile = new File(USER_DATA_FILE);

        // If the file does not exist, return null
        if (!userFile.exists()) {
            return null;
        }

        // Use BufferedReader to read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            // Read each line and split it by comma to get user data parts
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // If the username matches, create and return a User object
                if (parts[0].equals(username)) {
                    float balance = Float.parseFloat(parts[2]);
                    int gamesPlayed = Integer.parseInt(parts[3]);
                    int gamesWon = Integer.parseInt(parts[4]);
                    int achievements = Integer.parseInt(parts[5]);
                    return new User(username, parts[1], balance, gamesPlayed, gamesWon, achievements);
                }
            }
        }
        return null; // User not found
    }

    // Method to update user data in the file
    public static boolean updateUserData(User user) throws IOException {
        // Create a File object representing the user data file
        File userFile = new File(USER_DATA_FILE);
        // If the file does not exist, return false
        if (!userFile.exists()) {
            return false;
        }

        // Create a list to hold the lines of user data read from the file
        List<String> userLines = new ArrayList<>();
        // Use BufferedReader to read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            // Read each line and split it by comma to get user data parts
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // If the username matches, update the user's data
                if (parts[0].equals(user.getUsername())) {
                    line = user.getUsername() + "," + user.getPassword() + "," +
                            user.getBalance() + "," + user.getGamesPlayed() + "," +
                            user.getGamesWon() + "," + user.getAchievedAchievements().size();
                }
                // Add the (possibly updated) line to the list
                userLines.add(line);
            }
        }

        // Write the updated user data back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(userFile))) {
            for (String userLine : userLines) {
                writer.println(userLine); // Write each line to the file
            }
        }
        return true; // User data updated successfully
    }
}
