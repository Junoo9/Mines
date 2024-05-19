import java.io.*;
import java.util.*;

public class UserDataManager {

    private static final String FILE_PATH = "users.txt";

    public static boolean saveUserData(String username, String password) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.createNewFile();
        }

        List<User> users = loadUserData();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(username + "," + password + ",100,0,0,0\n");  // Setting initial balance to 100
        }
        return true;
    }

    public static List<User> loadUserData() throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    User user = new User(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
                    if (parts.length > 6) {
                        user.setAchievedAchievements(Arrays.asList(parts[6].split(";")));
                    }
                    users.add(user);
                }
            }
        }
        return users;
    }

    public static boolean checkCredentials(String username, String password) throws IOException {
        List<User> users = loadUserData();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static User getUser(String username) throws IOException {
        List<User> users = loadUserData();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // User not found
    }

    public static void updateUserData(User updatedUser) throws IOException {
        List<User> users = loadUserData();
        File file = new File(FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (User user : users) {
                if (user.getUsername().equals(updatedUser.getUsername())) {
                    writer.write(String.format("%s,%s,%d,%d,%d,%d,%s\n",
                        updatedUser.getUsername(), updatedUser.getPassword(), updatedUser.getBalance(),
                        updatedUser.getGamesPlayed(), updatedUser.getGamesWon(), updatedUser.getGamesLost(),
                        String.join(";", updatedUser.getAchievedAchievements())));
                } else {
                    writer.write(String.format("%s,%s,%d,%d,%d,%d,%s\n",
                        user.getUsername(), user.getPassword(), user.getBalance(),
                        user.getGamesPlayed(), user.getGamesWon(), user.getGamesLost(),
                        String.join(";", user.getAchievedAchievements())));
                }
            }
        }
    }
}