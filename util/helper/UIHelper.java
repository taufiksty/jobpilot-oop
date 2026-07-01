package util.helper;

import dto.AuthUserDto;
import util.security.SessionContext;

public class UIHelper {

    public static void printBanner() {
        System.out.println("\u001B[36m\u001B[1m");
        System.out.println("  ╔═══════════════════════════════════╗");
        System.out.println("  ║         J O B P I L O T           ║");
        System.out.println("  ║     Platform Manage JobApp CLI    ║");
        System.out.println("  ╚═══════════════════════════════════╝");
        System.out.println("\u001B[0m");
    }

    public static void printMenu() {
        System.out.println();
        if (!SessionContext.isLoggedIn()) {
            printGuestMenu();
        } else {
            printUserMenu();
        }
        System.out.print("Your choice : ");
    }

    private static void printGuestMenu() {
        ConsoleHelper.printDivider();
        System.out.println("  MAIN MENU (Not login yet)");
        ConsoleHelper.printDivider();
        System.out.println("  1. Login");
        System.out.println("  2. Register");
        System.out.println("  0. Keluar");
        ConsoleHelper.printDivider();
    }

    private static void printUserMenu() {
        AuthUserDto authUser = SessionContext.getCurrentUser();
        ConsoleHelper.printDivider();
        System.out.printf("  LOGIN AS: %s [%s]%n", authUser.getUser().getName(),
                authUser.getUserEmails().get(0).getEmail());
        ConsoleHelper.printDivider();
        System.out.println("\n --- COMMON ---");
        System.out.println("  1. My Profile");
        System.out.println("  2. Logout");

        System.out.println("\n --- JOB APPLICATION ---");
        System.out.println("  3. Add Application");
        System.out.println("  4. Show My Applications");
        System.out.println("  5. Edit My Application");
        System.out.println("  6. Delete My Application");

        System.out.println("\n  0. Quit Application");
        ConsoleHelper.printDivider();
    }
}
