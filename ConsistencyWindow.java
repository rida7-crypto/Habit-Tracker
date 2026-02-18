package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConsistencyWindow extends JFrame {
    private int userId;

    public ConsistencyWindow(int userId) {
        this.userId = userId;
        setTitle("Habit Tracker - Habit Consistency");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Column names for consistency table
        String[] columnNames = {"Habit ID", "Habit Name", "Consistency Score"};
        Object[][] data = fetchConsistencyData(); // Fetch consistency data dynamically

        if (data.length == 0) {
            JOptionPane.showMessageDialog(this, "No consistency data available.");
        }

        // Create JTable and add to JScrollPane
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Back button to return to HabitManagerWindow
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose(); // Close current window
            new HabitManagerWindow(userId).setVisible(true); // Return to HabitManagerWindow
        });

        // Layout for Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);

        // Add components to the window
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Fetch habit consistency data using Scrollable ResultSet
    private Object[][] fetchConsistencyData() {
        Object[][] data = {};
        String query = "SELECT H.habit_id, H.habit_name, " +
                "ROUND((COUNT(P.log_date) / DATEDIFF(CURDATE(), H.start_date)) * 100, 2) AS consistency_score " +
                "FROM Habit H " +
                "LEFT JOIN ProgressLog P ON H.habit_id = P.habit_id " +
                "WHERE H.user_id = ? " +
                "AND H.start_date <= CURDATE() " +
                "AND DATEDIFF(CURDATE(), H.start_date) > 0 " +
                "GROUP BY H.habit_id, H.habit_name";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            preparedStatement.setInt(1, userId); // Set the selected user's ID
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();

            data = new Object[rowCount][3];
            int i = 0;
            while (resultSet.next()) {
                data[i][0] = resultSet.getInt("habit_id"); // Habit ID
                data[i][1] = resultSet.getString("habit_name"); // Habit Name
                data[i][2] = resultSet.getDouble("consistency_score"); // Consistency Score
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching consistency data. Please try again.");
        }
        return data;
    }
}