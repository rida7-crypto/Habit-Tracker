package HabitTracker_Project;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotifyUserWindow extends JFrame {
    private JTextArea notificationArea; // Text area to display notifications
    private NotificationService notificationService; // Service for fetching notifications

    public NotifyUserWindow() {
        setTitle("Habit Tracker - Notifications");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        notificationService = new NotificationService();

        // Create UI components
        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        notificationArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(notificationArea);

        JButton fetchButton = new JButton("Fetch Notifications");
        fetchButton.addActionListener(e -> refreshNotifications());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(fetchButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Schedule automatic notification fetching every hour
        scheduleNotificationFetch();
    }

    // Method to refresh notifications dynamically
    private void refreshNotifications() {
        List<String> notifications = notificationService.fetchNotifications();
        if (notifications.isEmpty()) {
            notificationArea.setText("No notifications to display.");
        } else {
            notificationArea.setText(String.join("\n", notifications));
        }
    }

    // Schedule automatic fetching of notifications
    private void scheduleNotificationFetch() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> refreshNotifications()); // Update UI on the Event Dispatch Thread
            }
        };
        // Schedule task to run every hour (3600000 milliseconds)
        timer.scheduleAtFixedRate(task, 0, 3600000);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NotifyUserWindow window = new NotifyUserWindow();
            window.setVisible(true);
        });
    }
}