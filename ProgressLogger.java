package HabitTracker_Project;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProgressLogger {

    // Log Progress and Add Notification if Needed
    public void logProgress(int habitId) {
        String progressQuery = "INSERT INTO ProgressLog (habit_id, log_date) VALUES (?, CURDATE())";
        String notificationQuery = "INSERT INTO HabitNotifications (habit_id, dependent_habit_name) " +
                "SELECT ?, habit_name FROM Habit WHERE habit_id = (SELECT dependent_habit_id FROM Habit WHERE habit_id = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement progressStmt = connection.prepareStatement(progressQuery);
             PreparedStatement notificationStmt = connection.prepareStatement(notificationQuery)) {

            // Log progress
            progressStmt.setInt(1, habitId);
            progressStmt.executeUpdate();
            System.out.println("Progress logged successfully!");

            // Log dependent habit notification if applicable
            notificationStmt.setInt(1, habitId);
            notificationStmt.setInt(2, habitId);
            notificationStmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error while logging progress.");
            e.printStackTrace();
        }
    }

    // View Notifications
    public void viewNotifications(int habitId) {
        String query = "SELECT dependent_habit_name, notification_time FROM HabitNotifications WHERE habit_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, habitId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Habit Notifications:");
            while (resultSet.next()) {
                String dependentHabitName = resultSet.getString("dependent_habit_name");
                String notificationTime = resultSet.getString("notification_time");

                System.out.println("Dependent Habit: " + dependentHabitName);
                System.out.println("Notification Time: " + notificationTime);
                System.out.println("-------------------------------");
            }

        } catch (Exception e) {
            System.err.println("Error while retrieving notifications.");
            e.printStackTrace();
        }
    }
}