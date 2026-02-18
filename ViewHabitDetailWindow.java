package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewHabitDetailWindow extends JFrame {
    private int habitId;

    public ViewHabitDetailWindow(int habitId) {
        this.habitId = habitId;
        setTitle("Habit Tracker - Habit Details");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Fetch habit details dynamically
        Object[] habitDetails = fetchHabitDetails(habitId);

        if (habitDetails == null) {
            JOptionPane.showMessageDialog(this, "No details available for the selected habit.");
        }

        JPanel detailPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        detailPanel.add(new JLabel("Habit ID: " + habitDetails[0]));
        detailPanel.add(new JLabel("Habit Name: " + habitDetails[1]));
        detailPanel.add(new JLabel("Start Date: " + habitDetails[2]));
        detailPanel.add(new JLabel("Priority: " + habitDetails[3]));
        detailPanel.add(new JLabel("Streak: " + habitDetails[4]));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose(); // Close current window
            new ViewHabitWindow((Integer) habitDetails[5]).setVisible(true); // Return to ViewHabitWindow
        });

        add(detailPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    // Fetch habit details from the database
    private Object[] fetchHabitDetails(int habitId) {
        Object[] habitDetails = null;
        String query = "SELECT habit_id, habit_name, start_date, priority, streak, user_id FROM Habit WHERE habit_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, habitId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                habitDetails = new Object[6];
                habitDetails[0] = resultSet.getInt("habit_id"); // Habit ID
                habitDetails[1] = resultSet.getString("habit_name"); // Habit Name
                habitDetails[2] = resultSet.getDate("start_date"); // Start Date
                habitDetails[3] = resultSet.getInt("priority"); // Priority
                habitDetails[4] = resultSet.getInt("streak"); // Streak
                habitDetails[5] = resultSet.getInt("user_id"); // User ID
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching habit details. Please try again.");
        }
        return habitDetails;
    }
}