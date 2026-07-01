package repository;

import java.util.List;
import java.util.Optional;

import model.JobApplication;

public interface JobApplicationRepository {
    Optional<JobApplication> findById(int id);

    List<JobApplication> findAll();

    List<JobApplication> findByUserId(int userId);

    JobApplication save(JobApplication jobApplication);

    void deleteById(int id);
}
