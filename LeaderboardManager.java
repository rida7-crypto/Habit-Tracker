package HabitTracker_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardManager {

    // Method to fetch leaderboard data
    public List<Object[]> getLeaderboardData() {
        String query = "SELECT U.username AS user_name, " +
                "COUNT(H.habit_id) AS total_habits, " +
                "SUM(H.streak) AS total_streaks " +
                "FROM User U " +
                "JOIN Habit H ON U.user_id = H.user_id " +
                "GROUP BY U.user_id " +
                "ORDER BY total_streaks DESC, total_habits DESC " +
                "LIMIT 10";

        List<Object[]> leaderboard = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("user_name");
                int totalHabits = resultSet.getInt("total_habits");
                int totalStreaks = resultSet.getInt("total_streaks");
                leaderboard.add(new Object[]{username, totalHabits, totalStreaks});
            }

        } catch (Exception e) {
            System.err.println("Error while retrieving leaderboard data.");
            e.printStackTrace();
        }

        return leaderboard;
    }
}