package repository.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import exception.HandleException;
import model.User;
import repository.UserRepository;

public class UserRepositoryMysqlImpl extends MysqlRepositoryImpl implements UserRepository {

    public UserRepositoryMysqlImpl(DataSource dataSource) {
        super(dataSource);
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = """
                SELECT id, name, last_login_at, created_at FROM user;
                """;
        try (Connection conn = dataSource.getConnection();
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                users.add(mapRowToUser(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Optional<User> findById(String id) {
        String sql = """
                SELECT id, name, password, last_login_at, created_at FROM user WHERE id = ?;
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapRowToUser(result));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
            String sql = """
                    INSERT INTO user (id, name, password, last_login_at, created_by, updated_by)
                    VALUES (?, ?, ?, ?, ?, ?);
                    """;
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, user.getId());
                statement.setString(2, user.getName());
                statement.setString(3, user.getPassword());
                statement.setObject(4, user.getLastLoginAt());
                statement.setString(5, "system");
                statement.setString(6, "system");
                int affected = statement.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Insert failed");
                }
                return user;
            } catch (SQLException e) {
                throw new HandleException("Insert failed");
            }
        } else {
            // Update existing user
            Optional<User> existingUserOpt = findById(user.getId());
            if (existingUserOpt.isPresent()) {
                User existingUser = existingUserOpt.get();
                String sql = """
                        UPDATE user SET name = ?, password = ?, last_login_at = ?, updated_by = ? WHERE id = ?;
                        """;
                try (Connection conn = dataSource.getConnection();
                        PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, user.getName());
                    statement.setString(2, user.getPassword());
                    statement.setObject(3, user.getLastLoginAt());
                    statement.setString(4, user.getId());
                    statement.setString(5, user.getId());
                    int affected = statement.executeUpdate();
                    if (affected == 0) {
                        throw new SQLException("Update failed");
                    }
                } catch (SQLException e) {
                    throw new HandleException("Update failed");
                }
                return existingUser;
            } else {
                throw new HandleException("User with id " + user.getId() + " does not exist.");
            }
        }
    }

    // Helper

    private User mapRowToUser(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(getStringOrNull(result, "id"));
        user.setName(getStringOrNull(result, "name"));
        user.setPassword(getStringOrNull(result, "password"));
        user.setLastLoginAt(getObjectOrNull(result, "last_login_at", LocalDateTime.class));
        user.setCreatedAt(getObjectOrNull(result, "created_at", LocalDateTime.class));
        user.setUpdatedAt(getObjectOrNull(result, "updated_at", LocalDateTime.class));
        user.setCreatedAt(getObjectOrNull(result, "created_at", LocalDateTime.class));
        user.setCreatedBy(getStringOrNull(result, "created_by"));
        user.setUpdatedBy(getStringOrNull(result, "updated_by"));
        user.setDeletedBy(getStringOrNull(result, "deleted_by"));
        return user;
    }
}
