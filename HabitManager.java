package HabitTracker_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HabitManager {

    // Add a Habit with Priority and Dependency
    public int addHabit(int userId, String habitName, int priority, Integer dependentHabitId) {
        String query = "INSERT INTO Habit (user_id, habit_name, start_date, priority, dependent_habit_id) VALUES (?, ?, CURDATE(), ?, ?)";
        int habitId = -1;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, habitName);
            preparedStatement.setInt(3, priority);
            if (dependentHabitId != null) {
                preparedStatement.setInt(4, dependentHabitId);
            } else {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                habitId = generatedKeys.getInt(1);
                System.out.println("Habit added successfully! Generated Habit ID: " + habitId);
            }

        } catch (Exception e) {
            System.err.println("Error while adding habit.");
            e.printStackTrace();
        }

        return habitId;
    }

    // View Habits
    public void viewHabits(int userId) {
        String query = "SELECT * FROM Habit WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("User Habits:");
            while (resultSet.next()) {
                System.out.println("Habit ID: " + resultSet.getInt("habit_id"));
                System.out.println("Habit Name: " + resultSet.getString("habit_name"));
                System.out.println("Priority: " + resultSet.getInt("priority"));
                System.out.println("Dependent Habit ID: " + resultSet.getInt("dependent_habit_id"));
                System.out.println("Streak: " + resultSet.getInt("streak"));
                System.out.println("-------------------------------");
            }

        } catch (Exception e) {
            System.err.println("Error while retrieving habits.");
            e.printStackTrace();
        }
    }

    // Calculate Habit Consistency
    public void calculateConsistency() {
        String query = "SELECT " +
                "    H.habit_id, " +
                "    H.habit_name, " +
                "    ROUND((COUNT(P.log_date) / DATEDIFF(CURDATE(), H.start_date)) * 100, 2) AS consistency_score " +
                "FROM " +
                "    Habit H " +
                "LEFT JOIN " +
                "    ProgressLog P ON H.habit_id = P.habit_id " +
                "WHERE " +
                "    DATEDIFF(CURDATE(), H.start_date) > 0 " + // Avoid division by zero
                "GROUP BY " +
                "    H.habit_id, H.habit_name";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("Habit Consistency Scores:");
            System.out.println("-------------------------------");
            while (resultSet.next()) {
                int habitId = resultSet.getInt("habit_id");
                String habitName = resultSet.getString("habit_name");
                double consistencyScore = resultSet.getDouble("consistency_score");

                System.out.println("Habit ID: " + habitId);
                System.out.println("Habit Name: " + habitName);
                System.out.println("Consistency Score: " + consistencyScore + "%");
                System.out.println("-------------------------------");
            }

        } catch (Exception e) {
            System.err.println("Error while calculating habit consistency scores.");
            e.printStackTrace();
        }
    }

    // Rank Habits
    public void rankHabits(int userId) {
        String query = "SELECT H.habit_id, H.habit_name, H.priority, " +
                "ROUND((COUNT(P.log_date) / DATEDIFF(CURDATE(), H.start_date)) * 100, 2) AS consistency_score, " +
                "H.streak, " +
                "ROUND(((H.streak * 2) + (H.priority * 3) + (COUNT(P.log_date) / DATEDIFF(CURDATE(), H.start_date)) * 100) / 3, 2) AS habit_score " +
                "FROM Habit H " +
                "LEFT JOIN ProgressLog P ON H.habit_id = P.habit_id " +
                "WHERE H.user_id = ? " +
                "GROUP BY H.habit_id " +
                "ORDER BY habit_score DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Habit Rankings:");
            System.out.println("-------------------------------");
            while (resultSet.next()) {
                int habitId = resultSet.getInt("habit_id");
                String habitName = resultSet.getString("habit_name");
                int priority = resultSet.getInt("priority");
                double consistencyScore = resultSet.getDouble("consistency_score");
                int streak = resultSet.getInt("streak");
                double habitScore = resultSet.getDouble("habit_score");

                System.out.println("Habit ID: " + habitId);
                System.out.println("Habit Name: " + habitName);
                System.out.println("Priority: " + priority);
                System.out.println("Consistency Score: " + consistencyScore + "%");
                System.out.println("Streak: " + streak);
                System.out.println("Habit Score: " + habitScore);
                System.out.println("-------------------------------");
            }

        } catch (Exception e) {
            System.err.println("Error while ranking habits.");
            e.printStackTrace();
        }
    }

    // Delete a Habit
    public void deleteHabit(int habitId) {
        String query = "DELETE FROM Habit WHERE habit_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, habitId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Habit deleted successfully!");
            } else {
                System.out.println("No habit found with the given ID.");
            }

        } catch (Exception e) {
            System.err.println("Error while deleting habit.");
            e.printStackTrace();
        }
    }
}