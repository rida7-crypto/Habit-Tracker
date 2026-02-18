package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {
    private int userId; // Store the authenticated user's ID

    public MainWindow(int userId) {
        this.userId = userId; // Assign userId for personalized functionality
        setTitle("Habit Tracker - Main Menu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton habitManagerButton = new JButton("Manage Habits");
        JButton progressLoggerButton = new JButton("Log Progress");
        JButton leaderboardButton = new JButton("View Leaderboard");
        JButton notificationsButton = new JButton("View Notifications"); // New button for notifications
        JButton deleteUserButton = new JButton("Delete Account"); // Button for user deletion
        JButton logoutButton = new JButton("Logout");

        menuPanel.add(habitManagerButton);
        menuPanel.add(progressLoggerButton);
        menuPanel.add(leaderboardButton);
        menuPanel.add(notificationsButton); // Add Notifications button
        menuPanel.add(deleteUserButton); // Add Delete Account button
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.CENTER);

        // Button listeners

        habitManagerButton.addActionListener((ActionEvent e) -> {
            new HabitManagerWindow(userId).setVisible(true);
        });

        progressLoggerButton.addActionListener((ActionEvent e) -> {
            String habitId = JOptionPane.showInputDialog(this, "Enter Habit ID to log progress:");
            if (habitId != null) {
                ProgressLogger progressLogger = new ProgressLogger();
                progressLogger.logProgress(Integer.parseInt(habitId));
                JOptionPane.showMessageDialog(this, "Progress logged successfully!");
            }
        });

        leaderboardButton.addActionListener(e -> {
            new LeaderboardWindow(userId).setVisible(true);
        });

        notificationsButton.addActionListener((ActionEvent e) -> {
            new NotifyUserWindow().setVisible(true); // Open the notification window
        });

        deleteUserButton.addActionListener((ActionEvent e) -> {
            // Confirm before deleting the user account
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete your account? This action is irreversible.",
                    "Delete Account Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation == JOptionPane.YES_OPTION) {
                UserManager userManager = new UserManager();
                userManager.deleteUser(userId); // Call deleteUser method
                JOptionPane.showMessageDialog(this, "Your account has been deleted. Goodbye!");
                dispose(); // Close the main window
                new LoginWindow().setVisible(true); // Redirect to login window
            }
        });

        logoutButton.addActionListener((ActionEvent e) -> {
            dispose(); // Close the main window
            new LoginWindow().setVisible(true); // Return to login window
        });
    }
}