/*
 * Hands on 2 - JWT - Creating users and roles in Spring Security
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SecurityConfigRolesDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // stands in for BCryptPasswordEncoder, using JDK-only SHA-256 hashing
    static final class PasswordEncoder {
        String encode(String rawPassword) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(rawPassword.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hash) sb.append(String.format("%02x", b));
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e);
            }
        }

        boolean matches(String rawPassword, String encodedPassword) {
            return encode(rawPassword).equals(encodedPassword);
        }
    }

    static class UserAccount {
        String username;
        String encodedPassword;
        String role;
        UserAccount(String username, String encodedPassword, String role) {
            this.username = username;
            this.encodedPassword = encodedPassword;
            this.role = role;
        }
    }

    // @Configuration @EnableWebSecurity class SecurityConfig
    static class SecurityConfig {
        private final PasswordEncoder passwordEncoder = new PasswordEncoder();
        private final Map<String, UserAccount> users = new HashMap<>();

        SecurityConfig(Logger logger) {
            logger.info("Start");
            users.put("admin", new UserAccount("admin", passwordEncoder.encode("pwd"), "ADMIN"));
            users.put("user", new UserAccount("user", passwordEncoder.encode("pwd"), "USER"));
        }

        // configure(HttpSecurity): antMatchers("/countries").hasRole("USER")
        Map<String, Object> authorize(String username, String rawPassword, String requiredRole, String path) {
            UserAccount account = users.get(username);
            if (account == null || !passwordEncoder.matches(rawPassword, account.encodedPassword)) {
                return errorResponse(401, "Unauthorized", path);
            }
            if (!account.role.equals(requiredRole)) {
                return errorResponse(403, "Forbidden", path);
            }
            return null;
        }

        private Map<String, Object> errorResponse(int status, String error, String path) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("status", status);
            body.put("error", error);
            body.put("message", error);
            body.put("path", path);
            return body;
        }
    }

    static class Country {
        String code;
        String name;
        Country(String code, String name) { this.code = code; this.name = name; }
        @Override
        public String toString() { return "{\"code\":\"" + code + "\",\"name\":\"" + name + "\"}"; }
    }

    private static final Logger LOGGER = new Logger("SecurityConfigRolesDemo");

    private static List<Country> allCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country("US", "United States"));
        countries.add(new Country("DE", "Germany"));
        countries.add(new Country("IN", "India"));
        countries.add(new Country("JP", "Japan"));
        return countries;
    }

    private static void invokeCountries(SecurityConfig securityConfig, String user, String password, String curlDescription) {
        LOGGER.info("curl command: " + curlDescription);
        Map<String, Object> error = securityConfig.authorize(user, password, "USER", "/countries");
        if (error == null) {
            LOGGER.info("HTTP/1.1 200, response=" + allCountries());
        } else {
            LOGGER.info("response=" + error);
        }
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        SecurityConfig securityConfig = new SecurityConfig(LOGGER);

        invokeCountries(securityConfig, "user", "pwd", "curl -s -u user:pwd http://localhost:8090/countries");
        invokeCountries(securityConfig, "user", "pwd1", "curl -s -u user:pwd1 http://localhost:8090/countries");
        invokeCountries(securityConfig, "admin", "pwd", "curl -s -u admin:pwd http://localhost:8090/countries");

        LOGGER.info("End");
    }
}
