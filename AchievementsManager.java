import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class AchievementsManager {
    private List<Achievement> achievements;
    private User user;

    public AchievementsManager(User user) {
        this.user = user;
        achievements = new ArrayList<>();
        initializeAchievements();
        checkAchievements();
    }

    private void initializeAchievements() {
        achievements.add(new Achievement("First Game", "Play your first game", 50));
        achievements.add(new Achievement("First Win", "Win your first game", 100));
        achievements.add(new Achievement("High Roller", "Reach a balance of $500", 200));
        achievements.add(new Achievement("Veteran Player", "Play 100 games", 300));
        achievements.add(new Achievement("Winning Streak", "Win 50 games", 500));
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void checkAchievements() {
        for (Achievement achievement : achievements) {
            if (!user.getAchievedAchievements().contains(achievement.getName())) {
                if (achievement.getName().equals("First Game") && user.getGamesPlayed() >= 1) {
                    unlockAchievement(achievement);
                } else if (achievement.getName().equals("First Win") && user.getGamesWon() >= 1) {
                    unlockAchievement(achievement);
                } else if (achievement.getName().equals("High Roller") && user.getBalance() >= 500) {
                    unlockAchievement(achievement);
                } else if (achievement.getName().equals("Veteran Player") && user.getGamesPlayed() >= 100) {
                    unlockAchievement(achievement);
                } else if (achievement.getName().equals("Winning Streak") && user.getGamesWon() >= 50) {
                    unlockAchievement(achievement);
                }
            }
        }
    }

    private void unlockAchievement(Achievement achievement) {
        achievement.setAchieved(true);
        user.setBalance(user.getBalance() + achievement.getBonus());
        user.addAchievement(achievement);
        JOptionPane.showMessageDialog(null, "Achievement Unlocked: " + achievement.getName() + "!\nBonus: $" + achievement.getBonus());
    }
}