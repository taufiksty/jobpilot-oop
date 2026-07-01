package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import dto.JobApplicationDto;
import enums.JobApplicationStatus;
import exception.HandleException;
import model.Company;
import model.JobApplication;
import repository.CompanyRepository;
import repository.JobApplicationRepository;

public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyRepository companyRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
            CompanyRepository companyRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.companyRepository = companyRepository;
    }

    public JobApplicationDto addJobApplication(int userId, String companyName, String position,
            JobApplicationStatus status,
            String source) {
        Company company = findOrCreateCompany(companyName);

        JobApplication jobApplication = new JobApplication(
                userId,
                company.getId(),
                position,
                status,
                null, // jobUrl
                source,
                status.equals(JobApplicationStatus.APPLIED) ? LocalDateTime.now() : null, // appliedDateTime
                null, // importanDateTime
                null // importantNote
        );

        JobApplication saved = jobApplicationRepository.save(jobApplication);
        return toDto(saved);
    }

    public List<JobApplicationDto> getMyJobApplications(int userId) {
        return jobApplicationRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public JobApplicationDto getJobApplicationDetail(int id) {
        JobApplication jobApplication = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new HandleException("Job application with id " + id + " not found."));
        return toDto(jobApplication);
    }

    public JobApplicationDto updateJobApplication(int id, int userId, String companyName, String position,
            JobApplicationStatus status, String source) {
        JobApplication existing = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new HandleException("Job application with id " + id + " not found."));

        Company company = findOrCreateCompany(companyName);

        existing.setUserId(userId);
        existing.setCompanyId(company.getId());
        existing.setPosition(position);
        existing.setStatus(status);
        existing.setSource(source);

        JobApplication updated = jobApplicationRepository.save(existing);
        return toDto(updated);
    }

    public void deleteJobApplication(int id) {
        jobApplicationRepository.deleteById(id);
    }

    // ── Helpers ──

    private Company findOrCreateCompany(String companyString) {
        Company company = companyRepository.findByName(companyString).orElseGet(() -> {
            Company newCompany = new Company(companyString);
            return companyRepository.save(newCompany);
        });
        return company;
    }

    private JobApplicationDto toDto(JobApplication jobApplication) {
        Company company = companyRepository.findById(jobApplication.getCompanyId())
                .orElse(new Company("Unknown"));
        return new JobApplicationDto(jobApplication, company);
    }
}
