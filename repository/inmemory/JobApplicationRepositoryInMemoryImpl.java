package repository.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import exception.HandleException;
import model.JobApplication;
import repository.JobApplicationRepository;

public class JobApplicationRepositoryInMemoryImpl implements JobApplicationRepository {
    private final List<JobApplication> jobApplications = new ArrayList<>();

    @Override
    public Optional<JobApplication> findById(String id) {
        return jobApplications.stream().filter(jobApplication -> jobApplication.getId().equals(id)).findFirst();
    }

    @Override
    public List<JobApplication> findAll() {
        return new ArrayList<>(jobApplications);
    }

    @Override
    public List<JobApplication> findByUserId(String userId) {
        return jobApplications.stream()
                .filter(jobApplication -> jobApplication.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public JobApplication save(JobApplication jobApplication) {
        if (jobApplication.getId() == null) {
            jobApplication.setId(UUID.randomUUID().toString());
            jobApplications.add(jobApplication);
            return jobApplication;
        } else {
            // Update existing jobApplication
            Optional<JobApplication> existingJobApplicationOpt = findById(jobApplication.getId());
            if (existingJobApplicationOpt.isPresent()) {
                JobApplication existingJobApplication = existingJobApplicationOpt.get();
                existingJobApplication.setUserId(jobApplication.getUserId());
                existingJobApplication.setCompanyId(jobApplication.getCompanyId());
                existingJobApplication.setPosition(jobApplication.getPosition());
                existingJobApplication.setStatus(jobApplication.getStatus());
                existingJobApplication.setJobUrl(jobApplication.getJobUrl());
                existingJobApplication.setSource(jobApplication.getSource());
                existingJobApplication.setAppliedDateTime(jobApplication.getAppliedDateTime());
                existingJobApplication.setImportanDateTime(jobApplication.getImportanDateTime());
                existingJobApplication.setImportantNote(jobApplication.getImportantNote());
                existingJobApplication.setUpdatedAt(jobApplication.getUpdatedAt());
                existingJobApplication.setDeletedAt(jobApplication.getDeletedAt());
                existingJobApplication.setCreatedBy(jobApplication.getCreatedBy());
                existingJobApplication.setUpdatedBy(jobApplication.getUpdatedBy());
                existingJobApplication.setDeletedBy(jobApplication.getDeletedBy());
                return existingJobApplication;
            } else {
                throw new HandleException("JobApplication with id " + jobApplication.getId() + " does not exist.");
            }
        }
    }

    @Override
    public void deleteById(String id) {
        JobApplication jobApplication = findById(id)
                .orElseThrow(() -> new HandleException("JobApplication with id " + id + " does not exist."));
        jobApplications.remove(jobApplication);
    }
}
