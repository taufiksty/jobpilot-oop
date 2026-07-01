package util.security;

import dto.AuthUserDto;

public class SessionContext {
    private static AuthUserDto currentUser = null;

    private SessionContext() {
        // Private constructor to prevent instantiation
    }

    public static void login(AuthUserDto user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static AuthUserDto getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
