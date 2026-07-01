package model;

import java.time.LocalDateTime;

import enums.JobApplicationStatus;

public class JobApplication extends BaseModel {
    private int userId;
    private int companyId;
    private String position;
    private JobApplicationStatus status;
    private String jobUrl;
    private String source;
    private LocalDateTime appliedDateTime;
    private LocalDateTime importanDateTime;
    private String importantNote;

    public JobApplication(int userId, int companyId, String position, JobApplicationStatus status,
            String jobUrl, String source, LocalDateTime appliedDateTime,
            LocalDateTime importanDateTime, String importantNote) {
        super();
        this.userId = userId;
        this.companyId = companyId;
        this.position = position;
        this.status = status;
        this.jobUrl = jobUrl;
        this.source = source;
        this.appliedDateTime = appliedDateTime;
        this.importanDateTime = importanDateTime;
        this.importantNote = importantNote;
    }

    public JobApplication(int id, int userId, int companyId, String position, JobApplicationStatus status,
            String jobUrl, String source, LocalDateTime appliedDateTime,
            LocalDateTime importanDateTime, String importantNote) {
        super(id);
        this.userId = userId;
        this.companyId = companyId;
        this.position = position;
        this.status = status;
        this.jobUrl = jobUrl;
        this.source = source;
        this.appliedDateTime = appliedDateTime;
        this.importanDateTime = importanDateTime;
        this.importantNote = importantNote;
    }

    public String toString() {
        return "JobApplication{id=" + getId() + ", userId=" + userId + ", companyId=" + companyId
                + ", position='" + position + "', status=" + status + ", jobUrl='" + jobUrl
                + "', source='" + source + "', applied=" + appliedDateTime
                + ", importantDate=" + importanDateTime + "}";
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getAppliedDateTime() {
        return appliedDateTime;
    }

    public void setAppliedDateTime(LocalDateTime appliedDateTime) {
        this.appliedDateTime = appliedDateTime;
    }

    public LocalDateTime getImportanDateTime() {
        return importanDateTime;
    }

    public void setImportanDateTime(LocalDateTime importanDateTime) {
        this.importanDateTime = importanDateTime;
    }

    public String getImportantNote() {
        return importantNote;
    }

    public void setImportantNote(String importantNote) {
        this.importantNote = importantNote;
    }
}
