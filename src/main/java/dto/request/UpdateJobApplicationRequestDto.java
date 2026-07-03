package dto.request;

import enums.JobApplicationStatus;
import exception.HandleException;

public class UpdateJobApplicationRequestDto {
    private String id;
    private String userId;
    private String companyName;
    private String position;
    private JobApplicationStatus status;
    private String source;

    public UpdateJobApplicationRequestDto(String id, String userId, String companyName, String position,
            JobApplicationStatus status, String source) {
        this.id = id;
        this.userId = userId;
        this.companyName = companyName;
        this.position = position;
        this.status = status;
        this.source = source;
    }

    public void validate() {
        if (companyName == null || companyName.isBlank()) {
            throw new HandleException("Company name cannot be empty.");
        }
        if (position == null || position.isBlank()) {
            throw new HandleException("Position cannot be empty.");
        }
        if (status == null) {
            throw new HandleException("Invalid job application status.");
        }
        if (source == null || source.isBlank()) {
            throw new HandleException("Source cannot be empty.");
        }
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
