import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Authentication extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField passwordTextField;  // TextField to show the password
    private JButton actionButton;
    private JButton switchButton;
    private JCheckBox showPasswordCheckBox;
    private boolean isSignUp;

    public Authentication(boolean isSignUp) {
        super(isSignUp ? "Sign Up" : "Login");

        // icon 
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

        this.isSignUp = isSignUp;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 350);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(45, 45, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(isSignUp ? "Sign Up" : "Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setBackground(new Color(60, 63, 65));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(usernameField, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        add(passwordLabel, gbc);

        JPanel passwordPanel = new JPanel(new CardLayout());
        passwordPanel.setBackground(new Color(45, 45, 45));
        passwordPanel.setForeground(Color.WHITE);
        passwordPanel.setPreferredSize(new Dimension(200, 25));

        passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(60, 63, 65));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setPreferredSize(new Dimension(200, 25));

        passwordTextField = new JTextField(20);
        passwordTextField.setBackground(new Color(60, 63, 65));
        passwordTextField.setForeground(Color.WHITE);
        passwordTextField.setCaretColor(Color.WHITE);
        passwordTextField.setPreferredSize(new Dimension(200, 25));

        passwordPanel.add(passwordField, "password");
        passwordPanel.add(passwordTextField, "text");

        gbc.gridx = 1;
        add(passwordPanel, gbc);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(new Color(45, 45, 45));
        showPasswordCheckBox.setForeground(Color.WHITE);
        showPasswordCheckBox.addActionListener(e -> {
            CardLayout cl = (CardLayout) (passwordPanel.getLayout());
            if (showPasswordCheckBox.isSelected()) {
                passwordTextField.setText(new String(passwordField.getPassword()));
                cl.show(passwordPanel, "text");
            } else {
                passwordField.setText(passwordTextField.getText());
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
        actionButton.setMnemonic(isSignUp ? KeyEvent.VK_U : KeyEvent.VK_L); // Alt+U for Sign Up and Alt+L for Login
        add(actionButton, gbc);

        gbc.gridy++;

        switchButton = createButton(isSignUp ? "Switch to Login" : "Switch to Sign Up", e -> {
            Authentication authForm = new Authentication(!isSignUp);
            authForm.setVisible(true);
            this.dispose();
        });
        switchButton.setFont(new Font("Arial", Font.PLAIN, 12));
        switchButton.setMnemonic(isSignUp ? KeyEvent.VK_L : KeyEvent.VK_S); // Alt+L for Switch to Login and Alt+S for Switch to Sign Up
        add(switchButton, gbc);

        // Add key listener to perform action on Enter key press
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionButton.doClick();
                }
            }
        };

        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
        passwordTextField.addKeyListener(enterKeyListener);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(102, 102, 102));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 65)));
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    private void performAction(ActionEvent e) {
        String username = usernameField.getText().trim();
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
                if (UserDataManager.saveUserData(username, password)) {
                    JOptionPane.showMessageDialog(this, "Signup successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (UserDataManager.checkCredentials(username, password)) {
                    User loggedInUser = UserDataManager.getUser(username);
                    if (loggedInUser != null) {
                        MinesMenu minesGame = new MinesMenu(loggedInUser);
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
        }
    }    

    public static void main(String[] args) {
        Authentication authForm = new Authentication(false); // Start with login
        SwingUtilities.invokeLater(() -> authForm.setVisible(true));
    }
}