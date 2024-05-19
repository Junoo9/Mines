import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

public class MinesMenu extends JFrame {
    private User currentUser;
    private AchievementsManager achievementsManager;
    private JButton playButton;
    private JButton statsButton;
    private JButton achievementsButton;
    private JButton resetButton;
    private JButton switchAccountButton;
    private JButton exitButton;
    private JButton[] buttons;

    public MinesMenu(User user) {
        currentUser = user;
        achievementsManager = new AchievementsManager(currentUser); // Initialize AchievementsManager
        setTitle("Mines Game - Main Menu");
        setSize(550, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 10, 10)); // 7 rows for menu items and username
        getContentPane().setBackground(new Color(30, 30, 30));

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

        JLabel userLabel = new JLabel("Logged in as: " + currentUser.getUsername(), SwingConstants.CENTER);
        userLabel.setForeground(Color.WHITE);
        add(userLabel);

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

        exitButton = createButton("Exit", e -> System.exit(0), KeyEvent.VK_E);
        add(exitButton);

        buttons = new JButton[]{playButton, statsButton, achievementsButton, resetButton, switchAccountButton, exitButton};

        // Set up key bindings for arrow keys
        setupKeyBindings();

        // Set the initial focus
        playButton.requestFocusInWindow();
    }

    private JButton createButton(String text, ActionListener action, int mnemonic) {
        JButton button = new JButton(text);
        button.setBackground(new Color(102, 102, 102));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(action);
        button.setMnemonic(mnemonic);
        return button;
    }

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

    private void navigateMenu(int direction) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isFocusOwner()) {
                int nextIndex = (i + direction + buttons.length) % buttons.length;
                buttons[nextIndex].requestFocusInWindow();
                break;
            }
        }
    }

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

    private void viewStats() {
        JOptionPane.showMessageDialog(this, "Stats: \nBalance: " + currentUser.getBalance() +
                "\nGames Played: " + currentUser.getGamesPlayed() +
                "\nGames Won: " + currentUser.getGamesWon() +
                "\nGames Lost: " + currentUser.getGamesLost(), 
                "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

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

    private void resetProgress() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset your progress?", "Reset Progress", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            currentUser.setBalance(100);
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
}