package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewHabitWindow extends JFrame {
    private int userId;

    public ViewHabitWindow(int userId) {
        this.userId = userId;
        setTitle("Habit Tracker - View Habits");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Column names for the habit table
        String[] columnNames = {"Habit ID", "Habit Name", "Start Date", "Priority", "Streak"};
        Object[][] data = fetchHabitData(userId); // Fetch habit data dynamically

        // Create JTable and add to JScrollPane
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Back button for navigation
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            new HabitManagerWindow(userId).setVisible(true);
        });

        // Bottom panel for Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);

        // Add components to the window
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Fetch habit data from the database
    private Object[][] fetchHabitData(int userId) {
        Object[][] data = {};
        String query = "SELECT habit_id, habit_name, start_date, priority, streak FROM Habit WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query,
                     ResultSet.TYPE_SCROLL_INSENSITIVE, // Enable scrolling
                     ResultSet.CONCUR_READ_ONLY // Read-only
             )) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.last(); // Move to the last row
            int rowCount = resultSet.getRow(); // Get row count
            resultSet.beforeFirst(); // Reset cursor

            data = new Object[rowCount][5]; // Update array size to include Start Date
            int i = 0;
            while (resultSet.next()) {
                data[i][0] = resultSet.getInt("habit_id"); // Habit ID
                data[i][1] = resultSet.getString("habit_name"); // Habit Name
                data[i][2] = resultSet.getDate("start_date"); // Start Date
                data[i][3] = resultSet.getInt("priority"); // Priority
                data[i][4] = resultSet.getInt("streak"); // Streak
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching habit data. Please try again.");
        }
        return data;
    }
}