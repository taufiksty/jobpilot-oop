package dto;

import model.Company;
import model.JobApplication;

public class JobApplicationDto {
    private final JobApplication jobApplication;
    private final Company company;

    public JobApplicationDto(JobApplication jobApplication, Company company) {
        this.jobApplication = jobApplication;
        this.company = company;
    }

    // Getter and Setter
    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public Company getCompany() {
        return company;
    }
}
