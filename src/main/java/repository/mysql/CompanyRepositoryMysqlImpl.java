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
import model.Company;
import repository.CompanyRepository;

public class CompanyRepositoryMysqlImpl extends MysqlRepositoryImpl implements CompanyRepository {

    public CompanyRepositoryMysqlImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<Company> findById(String id) {
        String sql = """
                SELECT id, name FROM company WHERE id = ?
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapRowToCompany(result));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Company> findByName(String name) {
        String sql = """
                SELECT id, name FROM company WHERE name LIKE ?
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapRowToCompany(result));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Company save(Company company) {
        if (company.getId() == null) {
            company.setId(UUID.randomUUID().toString());
            String sql = """
                    INSERT INTO company (id, name, created_by, updated_by)
                    VALUES (?, ?, ?, ?);
                    """;
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, company.getId());
                statement.setString(2, company.getName());
                statement.setString(3, "system");
                statement.setString(4, "system");
                int affected = statement.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Insert failed");
                }
                return company;
            } catch (SQLException e) {
                throw new HandleException("Insert failed");
            }
        } else {
            // Update existing company
            Optional<Company> existingCompanyOpt = findById(company.getId());
            if (existingCompanyOpt.isPresent()) {
                Company existingCompany = existingCompanyOpt.get();
                String sql = """
                        UPDATE company SET name = ? WHERE id = ?;
                        """;
                try (Connection conn = dataSource.getConnection();
                        PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, company.getName());
                    statement.setString(2, company.getId());
                    int affected = statement.executeUpdate();
                    if (affected == 0) {
                        throw new SQLException("Update failed");
                    }
                } catch (SQLException e) {
                    throw new HandleException("Update failed");
                }
                return existingCompany;
            } else {
                throw new HandleException("Company with id " + company.getId() + " does not exist.");
            }
        }
    }

    // Helper

    private Company mapRowToCompany(ResultSet result) throws SQLException {
        Company company = new Company();
        company.setId(getStringOrNull(result, "id"));
        company.setName(getStringOrNull(result, "name"));
        company.setCreatedAt(getObjectOrNull(result, "created_at", LocalDateTime.class));
        company.setUpdatedAt(getObjectOrNull(result, "updated_at", LocalDateTime.class));
        company.setCreatedAt(getObjectOrNull(result, "created_at", LocalDateTime.class));
        company.setCreatedBy(getStringOrNull(result, "created_by"));
        company.setUpdatedBy(getStringOrNull(result, "updated_by"));
        company.setDeletedBy(getStringOrNull(result, "deleted_by"));
        return company;
    }

}
