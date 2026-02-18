package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;

public class RegistrationWindow extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegistrationWindow() {
        setTitle("Habit Tracker - Register");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window only
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        registerButton = new JButton("Register");
        inputPanel.add(new JLabel()); // Empty cell for layout
        inputPanel.add(registerButton);

        add(inputPanel, BorderLayout.CENTER);

        // Button Listener
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            UserManager userManager = new UserManager();
            int userId = userManager.addUser(username, email, password);
            if (userId != -1) {
                JOptionPane.showMessageDialog(this, "Registration successful! Your User ID: " + userId);
                dispose(); // Close the registration window
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
            }
        });
    }
}