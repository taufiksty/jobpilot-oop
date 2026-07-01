package util.helper;

public class ConsoleHelper {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    public static void printHeader(String title) {
        System.out.println(CYAN + BOLD);
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.printf("║  %-38s  ║%n", title);
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print(RESET);
    }

    public static void printDivider() {
        System.out.println("──────────────────────────────────────────");
    }

    public static void success(String msg) {
        System.out.println(GREEN + "v " + msg + RESET);
    }

    public static void error(String msg) {
        System.out.println(RED + "x " + msg + RESET);
    }

    public static void info(String msg) {
        System.out.println(YELLOW + "-> " + msg + RESET);
    }

    public static void print(String msg) {
        System.out.println(msg);
    }
}
