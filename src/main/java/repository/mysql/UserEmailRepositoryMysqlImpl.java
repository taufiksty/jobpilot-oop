package repository.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import exception.HandleException;
import model.UserEmail;
import repository.UserEmailRepository;

public class UserEmailRepositoryMysqlImpl extends MysqlRepositoryImpl implements UserEmailRepository {
    public UserEmailRepositoryMysqlImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<UserEmail> findById(String id) {
        String sql = """
                SELECT id, user_id, email, provider_id, provider_name FROM user_email WHERE id = ?;
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapRowToUserEmal(result));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserEmail> findByEmail(String email) {
        String sql = """
                SELECT id, user_id, email, provider_id, provider_name FROM user_email WHERE email = ?;
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapRowToUserEmal(result));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public UserEmail save(UserEmail userEmail) {
        if (userEmail.getId() == null) {
            userEmail.setId(UUID.randomUUID().toString());
            String sql = """
                    INSERT INTO user_email (id, user_id, email, provider_id, provider_name, created_by, updated_by)
                    VALUES (?, ?, ?, ?, ?, ?, ?);
                    """;
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, userEmail.getId());
                statement.setString(2, userEmail.getUserId());
                statement.setString(3, userEmail.getEmail());
                statement.setString(4, userEmail.getProviderId());
                statement.setString(5, userEmail.getProviderName());
                statement.setString(6, "system");
                statement.setString(7, "system");
                int affected = statement.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Insert failed");
                }
                return userEmail;
            } catch (SQLException e) {
                throw new HandleException("Insert failed");
            }
        } else {
            // Update existing userEmail
            Optional<UserEmail> existingUserEmailOpt = findById(userEmail.getId());
            if (existingUserEmailOpt.isPresent()) {
                UserEmail existingUserEmail = existingUserEmailOpt.get();
                String sql = """
                        UPDATE user_email SET email = ?, provider_id = ?, provider_name = ? WHERE id = ?;
                        """;
                try (Connection conn = dataSource.getConnection();
                        PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, userEmail.getEmail());
                    statement.setString(2, userEmail.getProviderId());
                    statement.setString(3, userEmail.getProviderName());
                    statement.setString(4, userEmail.getId());
                    int affected = statement.executeUpdate();
                    if (affected == 0) {
                        throw new SQLException("Update failed");
                    }
                } catch (SQLException e) {
                    throw new HandleException("Update failed");
                }
                return existingUserEmail;
            } else {
                throw new HandleException("UserEmail with id " + userEmail.getId() + " does not exist.");
            }
        }
    }

    // Helper

    private UserEmail mapRowToUserEmal(ResultSet result) throws SQLException {
        UserEmail userEmail = new UserEmail();
        userEmail.setId(getStringOrNull(result, "id"));
        userEmail.setUserId(getStringOrNull(result, "user_id"));
        userEmail.setEmail(getStringOrNull(result, "email"));
        userEmail.setProviderId(getStringOrNull(result, "provider_id"));
        userEmail.setProviderName(getStringOrNull(result, "provider_name"));
        userEmail.setCreatedAt(getObjectOrNull(result, "created_at", LocalDateTime.class));
        userEmail.setUpdatedAt(getObjectOrNull(result, "updated_at", LocalDateTime.class));
        userEmail.setCreatedAt(getObjectOrNull(result, "created_at", LocalDateTime.class));
        userEmail.setCreatedBy(getStringOrNull(result, "created_by"));
        userEmail.setUpdatedBy(getStringOrNull(result, "updated_by"));
        userEmail.setDeletedBy(getStringOrNull(result, "deleted_by"));
        return userEmail;
    }
}
