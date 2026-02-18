package HabitTracker_Project;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/habittracker";
    private static final String USER = "root";
    private static final String PASSWORD = "RIda#123";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully!");
        } catch (Exception e) {
            System.err.println("Failed to connect to the database. Please check the connection details.");
            e.printStackTrace();
        }
        return connection;
    }
}