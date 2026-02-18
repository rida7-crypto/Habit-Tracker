package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RankHabitsWindow extends JFrame {
    private int userId;

    public RankHabitsWindow(int userId) {
        this.userId = userId;
        setTitle("Habit Tracker - Ranked Habits");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Column names for ranked habits table
        String[] columnNames = {"Habit ID", "Habit Name", "Priority", "Consistency Score", "Streak", "Habit Score"};
        Object[][] data = fetchRankedHabitsData(); // Fetch ranked habits dynamically

        // Create JTable and add to JScrollPane
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Back button to return to HabitManagerWindow
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose(); // Close current window
            new HabitManagerWindow(userId).setVisible(true); // Return to HabitManagerWindow
        });

        // Bottom panel for Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);

        // Add components to the window
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Fetch ranked habits data using Scrollable ResultSet
    private Object[][] fetchRankedHabitsData() {
        Object[][] data = {};
        String query = "SELECT H.habit_id, H.habit_name, H.priority, " +
                "ROUND((COUNT(P.log_date) / DATEDIFF(CURDATE(), H.start_date)) * 100, 2) AS consistency_score, " +
                "H.streak, " +
                "ROUND(((H.streak * 2) + (H.priority * 3) + " +
                "(COUNT(P.log_date) / DATEDIFF(CURDATE(), H.start_date)) * 100) / 3, 2) AS habit_score " +
                "FROM Habit H " +
                "LEFT JOIN ProgressLog P ON H.habit_id = P.habit_id " +
                "WHERE H.user_id = ? " +
                "GROUP BY H.habit_id " +
                "ORDER BY habit_score DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     query,
                     ResultSet.TYPE_SCROLL_INSENSITIVE, // Enable scrolling
                     ResultSet.CONCUR_READ_ONLY // Read-only
             )) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.last(); // Move to the last row
            int rowCount = resultSet.getRow(); // Get the row count
            resultSet.beforeFirst(); // Reset cursor to the beginning

            data = new Object[rowCount][6];
            int i = 0;
            while (resultSet.next()) {
                data[i][0] = resultSet.getInt("habit_id"); // Habit ID
                data[i][1] = resultSet.getString("habit_name"); // Habit Name
                data[i][2] = resultSet.getInt("priority"); // Priority
                data[i][3] = resultSet.getDouble("consistency_score"); // Consistency Score
                data[i][4] = resultSet.getInt("streak"); // Streak
                data[i][5] = resultSet.getDouble("habit_score"); // Habit Score
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching ranked habits data. Please try again.");
        }
        return data;
    }
}