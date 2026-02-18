package HabitTracker_Project;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    // Method to fetch reminders for habits with a streak of 0
    public List<String> fetchNotifications() {
        List<String> notifications = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement callableStatement = connection.prepareCall("{CALL notify_user()}")) {

            boolean hasResults = callableStatement.execute();
            while (hasResults) {
                try (ResultSet resultSet = callableStatement.getResultSet()) {
                    while (resultSet.next()) {
                        notifications.add(resultSet.getString(1)); // Reminder message
                    }
                }
                hasResults = callableStatement.getMoreResults();
            }
        } catch (Exception e) {
            e.printStackTrace();
            notifications.add("Error fetching notifications: " + e.getMessage());
        }
        return notifications;
    }
}