/*
 * Hands on 2 - Configuring Authorization Servers and Resource Servers
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class AuthorizationResourceServerDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // spring.security.oauth2.resourceserver.jwt.issuer-uri
    static class ResourceServerProperties {
        final String issuerUri = "https://issuer.example.com";
    }

    static class Jwt {
        final String issuer;
        final String subject;
        final long expiresAtEpochSeconds;
        Jwt(String issuer, String subject, long expiresAtEpochSeconds) {
            this.issuer = issuer;
            this.subject = subject;
            this.expiresAtEpochSeconds = expiresAtEpochSeconds;
        }
    }

    // @EnableWebSecurity ResourceServerConfig -> http.authorizeRequests().anyRequest().authenticated()
    //   .and().oauth2ResourceServer().jwt()
    static class ResourceServerConfig {
        private final ResourceServerProperties properties;
        ResourceServerConfig(ResourceServerProperties properties) { this.properties = properties; }

        boolean isValid(Jwt jwt, long nowEpochSeconds) {
            return jwt != null
                    && properties.issuerUri.equals(jwt.issuer)
                    && jwt.expiresAtEpochSeconds > nowEpochSeconds;
        }
    }

    // GET /secure
    static class SecureController {
        String secure() {
            return "This is a secure endpoint";
        }
    }

    private static Map<String, Object> unauthorizedResponse(String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", "Full authentication is required to access this resource");
        body.put("path", path);
        return body;
    }

    private static final Logger LOGGER = new Logger("AuthorizationResourceServerDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        ResourceServerProperties properties = new ResourceServerProperties();
        ResourceServerConfig resourceServerConfig = new ResourceServerConfig(properties);
        SecureController secureController = new SecureController();

        long now = System.currentTimeMillis() / 1000L;

        LOGGER.info("curl command: curl -s http://localhost:8080/secure");
        LOGGER.info("HTTP/1.1 401");
        LOGGER.info("response=" + unauthorizedResponse("/secure"));

        Jwt validToken = new Jwt(properties.issuerUri, "user1", now + 3600);
        LOGGER.info("curl command: curl -s -H \"Authorization: Bearer <valid-jwt-issued-by-" + properties.issuerUri + ">\" http://localhost:8080/secure");
        if (resourceServerConfig.isValid(validToken, now)) {
            LOGGER.info("token issuer '" + validToken.issuer + "' matches configured issuer-uri, token not expired");
            LOGGER.info("HTTP/1.1 200");
            LOGGER.info("response=" + secureController.secure());
        }

        Jwt expiredToken = new Jwt(properties.issuerUri, "user1", now - 60);
        LOGGER.info("curl command: curl -s -H \"Authorization: Bearer <expired-jwt>\" http://localhost:8080/secure");
        if (!resourceServerConfig.isValid(expiredToken, now)) {
            LOGGER.info("token expired at " + expiredToken.expiresAtEpochSeconds + ", current time " + now);
            LOGGER.info("HTTP/1.1 401");
            LOGGER.info("response=" + unauthorizedResponse("/secure"));
        }

        Jwt untrustedIssuerToken = new Jwt("https://untrusted-issuer.example.com", "user1", now + 3600);
        LOGGER.info("curl command: curl -s -H \"Authorization: Bearer <jwt-from-untrusted-issuer>\" http://localhost:8080/secure");
        if (!resourceServerConfig.isValid(untrustedIssuerToken, now)) {
            LOGGER.info("token issuer '" + untrustedIssuerToken.issuer + "' does not match configured issuer-uri '" + properties.issuerUri + "'");
            LOGGER.info("HTTP/1.1 401");
            LOGGER.info("response=" + unauthorizedResponse("/secure"));
        }

        LOGGER.info("End");
    }
}
