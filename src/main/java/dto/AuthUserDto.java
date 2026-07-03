package dto;

import java.util.List;

import model.User;
import model.UserEmail;

public class AuthUserDto {
    private final User user;
    private final List<UserEmail> userEmails;

    public AuthUserDto(User user, List<UserEmail> userEmails) {
        this.user = user;
        this.userEmails = userEmails;
    }

    // Getter and Setter
    public User getUser() {
        return user;
    }

    public List<UserEmail> getUserEmails() {
        return userEmails;
    }
}
