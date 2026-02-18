package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;

public class HabitManagerWindow extends JFrame {
    private JButton addHabitButton;
    private JButton viewHabitsButton;
    private JButton deleteHabitButton;
    private JButton viewConsistencyButton;
    private JButton rankHabitsButton;
    private JButton backButton;

    public HabitManagerWindow(int userId) {
        setTitle("Habit Tracker - Habit Management");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Initialize buttons
        addHabitButton = new JButton("Add Habit");
        viewHabitsButton = new JButton("View Habits");
        deleteHabitButton = new JButton("Delete Habit");
        viewConsistencyButton = new JButton("View Consistency");
        rankHabitsButton = new JButton("Rank Habits");
        backButton = new JButton("Back");

        // Add buttons to the layout
        add(addHabitButton);
        add(viewHabitsButton);
        add(deleteHabitButton);
        add(viewConsistencyButton);
        add(rankHabitsButton);
        add(backButton);

        HabitManager habitManager = new HabitManager();

        // Add Habit functionality
        addHabitButton.addActionListener(e -> {
            new AddHabitWindow(userId, habitManager).setVisible(true);
        });

        // View Habits functionality
        viewHabitsButton.addActionListener(e -> {
            new ViewHabitWindow(userId).setVisible(true);
        });

        // Delete Habit functionality
        deleteHabitButton.addActionListener(e -> {
            String habitId = JOptionPane.showInputDialog(this, "Enter Habit ID to delete:");
            if (habitId != null) {
                habitManager.deleteHabit(Integer.parseInt(habitId));
            }
        });

        // View Consistency functionality
        viewConsistencyButton.addActionListener(e -> {
            new ConsistencyWindow(userId).setVisible(true);
        });

        // Rank Habits functionality
        rankHabitsButton.addActionListener(e -> {
            new RankHabitsWindow(userId).setVisible(true);
        });

        // Back to Main Menu
        backButton.addActionListener(e -> {
            dispose();
            new MainWindow(userId).setVisible(true);
        });
    }
}