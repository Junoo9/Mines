public class Achievement {
    private String name;
    private String description;
    private int bonus;
    private boolean achieved;

    public Achievement(String name, String description, int bonus) {
        this.name = name;
        this.description = description;
        this.bonus = bonus;
        this.achieved = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getBonus() {
        return bonus;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }
}