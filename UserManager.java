package HabitTracker_Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserManager {

    // Add a User with validation for empty fields
    public int addUser(String username, String email, String password) {
        if (username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            System.err.println("Error: Username, email, and password cannot be empty.");
            return -1;
        }

        String query = "INSERT INTO User (username, email, password) VALUES (?, ?, SHA2(?, 256))";
        int userId = -1;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
                System.out.println("User registered successfully! Generated User ID: " + userId);
            }

        } catch (Exception e) {
            System.err.println("Error while registering user. Please ensure the username and email are unique.");
            e.printStackTrace();
        }

        return userId;
    }

    // Login a User with better error messages
    public int loginUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.err.println("Error: Username and password cannot be empty.");
            return -1;
        }

        String query = "SELECT user_id FROM User WHERE username = ? AND password = SHA2(?, 256)";
        int userId = -1;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("user_id");
                System.out.println("Login successful! Welcome, " + username);
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }

        } catch (Exception e) {
            System.err.println("Error during login. Please contact support.");
            e.printStackTrace();
        }

        return userId;
    }

    // Delete a User with confirmation of cascading deletions
    public void deleteUser(int userId) {
        if (userId <= 0) {
            System.err.println("Error: Invalid User ID.");
            return;
        }

        String query = "DELETE FROM User WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User and associated data deleted successfully!");
            } else {
                System.out.println("No user found with the given ID.");
            }

        } catch (Exception e) {
            System.err.println("Error while deleting user. Please contact support.");
            e.printStackTrace();
        }
    }
}