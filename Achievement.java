// The Achievement class represents a specific achievement with its properties
public class Achievement {
    private String name; // Name of the achievement
    private String description; // Description of what the achievement entails
    private int bonus; // Bonus awarded for achieving this achievement
    private boolean achieved; // Flag indicating whether the achievement has been achieved

    // Constructor to initialize the achievement with its name, description, and bonus
    public Achievement(String name, String description, int bonus) {
        this.name = name; // Set the name of the achievement
        this.description = description; // Set the description of the achievement
        this.bonus = bonus; // Set the bonus for the achievement
        this.achieved = false; // Initialize the achievement as not yet achieved
    }

    // Getter method for the name of the achievement
    public String getName() {
        return name;
    }

    // Getter method for the description of the achievement
    public String getDescription() {
        return description;
    }

    // Getter method for the bonus of the achievement
    public int getBonus() {
        return bonus;
    }

    // Getter method to check if the achievement has been achieved
    public boolean isAchieved() {
        return achieved;
    }

    // Setter method to mark the achievement as achieved or not achieved
    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }
}