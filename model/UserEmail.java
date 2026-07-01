package model;

public class UserEmail extends BaseModel {
    private int userId;
    private String email;
    private String providerId;
    private String providerName;

    public UserEmail(int userId, String email, String providerId, String providerName) {
        super();
        this.userId = userId;
        this.email = email;
        this.providerId = providerId;
        this.providerName = providerName;
    }

    public UserEmail(int id, int userId, String email, String providerId, String providerName) {
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
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
