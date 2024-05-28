import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

// The Authentication class creates a sign-up or login window
public class Authentication extends JFrame {
    private JTextField usernameField; // Field for the user to enter their username
    private JPasswordField passwordField; // Field for the user to enter their password (hidden)
    private JTextField passwordTextField;  // TextField to show the password (visible)
    private JButton actionButton; // Button to submit the form (Sign Up or Login)
    private JButton switchButton; // Button to switch between Sign Up and Login modes
    private JCheckBox showPasswordCheckBox; // Checkbox to toggle password visibility
    private boolean isSignUp; // Flag to indicate if the form is in Sign Up mode

    // Constructor to initialize the authentication window
    public Authentication(boolean isSignUp) {
        super(isSignUp ? "Sign Up" : "Login"); // Set the window title based on the mode

        // Set the icon for the window
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

        this.isSignUp = isSignUp; // Set the mode (Sign Up or Login)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation
        setSize(550, 350); // Set the window size
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new GridBagLayout()); // Use GridBagLayout for flexible layout
        getContentPane().setBackground(new Color(45, 45, 45)); // Set the background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Set padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally

        JLabel titleLabel = new JLabel(isSignUp ? "Sign Up" : "Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set title font
        titleLabel.setForeground(Color.WHITE); // Set title color
        gbc.gridwidth = 3; // Span title across three columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE); // Set label color
        gbc.gridx = 0;
        add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setBackground(new Color(60, 63, 65)); // Set text field background color
        usernameField.setForeground(Color.WHITE); // Set text field text color
        usernameField.setCaretColor(Color.WHITE); // Set caret color
        usernameField.setPreferredSize(new Dimension(200, 25)); // Set preferred size
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(usernameField, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE); // Set label color
        gbc.gridx = 0;
        add(passwordLabel, gbc);

        JPanel passwordPanel = new JPanel(new CardLayout());
        passwordPanel.setBackground(new Color(45, 45, 45)); // Set panel background color
        passwordPanel.setPreferredSize(new Dimension(200, 25)); // Set preferred size

        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(60, 63, 65)); // Set password field background color
        passwordField.setForeground(Color.WHITE); // Set password field text color
        passwordField.setCaretColor(Color.WHITE); // Set caret color
        passwordField.setPreferredSize(new Dimension(200, 25)); // Set preferred size

        passwordTextField = new JTextField(20);
        passwordTextField.setBackground(new Color(60, 63, 65)); // Set text field background color
        passwordTextField.setForeground(Color.WHITE); // Set text field text color
        passwordTextField.setCaretColor(Color.WHITE); // Set caret color
        passwordTextField.setPreferredSize(new Dimension(200, 25)); // Set preferred size

        // Add password fields to the panel
        passwordPanel.add(passwordField, "password");
        passwordPanel.add(passwordTextField, "text");

        gbc.gridx = 1;
        add(passwordPanel, gbc);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(new Color(45, 45, 45)); // Set checkbox background color
        showPasswordCheckBox.setForeground(Color.WHITE); // Set checkbox text color
        showPasswordCheckBox.addActionListener(e -> {
            CardLayout cl = (CardLayout) (passwordPanel.getLayout());
            if (showPasswordCheckBox.isSelected()) {
                passwordTextField.setText(new String(passwordField.getPassword())); // Show password as text
                cl.show(passwordPanel, "text");
            } else {
                passwordField.setText(passwordTextField.getText()); // Hide password as asterisks
                cl.show(passwordPanel, "password");
            }
        });
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        add(showPasswordCheckBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 3;

        actionButton = createButton(isSignUp ? "Sign Up" : "Login", this::performAction);
        actionButton.setMnemonic(isSignUp ? KeyEvent.VK_U : KeyEvent.VK_L); // Set keyboard shortcut
        add(actionButton, gbc);

        gbc.gridy++;

        switchButton = createButton(isSignUp ? "Switch to Login" : "Switch to Sign Up", e -> {
            Authentication authForm = new Authentication(!isSignUp); // Switch mode
            authForm.setVisible(true);
            this.dispose(); // Close the current window
        });
        switchButton.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font
        switchButton.setMnemonic(isSignUp ? KeyEvent.VK_L : KeyEvent.VK_S); // Set keyboard shortcut
        add(switchButton, gbc);

        // Add key listener to perform action on Enter key press
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionButton.doClick(); // Trigger button click
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener); // Add key listener to username field
        passwordField.addKeyListener(enterKeyListener); // Add key listener to password field
        passwordTextField.addKeyListener(enterKeyListener); // Add key listener to text field
    }

    // Method to create and return a styled button
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(102, 102, 102)); // Set button background color
        button.setForeground(Color.WHITE); // Set button text color
        button.setFocusPainted(false); // Remove focus paint
        button.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 65))); // Set button border
        button.addActionListener(action); // Add action listener
        button.setPreferredSize(new Dimension(100, 30)); // Set preferred size
        return button;
    }

    // Method to perform the action when the button is clicked
    private void performAction(ActionEvent e) {
        // Convert the username to lowercase
        String username = usernameField.getText().trim().toLowerCase();
        String password = showPasswordCheckBox.isSelected() ? passwordTextField.getText().trim() : new String(passwordField.getPassword()).trim();
    
        // Check username and password length
        if (username.length() < 4 || password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Username must be at least 4 characters long and password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Check for empty username or password
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Check for spaces in username or password
        if (username.contains(" ") || password.contains(" ")) {
            JOptionPane.showMessageDialog(this, "Username and password cannot contain spaces.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            if (isSignUp) {
                // Save user data with lowercase username
                if (UserDataManager.saveUserData(username, password)) {
                    JOptionPane.showMessageDialog(this, "Signup successful!");
                    // Switch to login screen after successful signup
                    Authentication loginForm = new Authentication(false);
                    loginForm.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Check credentials with lowercase username
                if (UserDataManager.checkCredentials(username, password)) {
                    User loggedInUser = UserDataManager.getUser(username);
                    if (loggedInUser != null) {
                        MinesMenu minesGame = new MinesMenu(loggedInUser); // Open the Mines game menu
                        minesGame.setVisible(true);
                        this.dispose();  // Close the login window
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to retrieve user data.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error accessing the user data file.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            // hi
        }
    }
}