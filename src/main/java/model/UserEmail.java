package model;

public class UserEmail extends BaseModel {
    private String userId;
    private String email;
    private String providerId;
    private String providerName;

    public UserEmail() {
        super();
    }

    public UserEmail(String userId, String email, String providerId, String providerName) {
        super();
        this.userId = userId;
        this.email = email;
        this.providerId = providerId;
        this.providerName = providerName;
    }

    public UserEmail(String id, String userId, String email, String providerId, String providerName) {
        super(id);
        this.userId = userId;
        this.email = email;
        this.providerId = providerId;
        this.providerName = providerName;
    }

    public String toString() {
        return "UserEmail{id=" + getId() + ", userId='" + userId + "', email='" + email + "', providerId='" + providerId
                + "', providerName='" + providerName + "'}";
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
