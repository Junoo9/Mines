import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

public class MinesMenu extends JFrame {
    // Declaring instance variables for the current user and achievements manager
    private User currentUser;
    private AchievementsManager achievementsManager;
    
    // Declaring buttons for the menu
    private JButton playButton;
    private JButton statsButton;
    private JButton achievementsButton;
    private JButton resetButton;
    private JButton switchAccountButton;
    private JButton exitButton;
    private JButton creditsButton; // New Credits button
    private JButton[] buttons; // Array to hold all buttons for navigation

    // Constructor for the MinesMenu class
    public MinesMenu(User user) {
        // Initialize the current user and achievements manager
        currentUser = user;
        achievementsManager = new AchievementsManager(currentUser);
        
        // Set up the main frame
        setTitle("Mines Game - Main Menu");
        setSize(550, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 1, 10, 10)); // Adjusted for 8 rows
        getContentPane().setBackground(new Color(30, 30, 30));

        // Set the icon image for the frame
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

        // Create and add a label to show the logged-in user
        JLabel userLabel = new JLabel("Logged in as: " + currentUser.getUsername(), SwingConstants.CENTER);
        userLabel.setForeground(Color.WHITE);
        add(userLabel);

        // Create and add the menu buttons
        playButton = createButton("Play", e -> playGame(), KeyEvent.VK_P);
        add(playButton);

        statsButton = createButton("View Stats", e -> viewStats(), KeyEvent.VK_S);
        add(statsButton);

        achievementsButton = createButton("Achievements", e -> viewAchievements(), KeyEvent.VK_A);
        add(achievementsButton);

        resetButton = createButton("Reset Progress", e -> resetProgress(), KeyEvent.VK_R);
        add(resetButton);

        switchAccountButton = createButton("Switch Account", e -> switchAccount(), KeyEvent.VK_W);
        add(switchAccountButton);

        creditsButton = createButton("Credits", e -> viewCredits(), KeyEvent.VK_C); // New Credits button
        add(creditsButton);

        exitButton = createButton("Exit", e -> System.exit(0), KeyEvent.VK_E);
        add(exitButton);

        // Store all buttons in an array for navigation
        buttons = new JButton[]{playButton, statsButton, achievementsButton, resetButton, switchAccountButton, creditsButton, exitButton};

        // Set up key bindings for arrow keys
        setupKeyBindings();

        // Set the initial focus on the play button
        playButton.requestFocusInWindow();
    }

    // Method to create a button with specified text, action, and mnemonic
    private JButton createButton(String text, ActionListener action, int mnemonic) {
        JButton button = new JButton(text);
        button.setBackground(new Color(102, 102, 102));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(action);
        button.setMnemonic(mnemonic);
        return button;
    }

    // Method to set up key bindings for navigating the menu
    private void setupKeyBindings() {
        JComponent contentPane = (JComponent) getContentPane();
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = contentPane.getInputMap(condition);
        ActionMap actionMap = contentPane.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "navigateUp");
        actionMap.put("navigateUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateMenu(-1);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "navigateDown");
        actionMap.put("navigateDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateMenu(1);
            }
        });
    }

    // Method to navigate the menu using arrow keys
    private void navigateMenu(int direction) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isFocusOwner()) {
                int nextIndex = (i + direction + buttons.length) % buttons.length;
                buttons[nextIndex].requestFocusInWindow();
                break;
            }
        }
    }

    // Method to start the game
    private void playGame() {
        MinesGame game = new MinesGame(currentUser);
        game.setVisible(true);  // Make the game window visible
        game.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                achievementsManager.checkAchievements(true); // Check achievements with notifications after the game window is closed
                try {
                    UserDataManager.updateUserData(currentUser); // Save user data after achievements are checked
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error saving data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Method to view user statistics
    private void viewStats() {
        JOptionPane.showMessageDialog(this, "Stats: \nBalance: " + currentUser.getBalance() +
                "\nGames Played: " + currentUser.getGamesPlayed() +
                "\nGames Won: " + currentUser.getGamesWon() +
                "\nGames Lost: " + currentUser.getGamesLost(), 
                "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to view achievements
    private void viewAchievements() {
        List<Achievement> achievements = achievementsManager.getAchievements();
        StringBuilder achievementsText = new StringBuilder("Achievements:\n");
        for (Achievement achievement : achievements) {
            boolean achieved = currentUser.getAchievedAchievements().contains(achievement.getName());
            achievementsText.append("- ").append(achievement.getName()).append(": ").append(achievement.getDescription()).append(" - ").append(achieved ? "Achieved" : "Not Achieved").append("\n");
        }
        if (achievements.isEmpty()) {
            achievementsText.append("No achievements yet.");
        }
        JOptionPane.showMessageDialog(this, achievementsText.toString(), "Achievements", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to reset user progress
    private void resetProgress() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset your progress?", "Reset Progress", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            currentUser.setBalance(1000);
            currentUser.setGamesPlayed(0);
            currentUser.setGamesWon(0);
            currentUser.setGamesLost(0);
            try {
                UserDataManager.updateUserData(currentUser);
                JOptionPane.showMessageDialog(this, "Progress has been reset and saved.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to switch accounts
    private void switchAccount() {
        // Close all open windows
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            window.dispose();
        }

        // Run garbage collector
        System.gc();

        // Open login screen
        new Authentication(false).setVisible(true);
    }

    // New method to show the credits
    private void viewCredits() {
        String credits = "This Mines game project was created by:\n\n" +
                         "Kevin Botrous\n" +
                         "Majd Gerges\n" +
                         "Charbel Rahme\n" +
                         "Special thanks to:\n" +
                         "GFG\n" +
                         "w3schools\n\n" +
                         "References:\n" +
                         "Java How to Program by Paul Deitel\n\n" +
                         "This project is licensed under the GNU General Public License v3.0.\n" +
                         "See the LICENSE file for details.";
        JOptionPane.showMessageDialog(this, credits, "Credits", JOptionPane.INFORMATION_MESSAGE);
    }
}