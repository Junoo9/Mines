import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private float balance;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private List<String> achievedAchievements;

    public User(String username, String password, float balance, int gamesPlayed, int gamesWon, int gamesLost) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.achievedAchievements = new ArrayList<>();
    }

    public List<String> getAchievedAchievements() {
        return achievedAchievements;
    }

    public void setAchievedAchievements(List<String> achievedAchievements) {
        this.achievedAchievements = achievedAchievements;
    }

    public void addAchievement(Achievement achievement) {
        if (!achievedAchievements.contains(achievement.getName())) {
            achievedAchievements.add(achievement.getName());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }
}
