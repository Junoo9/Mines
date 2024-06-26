import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class MinesGame extends JFrame {
    // Instance variables for game state and UI components
    private User currentUser;
    private JButton[][] gridButtons = new JButton[5][5];
    private Set<Point> mineLocations = new HashSet<>();
    private Set<Point> diamondLocations = new HashSet<>();
    private JTextField betAmountField;
    private JComboBox<Integer> mineCountSelector;
    private JButton startButton;
    private JButton cashoutButton;
    private JLabel profitLabel;
    private JLabel multiplierLabel;
    private JLabel balanceLabel;
    private float betAmount;
    private boolean gameStarted = false;
    private float profit = 0.0f;
    private int safeClicks = 0;
    private JPanel gridPanel;
    private AchievementsManager achievementsManager;

    // Constructor initializing the game with the current user
    public MinesGame(User user) {
        currentUser = user;
        achievementsManager = new AchievementsManager(currentUser);
        setTitle("Mines Game");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Set the icon for the game window
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

        // Top panel configuration
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(30, 30, 30));
        balanceLabel = new JLabel("Balance: $" + currentUser.getBalance());
        balanceLabel.setForeground(Color.WHITE);
        topPanel.add(balanceLabel, BorderLayout.WEST);

        // Input panel configuration within the top panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inputPanel.setBackground(new Color(30, 30, 30));

        JLabel betAmountLabel = new JLabel("Bet Amount:");
        betAmountLabel.setForeground(Color.WHITE);
        inputPanel.add(betAmountLabel);

        betAmountField = new JTextField(5);
        inputPanel.add(betAmountField);

        JLabel mineCountLabel = new JLabel("Mines:");
        mineCountLabel.setForeground(Color.WHITE);
        inputPanel.add(mineCountLabel);

        // Mine count selector configuration
        Integer[] mineOptions = new Integer[24];
        for (int i = 1; i <= 24; i++) {
            mineOptions[i - 1] = i;
        }
        mineCountSelector = new JComboBox<>(mineOptions);
        inputPanel.add(mineCountSelector);

        // Start button configuration
        startButton = new JButton("Start");
        startButton.setBackground(new Color(0, 153, 76));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(this::startGame);
        inputPanel.add(startButton);

        topPanel.add(inputPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Grid panel configuration
        gridPanel = new JPanel(new GridLayout(5, 5, 5, 5));
        gridPanel.setBackground(new Color(50, 50, 50));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                JButton button = new JButton();
                button.setEnabled(false);
                button.setBackground(new Color(102, 102, 102));
                final int x = i;
                final int y = j;
                button.addActionListener(e -> processClick(x, y));
                gridButtons[i][j] = button;
                gridPanel.add(button);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // Bottom panel configuration
        profitLabel = new JLabel("Profit: $0.00", SwingConstants.CENTER);
        profitLabel.setForeground(Color.WHITE);

        multiplierLabel = new JLabel("Multiplier: 1.00", SwingConstants.CENTER);
        multiplierLabel.setForeground(Color.WHITE);

        cashoutButton = new JButton("Cashout");
        cashoutButton.setEnabled(false);
        cashoutButton.setBackground(new Color(255, 51, 51));
        cashoutButton.setForeground(Color.WHITE);
        cashoutButton.addActionListener(e -> cashout());

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(profitLabel, BorderLayout.NORTH);
        bottomPanel.add(multiplierLabel, BorderLayout.CENTER);
        bottomPanel.add(cashoutButton, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Start game logic
    private void startGame(ActionEvent e) {
        try {
            betAmount = Float.parseFloat(betAmountField.getText());
            if (betAmount > currentUser.getBalance()) {
                JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int mineCount = (int) mineCountSelector.getSelectedItem();
            if (mineCount >= 25) {
                JOptionPane.showMessageDialog(this, "Too many mines! Choose fewer mines.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            currentUser.setBalance(currentUser.getBalance() - betAmount); // Deduct the bet amount at the start
            currentUser.setGamesPlayed(currentUser.getGamesPlayed() + 1);
            updateUserData();
            profit = 0.0f;
            safeClicks = 0;
            profitLabel.setText("Profit: $0.00");
            multiplierLabel.setText("Multiplier: 1.00");
            cashoutButton.setEnabled(false);
            gameStarted = true;
            startButton.setEnabled(false);
            generateMines(mineCount);
            generateDiamonds(25 - mineCount);
            enableGrid();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid bet amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Generate mine locations
    private void generateMines(int mineCount) {
        mineLocations.clear();
        Random rand = new Random();
        while (mineLocations.size() < mineCount) {
            int x = rand.nextInt(5);
            int y = rand.nextInt(5);
            mineLocations.add(new Point(x, y));
        }
    }

    // Generate diamond locations
    private void generateDiamonds(int maxDiamonds) {
        diamondLocations.clear();
        Random rand = new Random();
        int diamondCount = Math.min(maxDiamonds, 25 - mineLocations.size());
        while (diamondLocations.size() < diamondCount) {
            int x = rand.nextInt(5);
            int y = rand.nextInt(5);
            Point diamondPoint = new Point(x, y);
            if (!mineLocations.contains(diamondPoint) && !diamondLocations.contains(diamondPoint)) {
                diamondLocations.add(diamondPoint);
            }
        }
    }

    // Enable the grid for user interaction
    private void enableGrid() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridButtons[i][j].setEnabled(true);
                gridButtons[i][j].setText("");
                gridButtons[i][j].setBackground(new Color(102, 102, 102));
            }
        }
    }

    // Process user clicks on the grid
    private void processClick(int x, int y) {
        Point clickedPoint = new Point(x, y);
        if (!gridButtons[x][y].isEnabled()) {
            return;
        }

        if (mineLocations.contains(clickedPoint)) {
            playSound("bomb.wav"); // Play mine sound
            gridButtons[x][y].setForeground(Color.WHITE);
            gridButtons[x][y].setFont(new Font("Segoe UI Emoji", Font.PLAIN, 31));
            gridButtons[x][y].setText("\uD83D\uDCA3");
            gridButtons[x][y].setBackground(Color.RED);
            JOptionPane.showMessageDialog(this, "Mine hit! Game over.");
            currentUser.setGamesLost(currentUser.getGamesLost() + 1);
            updateUserData();
            revealGrid();
            endGame();
        } else {
            playSound("gem.wav"); // Play gem sound
            if (diamondLocations.contains(clickedPoint)) {
                gridButtons[x][y].setForeground(Color.WHITE);
                gridButtons[x][y].setFont(new Font("Segoe UI Emoji", Font.PLAIN, 31));
                gridButtons[x][y].setText("\uD83D\uDC8E");
                gridButtons[x][y].setBackground(new Color(0, 204, 204));
            } else {
                gridButtons[x][y].setForeground(Color.WHITE);
                gridButtons[x][y].setFont(new Font("Segoe UI Emoji", Font.PLAIN, 31));
                gridButtons[x][y].setText("\uD83D\uDC8E");
                gridButtons[x][y].setBackground(new Color(0, 204, 0));
            }
            safeClicks++;
            updateProfit();
        }
        gridButtons[x][y].setEnabled(false);
    }

    // Update profit and multiplier based on user actions
    private void updateProfit() {
        int mineCount = (int) mineCountSelector.getSelectedItem();
        double multiplier = calculateMultiplier(mineCount, safeClicks);
        multiplierLabel.setText(String.format("Multiplier: %.2f", multiplier));
        profit = (float) ((betAmount * multiplier) - betAmount);
        profitLabel.setText(String.format("Profit: $%.2f", profit));
        cashoutButton.setEnabled(true);
    }

    // Cashout logic for ending the game and updating user balance
    private void cashout() {
        currentUser.setBalance(currentUser.getBalance() + profit + betAmount); // Add original bet amount back to the balance
        currentUser.setGamesWon(currentUser.getGamesWon() + 1);
        JOptionPane.showMessageDialog(this, "You cashed out $" + String.format("%.2f", profit) + "!");
        updateUserData();
        revealGrid();
        endGame();
    }

    // Calculate the multiplier based on mine count and safe clicks
    private double calculateMultiplier(int mineCount, int safeClicks) {
        double baseMultiplier = 1.0 + (safeClicks * 0.07 * mineCount);
        double mineAdjustment = 1.0 + (mineCount * 0.05);

        if (mineCount <= 10) {
            return baseMultiplier * mineAdjustment;
        } else if (mineCount <= 15) {
            return baseMultiplier * (mineAdjustment + 0.1); // Slightly higher reward for 11-15 mines
        } else if (mineCount == 24) {
            return baseMultiplier * 4 * (mineAdjustment + 0.1);
        } else {
            return baseMultiplier * (mineAdjustment + 0.15); // Even higher reward for 16-20 mines
        }
    }

    // Reveal the entire grid showing all mines and diamonds
    private void revealGrid() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridButtons[i][j].setEnabled(false);
                Point point = new Point(i, j);
                if (mineLocations.contains(point)) {
                    gridButtons[i][j].setForeground(Color.WHITE);
                    gridButtons[i][j].setFont(new Font("Segoe UI Emoji", Font.PLAIN, 31));
                    gridButtons[i][j].setText("\uD83D\uDCA3");
                    gridButtons[i][j].setBackground(Color.RED);
                } else if (diamondLocations.contains(point)) {
                    gridButtons[i][j].setForeground(Color.WHITE);
                    gridButtons[i][j].setFont(new Font("Segoe UI Emoji", Font.PLAIN, 31));
                    gridButtons[i][j].setText("\uD83D\uDC8E");
                    gridButtons[i][j].setBackground(new Color(0, 204, 204));
                }
            }
        }
    }

    // End the game, resetting relevant variables and updating UI components
    private void endGame() {
        gameStarted = false;
        startButton.setEnabled(true);
        cashoutButton.setEnabled(false);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gridButtons[i][j].setEnabled(false);
            }
        }

        balanceLabel.setText("Balance: $" + String.format("%.2f", currentUser.getBalance()));

        updateUserData();

        profit = 0.0f;
        profitLabel.setText("Profit: $0.00");
        multiplierLabel.setText("Multiplier: 1.00");
    }

    // Update user data in the users.txt file
    private void updateUserData() {
        try {
            File userFile = new File("users.txt");
            List<String> userLines = new ArrayList<>();

            if (userFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        userLines.add(line);
                    }
                }
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(userFile))) {
                boolean userUpdated = false;

                for (String userLine : userLines) {
                    User user = User.fromString(userLine);
                    if (user.getUsername().equals(currentUser.getUsername())) {
                        writer.println(currentUser.toString());
                        userUpdated = true;
                    } else {
                        writer.println(userLine);
                    }
                }

                if (!userUpdated) {
                    writer.println(currentUser.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Play sound effect based on user action
    private void playSound(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}