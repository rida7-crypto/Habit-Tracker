package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardWindow extends JFrame {
    private int userId;

    public LeaderboardWindow(int userId) {
        this.userId = userId;
        setTitle("Habit Tracker - Leaderboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        // Column names for the leaderboard
        String[] columnNames = {"Username", "Total Habits", "Total Streaks"};

        // Fetch leaderboard data using LeaderboardManager
        LeaderboardManager leaderboardManager = new LeaderboardManager();
        List<Object[]> leaderboardData = leaderboardManager.getLeaderboardData();

        // Convert List to 2D Array for JTable
        Object[][] data = new Object[leaderboardData.size()][3];
        for (int i = 0; i < leaderboardData.size(); i++) {
            data[i] = leaderboardData.get(i);
        }

        // Create JTable and add to JScrollPane
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Back button to return to MainWindow
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose(); // Close current window
            new MainWindow(userId).setVisible(true); // Return to MainWindow
        });

        // Bottom panel for Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);

        // Add components to the window
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}