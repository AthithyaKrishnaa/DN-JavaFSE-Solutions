/*
 * Hands on 6 - JWT - Read Authorization header and decode the username and password
 */
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationControllerGetUserDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // @RestController class AuthenticationController
    static class AuthenticationController {
        private final Logger logger;

        AuthenticationController(Logger logger) { this.logger = logger; }

        private String getUser(String authHeader) {
            String encodedCredentials = authHeader.substring("Basic ".length());
            logger.debug("encodedCredentials={}", encodedCredentials);
            byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
            String decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
            logger.debug("decodedCredentials={}", decodedCredentials);
            String user = decodedCredentials.substring(0, decodedCredentials.indexOf(':'));
            logger.debug("user={}", user);
            return user;
        }

        // @GetMapping("/authenticate")
        Map<String, String> authenticate(String authHeader) {
            logger.info("Start");
            logger.debug("authHeader={}", authHeader);

            String user = getUser(authHeader);

            Map<String, String> map = new HashMap<>();
            map.put("token", "");
            logger.info("End");
            return map;
        }
    }

    private static final Logger LOGGER = new Logger("AuthenticationControllerGetUserDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        AuthenticationController authenticationController = new AuthenticationController(LOGGER);

        LOGGER.info("curl command: curl -s -u user:pwd http://localhost:8090/authenticate");
        String authHeader = "Basic " + Base64.getEncoder().encodeToString("user:pwd".getBytes(StandardCharsets.UTF_8));

        Map<String, String> response = authenticationController.authenticate(authHeader);
        LOGGER.info("response=" + response);

        LOGGER.info("End");
    }
}
