import java.util.ArrayList; // Importing the ArrayList class
import java.util.List;      // Importing the List interface

public class User {
    private String username; // Username of the user
    private String password; // Password of the user
    private float balance;   // Balance of the user
    private int gamesPlayed; // Number of games played by the user
    private int gamesWon;    // Number of games won by the user
    private int gamesLost;   // Number of games lost by the user
    private List<String> achievedAchievements; // List of achievements the user has achieved

    // Constructor to initialize a new User object with the provided details
    public User(String username, String password, float balance, int gamesPlayed, int gamesWon, int gamesLost) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.achievedAchievements = new ArrayList<>(); // Initialize the list of achievements as an empty list
    }

    // Getter method for the achievedAchievements list
    public List<String> getAchievedAchievements() {
        return achievedAchievements;
    }

    // Setter method for the achievedAchievements list
    public void setAchievedAchievements(List<String> achievedAchievements) {
        this.achievedAchievements = achievedAchievements;
    }

    // Method to add a new achievement to the achievedAchievements list
    public void addAchievement(Achievement achievement) {
        // Check if the achievement is not already in the list before adding it
        if (!achievedAchievements.contains(achievement.getName())) {
            achievedAchievements.add(achievement.getName());
        }
    }

    // Getter method for the username
    public String getUsername() {
        return username;
    }

    // Setter method for the username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter method for the password
    public String getPassword() {
        return password;
    }

    // Setter method for the password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter method for the balance
    public float getBalance() {
        return balance;
    }

    // Setter method for the balance
    public void setBalance(float balance) {
        this.balance = balance;
    }

    // Getter method for the gamesPlayed
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    // Setter method for the gamesPlayed
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    // Getter method for the gamesWon
    public int getGamesWon() {
        return gamesWon;
    }

    // Setter method for the gamesWon
    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    // Getter method for the gamesLost
    public int getGamesLost() {
        return gamesLost;
    }

    // Setter method for the gamesLost
    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    // Static method to create a User object from a comma-separated string
    public static User fromString(String userString) {
        String[] parts = userString.split(","); // Split the string into parts
        User user = new User(parts[0], parts[1], Float.parseFloat(parts[2]), Integer.parseInt(parts[3]),
                             Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
        // If there are achievements included in the string, add them to the user's achievements list
        if (parts.length > 6) {
            for (int i = 6; i < parts.length; i++) {
                user.achievedAchievements.add(parts[i]);
            }
        }
        return user; // Return the created User object
    }

    // Override the toString method to provide a comma-separated string representation of the User object
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // Use StringBuilder for efficient string concatenation
        builder.append(username).append(",").append(password).append(",").append(balance).append(",")
               .append(gamesPlayed).append(",").append(gamesWon).append(",").append(gamesLost);
        // Append each achieved achievement to the string
        for (String achievement : achievedAchievements) {
            builder.append(",").append(achievement);
        }
        return builder.toString(); // Return the constructed string
    }
}