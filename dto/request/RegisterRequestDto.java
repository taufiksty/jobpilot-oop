package dto.request;

import exception.HandleException;

public class RegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    public RegisterRequestDto(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new HandleException("Name cannot be empty.");
        }
        if (email == null || !email.matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new HandleException("Invalid email format.");
        }
        if (password == null || password.length() < 6) {
            throw new HandleException("Password must be at least 6 characters long.");
        }
        if (!password.equals(confirmPassword)) {
            throw new HandleException("Passwords do not match.");
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
