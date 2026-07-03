package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import dto.AuthUserDto;
import dto.request.LoginRequestDto;
import dto.request.RegisterRequestDto;
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

    public User login(LoginRequestDto request) {
        request.validate();

        Optional<UserEmail> userEmailOpt = userEmailRepository.findByEmail(request.getEmail());
        if (userEmailOpt.isEmpty()) {
            throw new HandleException("Invalid email or password.");
        }

        Optional<User> userOpt = userRepository.findById(userEmailOpt.get().getUserId());
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
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

    public User register(RegisterRequestDto request) {
        request.validate();

        Optional<UserEmail> existingUserEmail = userEmailRepository.findByEmail(request.getEmail());
        if (existingUserEmail.isPresent()) {
            throw new HandleException("Email is already registered.");
        }

        User newUser = new User(request.getName(), request.getPassword());
        userRepository.save(newUser);

        UserEmail newUserEmail = new UserEmail(newUser.getId(), request.getEmail(), "local", "local");
        userEmailRepository.save(newUserEmail);

        return newUser;
    }

    public void logout() {
        SessionContext.logout();
    }
}
