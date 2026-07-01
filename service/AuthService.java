package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import dto.AuthUserDto;
import exception.HandleException;
import model.User;
import model.UserEmail;
import repository.UserEmailRepository;
import repository.UserRepository;
import util.security.SessionContext;

public class AuthService {
    private final UserRepository userRepository;
    private final UserEmailRepository userEmailRepository;

    public AuthService(UserRepository userRepository, UserEmailRepository userEmailRepository) {
        this.userRepository = userRepository;
        this.userEmailRepository = userEmailRepository;
    }

    public User login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new HandleException("Email cannot be empty.");
        }
        if (password == null || password.isBlank()) {
            throw new HandleException("Password cannot be empty.");
        }

        Optional<UserEmail> userEmailOpt = userEmailRepository.findByEmail(email);
        if (userEmailOpt.isEmpty()) {
            throw new HandleException("Invalid email or password.");
        }

        Optional<User> userOpt = userRepository.findById(userEmailOpt.get().getUserId());
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            throw new HandleException("Invalid email or password.");
        }

        User user = userOpt.get();
        UserEmail userEmail = userEmailOpt.get();

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        AuthUserDto authUserDto = new AuthUserDto(user, List.of(userEmail));

        SessionContext.login(authUserDto);
        return user;
    }

    public User register(String name, String email, String password, String confirmPassword) {
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

        Optional<UserEmail> existingUserEmail = userEmailRepository.findByEmail(email);
        if (existingUserEmail.isPresent()) {
            throw new HandleException("Email is already registered.");
        }

        User newUser = new User(name, password);
        userRepository.save(newUser);

        UserEmail newUserEmail = new UserEmail(newUser.getId(), email, "local", "local");
        userEmailRepository.save(newUserEmail);

        return newUser;
    }

    public void logout() {
        SessionContext.logout();
    }
}
