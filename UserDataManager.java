import java.io.*;
import java.util.*;

public class UserDataManager {
    private static final String USER_DATA_FILE = "users.txt";

    public static boolean saveUserData(String username, String password) throws IOException {
        List<String> userLines = new ArrayList<>();
        File userFile = new File(USER_DATA_FILE);

        if (userFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    userLines.add(line);
                }
            }
        }

        for (String userLine : userLines) {
            String[] parts = userLine.split(",");
            if (parts[0].equals(username)) {
                return false; // Username already exists
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(userFile, true))) {
            writer.println(username + "," + password + ",1000.0,0,0,0");
        }
        return true;
    }

    public static boolean checkCredentials(String username, String password) throws IOException {
        File userFile = new File(USER_DATA_FILE);

        if (!userFile.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static User getUser(String username) throws IOException {
        File userFile = new File(USER_DATA_FILE);

        if (!userFile.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    float balance = Float.parseFloat(parts[2]);
                    int gamesPlayed = Integer.parseInt(parts[3]);
                    int gamesWon = Integer.parseInt(parts[4]);
                    int achievements = Integer.parseInt(parts[5]);
                    return new User(username, parts[1], balance, gamesPlayed, gamesWon, achievements);
                }
            }
        }
        return null;
    }

    public static boolean updateUserData(User user) throws IOException {
        File userFile = new File(USER_DATA_FILE);
        if (!userFile.exists()) {
            return false;
        }

        List<String> userLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(user.getUsername())) {
                    // Update the user's data
                    line = user.getUsername() + "," + user.getPassword() + "," +
                            user.getBalance() + "," + user.getGamesPlayed() + "," +
                            user.getGamesWon() + "," + user.getAchievedAchievements().size();
                }
                userLines.add(line);
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(userFile))) {
            for (String userLine : userLines) {
                writer.println(userLine);
            }
        }
        return true;
    }
}