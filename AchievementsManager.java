import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// The AchievementsManager class manages the achievements for a given user
public class AchievementsManager {
    private List<Achievement> achievements; // List to store all possible achievements
    private User user; // The user for whom achievements are being managed

    // Constructor to initialize the AchievementsManager with a specific user
    public AchievementsManager(User user) {
        this.user = user; // Set the user
        achievements = new ArrayList<>(); // Initialize the achievements list
        initializeAchievements(); // Initialize the predefined achievements
        checkAchievements(false); // Check achievements without showing notifications during initialization
    }

    // Method to define and add the predefined achievements to the list
    private void initializeAchievements() {
        achievements.add(new Achievement("First Game", "Play your first game", 50)); // Achievement for playing the first game
        achievements.add(new Achievement("First Win", "Win your first game", 100)); // Achievement for winning the first game
        achievements.add(new Achievement("High Roller", "Reach a balance of $500000", 200)); // Achievement for reaching a balance of $500,000
        achievements.add(new Achievement("Veteran Player", "Play 100 games", 300)); // Achievement for playing 100 games
        achievements.add(new Achievement("Winning Streak", "Win 50 games", 500)); // Achievement for winning 50 games
    }

    // Method to get the list of all achievements
    public List<Achievement> getAchievements() {
        return achievements;
    }

    // Method to check and unlock achievements based on the user's progress
    public void checkAchievements(boolean showNotification) {
        for (Achievement achievement : achievements) {
            // If the user hasn't unlocked this achievement yet
            if (!user.getAchievedAchievements().contains(achievement.getName())) {
                boolean unlocked = false;
                
                // Check the conditions for unlocking each achievement
                if (achievement.getName().equals("First Game") && user.getGamesPlayed() >= 1) {
                    unlocked = true; // Unlock if the user has played their first game
                } else if (achievement.getName().equals("First Win") && user.getGamesWon() >= 1) {
                    unlocked = true; // Unlock if the user has won their first game
                } else if (achievement.getName().equals("High Roller") && user.getBalance() >= 500000) {
                    unlocked = true; // Unlock if the user's balance is at least $500,000
                } else if (achievement.getName().equals("Veteran Player") && user.getGamesPlayed() >= 100) {
                    unlocked = true; // Unlock if the user has played 100 games
                } else if (achievement.getName().equals("Winning Streak") && user.getGamesWon() >= 50) {
                    unlocked = true; // Unlock if the user has won 50 games
                }

                // If the achievement is unlocked, process it
                if (unlocked) {
                    unlockAchievement(achievement, showNotification);
                }
            }
        }
    }

    // Method to unlock an achievement and update the user's data
    private void unlockAchievement(Achievement achievement, boolean showNotification) {
        achievement.setAchieved(true); // Mark the achievement as achieved
        user.setBalance(user.getBalance() + achievement.getBonus()); // Add the achievement bonus to the user's balance
        user.addAchievement(achievement); // Add the achievement to the user's list of achieved achievements
        // Show a notification if requested
        if (showNotification) {
            JOptionPane.showMessageDialog(null, "Achievement Unlocked: " + achievement.getName() + "!\nBonus: $" + achievement.getBonus());
        }
    }
}