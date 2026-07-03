package dto.request;

import exception.HandleException;

public class LoginRequestDto {
    private String email;
    private String password;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void validate() {
        if (email == null || email.isBlank()) {
            throw new HandleException("Email cannot be empty.");
        }
        if (password == null || password.isBlank()) {
            throw new HandleException("Password cannot be empty.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
