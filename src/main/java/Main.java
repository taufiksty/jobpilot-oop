import java.util.Scanner;

import controller.AuthController;
import controller.JobApplicationController;
import repository.CompanyRepository;
import repository.JobApplicationRepository;
import repository.UserEmailRepository;
import repository.UserRepository;
import repository.inmemory.CompanyRepositoryInMemoryImpl;
import repository.inmemory.JobApplicationRepositoryInMemoryImpl;
import repository.inmemory.UserEmailRepositoryInMemoryImpl;
import repository.inmemory.UserRepositoryInMemoryImpl;
import service.AuthService;
import service.JobApplicationService;
import util.helper.ConsoleHelper;
import util.helper.UIHelper;
import util.security.SessionContext;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final UserRepository userRepository = new UserRepositoryInMemoryImpl();
    private static final UserEmailRepository userEmailRepository = new UserEmailRepositoryInMemoryImpl();
    private static final CompanyRepository companyRepository = new CompanyRepositoryInMemoryImpl();
    private static final JobApplicationRepository jobApplicationRepository = new JobApplicationRepositoryInMemoryImpl();

    private static final AuthService authService = new AuthService(userRepository, userEmailRepository);
    private static final JobApplicationService jobApplicationService = new JobApplicationService(
            jobApplicationRepository, companyRepository);

    private static final AuthController authController = new AuthController(authService, scanner);
    private static final JobApplicationController jobApplicationController = new JobApplicationController(
            jobApplicationService, scanner);

    public static void main(String[] args) {
        UIHelper.printBanner();
        boolean running = true;

        while (running) {
            UIHelper.printMenu();
            String choice = scanner.nextLine().trim();

            try {
                if (!SessionContext.isLoggedIn()) {
                    running = handleGuestMenu(choice);
                } else {
                    running = handleAuthenticatedMenu(choice);
                }
            } catch (Exception e) {
                ConsoleHelper.error(e.getMessage());
            }

        }

        System.out.println("\nExiting the application. Goodbye!");
        scanner.close();
    }

    // Menu before login
    private static boolean handleGuestMenu(String choice) {
        return switch (choice) {
            case "1" -> {
                // Handle login
                authController.handleLogin();
                yield true;
            }
            case "2" -> {
                // Handle register
                authController.handleRegister();
                yield true;
            }
            case "0" -> {
                // Exit
                yield false;
            }
            default -> {
                ConsoleHelper.error("Invalid choice. Please try again.");
                yield true;
            }
        };
    }

    // Menu after login
    private static boolean handleAuthenticatedMenu(String choice) {
        return switch (choice) {
            case "1" -> {
                // Handle view profile
                authController.handleMyProfile();
                yield true;
            }
            case "2" -> {
                // Handle logout
                authController.handleLogout();
                yield true;
            }
            case "3" -> {
                // Handle add job application
                jobApplicationController.handleAddJobApplication();
                yield true;
            }
            case "4" -> {
                // Handle get all job application user
                jobApplicationController.handleViewMyJobApplications();
                yield true;
            }
            case "5" -> {
                // Handle update job application user
                jobApplicationController.handleUpdateJobApplication();
                yield true;
            }
            case "6" -> {
                // Handle delete job application user
                jobApplicationController.handleDeleteJobApplication();
                yield true;
            }
            case "0" -> {
                // Exit
                yield false;
            }
            default -> {
                ConsoleHelper.error("Invalid choice. Please try again.");
                yield true;
            }
        };
    }
}
