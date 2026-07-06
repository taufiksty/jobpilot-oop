package config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariDataSourceProvider {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3307/jobpilot-oop");
        config.setUsername("root");
        config.setPassword("rootpassword");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Performance, reliability, and pooling settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(5000);

        dataSource = new HikariDataSource(config);
    }

    public static DataSource geDataSource() {
        return dataSource;
    }

    public static Connection geConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
