package repository;

import java.util.List;
import java.util.Optional;

import model.JobApplication;

public interface JobApplicationRepository {
    Optional<JobApplication> findById(String id);

    List<JobApplication> findAll();

    List<JobApplication> findByUserId(String userId);

    JobApplication save(JobApplication jobApplication);

    void deleteById(String id);
}
