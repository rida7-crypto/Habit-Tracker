package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;

public class AddHabitWindow extends JFrame {
    private JTextField habitNameField;
    private JComboBox<String> priorityComboBox;
    private JTextField dependentHabitField;
    private JButton addHabitButton;
    private JButton backButton;

    public AddHabitWindow(int userId, HabitManager habitManager) {
        setTitle("Add Habit");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Habit Name:"));
        habitNameField = new JTextField();
        inputPanel.add(habitNameField);

        inputPanel.add(new JLabel("Priority:"));
        String[] priorities = {"High (1)", "Medium (2)", "Low (3)"};
        priorityComboBox = new JComboBox<>(priorities);
        inputPanel.add(priorityComboBox);

        inputPanel.add(new JLabel("Dependent Habit ID:"));
        dependentHabitField = new JTextField();
        inputPanel.add(dependentHabitField);

        addHabitButton = new JButton("Add Habit");
        backButton = new JButton("Back");
        inputPanel.add(addHabitButton);
        inputPanel.add(backButton);

        add(inputPanel, BorderLayout.CENTER);

        addHabitButton.addActionListener(e -> {
            String habitName = habitNameField.getText();
            int priority = priorityComboBox.getSelectedIndex() + 1;
            String dependentHabit = dependentHabitField.getText();
            Integer dependentHabitId = dependentHabit.isEmpty() ? null : Integer.parseInt(dependentHabit);

            int habitId = habitManager.addHabit(userId, habitName, priority, dependentHabitId);
            JOptionPane.showMessageDialog(this, "Habit added successfully! ID: " + habitId);
            dispose();
        });

        backButton.addActionListener(e -> {
            dispose();
            new HabitManagerWindow(userId).setVisible(true);
        });
    }
}