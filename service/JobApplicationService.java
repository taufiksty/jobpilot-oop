package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import dto.JobApplicationDto;
import dto.request.AddJobApplicationRequestDto;
import dto.request.UpdateJobApplicationRequestDto;
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

    public JobApplicationDto addJobApplication(AddJobApplicationRequestDto request) {
        Company company = findOrCreateCompany(request.getCompanyName());

        JobApplication jobApplication = new JobApplication(
                request.getUserId(),
                company.getId(),
                request.getPosition(),
                request.getStatus(),
                null, // jobUrl
                request.getSource(),
                request.getStatus().equals(JobApplicationStatus.APPLIED) ? LocalDateTime.now() : null, // appliedDateTime
                null, // importanDateTime
                null // importantNote
        );

        JobApplication saved = jobApplicationRepository.save(jobApplication);
        return toDto(saved);
    }

    public List<JobApplicationDto> getMyJobApplications(String userId) {
        return jobApplicationRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public JobApplicationDto getJobApplicationDetail(String id) {
        JobApplication jobApplication = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new HandleException("Job application with id " + id + " not found."));
        return toDto(jobApplication);
    }

    public JobApplicationDto updateJobApplication(UpdateJobApplicationRequestDto request) {
        JobApplication existing = jobApplicationRepository.findById(request.getId())
                .orElseThrow(() -> new HandleException("Job application with id " + request.getId() + " not found."));

        Company company = findOrCreateCompany(request.getCompanyName());

        existing.setUserId(request.getUserId());
        existing.setCompanyId(company.getId());
        existing.setPosition(request.getPosition());
        existing.setStatus(request.getStatus());
        existing.setSource(request.getSource());

        JobApplication updated = jobApplicationRepository.save(existing);
        return toDto(updated);
    }

    public void deleteJobApplication(String id) {
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
