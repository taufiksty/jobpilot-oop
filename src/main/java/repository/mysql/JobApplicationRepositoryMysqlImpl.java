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

import enums.JobApplicationStatus;
import exception.HandleException;
import model.JobApplication;
import repository.JobApplicationRepository;

public class JobApplicationRepositoryMysqlImpl extends MysqlRepositoryImpl implements JobApplicationRepository {

    public JobApplicationRepositoryMysqlImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Optional<JobApplication> findById(String id) {
        String sql = """
                SELECT id, user_id, company_id, position, status, source, applied_date_time FROM job_application WHERE id = ?;
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapRowToJobApplication(result));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<JobApplication> findAll() {
        List<JobApplication> jobApplications = new ArrayList<>();
        String sql = """
                SELECT id, user_id, company_id, position, status, source, applied_date_time FROM job_application WHERE id = ?;
                """;
        try (Connection conn = dataSource.getConnection();
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                jobApplications.add(mapRowToJobApplication(result));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobApplications;
    }

    @Override
    public List<JobApplication> findByUserId(String userId) {
        List<JobApplication> jobApplications = new ArrayList<>();
        String sql = """
                SELECT id, user_id, company_id, position, status, source, applied_date_time FROM job_application WHERE user_id = ?;
                """;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    jobApplications.add(mapRowToJobApplication(result));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobApplications;
    }

    @Override
    public JobApplication save(JobApplication jobApplication) {
        if (jobApplication.getId() == null) {
            jobApplication.setId(UUID.randomUUID().toString());
            String sql = """
                    INSERT INTO job_application (id, user_id, company_id, position, status, source, applied_date_time, created_by, updated_by)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                    """;
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, jobApplication.getId());
                statement.setString(2, jobApplication.getUserId());
                statement.setString(3, jobApplication.getCompanyId());
                statement.setString(4, jobApplication.getPosition());
                statement.setString(5, jobApplication.getStatus().toString());
                statement.setString(6, jobApplication.getSource());
                statement.setObject(7, jobApplication.getAppliedDateTime());
                statement.setString(8, "system");
                statement.setString(9, "system");
                int affected = statement.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Insert failed");
                }
                return jobApplication;
            } catch (SQLException e) {
                throw new HandleException("Insert failed");
            }
        } else {
            // Update existing jobApplication
            Optional<JobApplication> existingJobApplicationOpt = findById(jobApplication.getId());
            if (existingJobApplicationOpt.isPresent()) {
                JobApplication existingJobApplication = existingJobApplicationOpt.get();
                String sql = """
                        UPDATE job_application SET company_id = ?, position = ?, status = ?, source = ? WHERE id = ?;
                        """;
                try (Connection conn = dataSource.getConnection();
                        PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, jobApplication.getCompanyId());
                    statement.setString(2, jobApplication.getPosition());
                    statement.setString(3, jobApplication.getStatus().toString());
                    statement.setString(4, jobApplication.getSource());
                    statement.setString(5, jobApplication.getId());
                    int affected = statement.executeUpdate();
                    if (affected == 0) {
                        throw new SQLException("Update failed");
                    }
                } catch (SQLException e) {
                    throw new HandleException("Update failed");
                }
                return existingJobApplication;
            } else {
                throw new HandleException("JobApplication with id " + jobApplication.getId() + " does not exist.");
            }
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = """
                DELETE FROM job_application WHERE id = ?;
                """;
        try (Connection conn = dataSource.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper

    private JobApplication mapRowToJobApplication(ResultSet result) throws SQLException {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setId(getStringOrNull(result, "id"));
        jobApplication.setUserId(getStringOrNull(result, "user_id"));
        jobApplication.setCompanyId(getStringOrNull(result, "company_id"));
        jobApplication.setPosition(getStringOrNull(result, "position"));
        jobApplication.setStatus(JobApplicationStatus.valueOf(result.getString("status")));
        jobApplication.setJobUrl(getStringOrNull(result, "job_url"));
        jobApplication.setSource(getStringOrNull(result, "source"));
        jobApplication.setAppliedDateTime(getObjectOrNull(result, "applied_date_time", LocalDateTime.class));
        jobApplication.setImportanDateTime(getObjectOrNull(result, "important_date_time", LocalDateTime.class));
        jobApplication.setImportantNote(getStringOrNull(result, "important_note"));
        jobApplication.setCreatedAt(getObjectOrNull(result, "created_at", LocalDateTime.class));
        jobApplication.setUpdatedAt(getObjectOrNull(result, "updated_at", LocalDateTime.class));
        jobApplication.setCreatedAt(getObjectOrNull(result, "created_at", LocalDateTime.class));
        jobApplication.setCreatedBy(getStringOrNull(result, "created_by"));
        jobApplication.setUpdatedBy(getStringOrNull(result, "updated_by"));
        jobApplication.setDeletedBy(getStringOrNull(result, "deleted_by"));
        return jobApplication;
    }
}
