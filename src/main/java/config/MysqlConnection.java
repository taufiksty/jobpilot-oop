package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exception.HandleException;

public class MysqlConnection {
    private static String URL = "jdbc:mysql://localhost:3307/jobpilot-oop";
    private static String USER = "root";
    private static String PASSWORD = "rootpassword";

    // Load explicitly
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new HandleException("MySQL JDBC driver not found");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
