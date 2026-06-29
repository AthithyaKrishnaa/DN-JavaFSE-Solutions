// Exercise 1: Implementing the Singleton Pattern
// Scenario: A logging utility class that must have only ONE instance
// throughout the application lifecycle to ensure consistent logging.

class Logger {

    // Single private static instance of the class
    private static Logger instance;

    // Keep a simple log counter to prove the same instance is reused
    private int logCount;

    // Private constructor prevents direct instantiation from outside
    private Logger() {
        logCount = 0;
        System.out.println("Logger instance created.");
    }

    // Public static method to get the (only) instance of Logger
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        logCount++;
        System.out.println("[LOG #" + logCount + "] " + message);
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== SINGLETON PATTERN: LOGGER ===");

        System.out.println("\nRequesting first Logger instance:");
        Logger logger1 = Logger.getInstance();
        logger1.log("Application started.");

        System.out.println("\nRequesting second Logger instance:");
        Logger logger2 = Logger.getInstance();
        logger2.log("User logged in.");

        System.out.println("\n=== VERIFYING SINGLETON ===");
        if (logger1 == logger2) {
            System.out.println("logger1 and logger2 reference the SAME instance.");
        } else {
            System.out.println("logger1 and logger2 are DIFFERENT instances.");
        }

        logger1.log("This call increments the SAME shared counter.");

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("Only one Logger instance exists across the application,");
        System.out.println("ensuring consistent and centralized logging.");
    }
}
