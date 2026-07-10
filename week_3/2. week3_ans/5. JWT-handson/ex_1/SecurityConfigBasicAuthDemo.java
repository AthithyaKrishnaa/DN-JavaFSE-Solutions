/*
 * Hands on 1 - JWT - Securing RESTful Web Services with Spring Security
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SecurityConfigBasicAuthDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class Country {
        String code;
        String name;
        Country(String code, String name) { this.code = code; this.name = name; }
        @Override
        public String toString() { return "{\"code\":\"" + code + "\",\"name\":\"" + name + "\"}"; }
    }

    // @Configuration @EnableWebSecurity class SecurityConfig
    static class SecurityConfig {
        // spring boot generates a random password on startup when no explicit user is configured
        final String generatedPassword = UUID.randomUUID().toString();
        final String generatedUser = "user";

        boolean isAuthorized(String user, String password) {
            return generatedUser.equals(user) && generatedPassword.equals(password);
        }
    }

    static class CountryController {
        private final List<Country> countries = new ArrayList<>();

        CountryController() {
            countries.add(new Country("US", "United States"));
            countries.add(new Country("DE", "Germany"));
            countries.add(new Country("IN", "India"));
            countries.add(new Country("JP", "Japan"));
        }

        List<Country> getAllCountries() { return countries; }
    }

    private static final Logger LOGGER = new Logger("SecurityConfigBasicAuthDemo");

    private static Map<String, Object> unauthorizedResponse(String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", "Unauthorized");
        body.put("path", path);
        return body;
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        SecurityConfig securityConfig = new SecurityConfig();
        CountryController countryController = new CountryController();

        LOGGER.info("Using web security: password generated for user 'user', check application logs");
        LOGGER.info("Using generated security password: " + securityConfig.generatedPassword);

        LOGGER.info("curl command: curl -s http://localhost:8090/countries");
        LOGGER.info("response=" + unauthorizedResponse("/countries"));

        LOGGER.info("curl command: curl -s -u user:" + securityConfig.generatedPassword + " http://localhost:8090/countries");
        if (securityConfig.isAuthorized("user", securityConfig.generatedPassword)) {
            LOGGER.info("HTTP/1.1 200");
            LOGGER.info("response=" + countryController.getAllCountries());
        } else {
            LOGGER.info("response=" + unauthorizedResponse("/countries"));
        }

        LOGGER.info("End");
    }
}
