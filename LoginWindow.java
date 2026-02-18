package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginWindow() {
        setTitle("Habit Tracker - Login");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        inputPanel.add(loginButton);
        inputPanel.add(registerButton);

        add(inputPanel, BorderLayout.CENTER);

        // Button listeners
        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            UserManager userManager = new UserManager();
            int userId = userManager.loginUser(username, password);
            if (userId != -1) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new MainWindow(userId).setVisible(true); // Pass userId to MainWindow
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
            }
        });

        registerButton.addActionListener(e -> {
            new RegistrationWindow().setVisible(true);
        });
    }
}