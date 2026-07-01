package controller;

import java.util.List;
import java.util.Scanner;

import dto.AuthUserDto;
import model.User;
import model.UserEmail;
import service.AuthService;
import util.helper.ConsoleHelper;

public class AuthController {

    private final AuthService authService;
    private final Scanner scanner;

    public AuthController(AuthService authService, Scanner scanner) {
        this.authService = authService;
        this.scanner = scanner;
    }

    public void handleRegister() {
        ConsoleHelper.print("\nREGISTER");
        System.out.print("Name              : ");
        String name = scanner.nextLine().trim();
        System.out.print("Email             : ");
        String email = scanner.nextLine().trim();
        System.out.print("Password          : ");
        String password = scanner.nextLine().trim();
        System.out.print("Confirm Password  : ");
        String confirmPassword = scanner.nextLine().trim();

        User user = authService.register(name, email, password, confirmPassword);
        ConsoleHelper.success("Registration successful for " + user.getName() + "! Please log in.");
    }

    public void handleLogin() {
        ConsoleHelper.print("\nLOGIN");
        System.out.print("Email     : ");
        String email = scanner.nextLine().trim();
        System.out.print("Password  : ");
        String password = scanner.nextLine().trim();

        User user = authService.login(email, password);
        ConsoleHelper.success("Login successful! Welcome, " + user.getName() + ".");
    }

    public void handleMyProfile() {
        ConsoleHelper.printHeader("MY PROFILE");

        AuthUserDto currentUser = util.security.SessionContext.getCurrentUser();
        User user = currentUser.getUser();
        List<UserEmail> emails = currentUser.getUserEmails();

        System.out.printf("  ID         : %d%n", user.getId());
        System.out.printf("  Name       : %s%n", user.getName());
        System.out.printf("  Created At : %s%n", user.getCreatedAt());
        System.out.printf("  Last Login : %s%n", user.getLastLoginAt());

        System.out.println("\n  Emails:");
        for (UserEmail email : emails) {
            System.out.printf("    - %s (%s)%n", email.getEmail(), email.getProviderName());
        }
    }

    public void handleLogout() {
        authService.logout();
        ConsoleHelper.success("You have been logged out successfully.");
    }
}
