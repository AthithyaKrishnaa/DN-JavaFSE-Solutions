/*
 * Hands on 1 - Introduction to HQL and JPQL
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class HqlVsJpqlDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-15s %s%n", "main", level, name, msg);
        }
    }

    static class QueryLanguageValidator {
        boolean isValidJpql(String query) {
            String trimmed = query.trim().toUpperCase();
            return trimmed.startsWith("SELECT") || trimmed.startsWith("UPDATE") || trimmed.startsWith("DELETE");
        }

        boolean isValidHql(String query) {
            String trimmed = query.trim().toUpperCase();
            return trimmed.startsWith("SELECT") || trimmed.startsWith("UPDATE")
                    || trimmed.startsWith("DELETE") || trimmed.startsWith("INSERT");
        }
    }

    private static final Logger LOGGER = new Logger("HqlVsJpqlDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        QueryLanguageValidator validator = new QueryLanguageValidator();

        Map<String, String> queries = new LinkedHashMap<>();
        queries.put("Select query", "SELECT e FROM Employee e WHERE e.permanent = 1");
        queries.put("Update query", "UPDATE Employee e SET e.salary = e.salary * 1.1 WHERE e.permanent = 1");
        queries.put("Insert query", "INSERT INTO Employee (id, name, salary) SELECT id, name, salary FROM TempEmployee");

        for (Map.Entry<String, String> entry : queries.entrySet()) {
            LOGGER.info("Start");
            LOGGER.debug(entry.getKey() + " validAsJpql={}", validator.isValidJpql(entry.getValue()));
            LOGGER.debug(entry.getKey() + " validAsHql={}", validator.isValidHql(entry.getValue()));
            LOGGER.info("End");
        }
    }
}
