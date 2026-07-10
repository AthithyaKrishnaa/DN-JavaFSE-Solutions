/*
 * Hands on 5 - JWT - Create authentication controller and configure it in SecurityConfig
 */
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationControllerStubDemo {

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

        // @GetMapping("/authenticate")
        Map<String, String> authenticate(String authHeader) {
            logger.info("Start");
            logger.debug("authHeader={}", authHeader);
            Map<String, String> map = new HashMap<>();
            map.put("token", "");
            logger.info("End");
            return map;
        }
    }

    // antMatchers("/authenticate").hasAnyRole("USER", "ADMIN")
    static class SecurityConfig {
        boolean isAuthorized(String role) { return "USER".equals(role) || "ADMIN".equals(role); }
    }

    private static final Logger LOGGER = new Logger("AuthenticationControllerStubDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        SecurityConfig securityConfig = new SecurityConfig();
        AuthenticationController authenticationController = new AuthenticationController(LOGGER);

        LOGGER.info("curl command: curl -s -u user:pwd http://localhost:8090/authenticate");
        String authHeader = "Basic " + Base64.getEncoder().encodeToString("user:pwd".getBytes(StandardCharsets.UTF_8));

        if (securityConfig.isAuthorized("USER")) {
            Map<String, String> response = authenticationController.authenticate(authHeader);
            LOGGER.info("response=" + response);
        }

        LOGGER.info("End");
    }
}
