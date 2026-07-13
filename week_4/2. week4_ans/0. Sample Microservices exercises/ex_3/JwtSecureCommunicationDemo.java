/*
 * Hands on 3 - Using JSON Web Tokens (JWT) for Secure Communication
 */
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtSecureCommunicationDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // spring.security.jwt.secret
    static class JwtConfig {
        private final String secret = "YOUR_SECRET_KEY";
        String getSecret() { return secret; }
    }

    // JwtTokenProvider - creates and validates HS256 signed tokens
    static class JwtTokenProvider {
        private final JwtConfig jwtConfig;
        JwtTokenProvider(JwtConfig jwtConfig) { this.jwtConfig = jwtConfig; }

        String createToken(String username) {
            String header = base64UrlEncode("{\"alg\":\"HS256\"}");
            long now = System.currentTimeMillis();
            long validity = now + 3600000; // 1 hour validity
            String payload = base64UrlEncode(
                    "{\"sub\":\"" + username + "\",\"iat\":" + now + ",\"exp\":" + validity + "}");
            String signature = sign(header + "." + payload);
            return header + "." + payload + "." + signature;
        }

        boolean validateToken(String token) {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            String expectedSignature = sign(parts[0] + "." + parts[1]);
            return expectedSignature.equals(parts[2]) && !isExpired(parts[1]);
        }

        String getUsername(String token) {
            String[] parts = token.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            int subStart = payloadJson.indexOf("\"sub\":\"") + 7;
            int subEnd = payloadJson.indexOf('"', subStart);
            return payloadJson.substring(subStart, subEnd);
        }

        private boolean isExpired(String encodedPayload) {
            String payloadJson = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
            int expStart = payloadJson.indexOf("\"exp\":") + 6;
            int expEnd = payloadJson.indexOf('}', expStart);
            long exp = Long.parseLong(payloadJson.substring(expStart, expEnd));
            return new Date().getTime() > exp;
        }

        private String sign(String data) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
                return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static String base64UrlEncode(String data) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
        }
    }

    // JwtTokenFilter - resolves Authorization header and sets authentication before the request reaches the controller
    static class JwtTokenFilter {
        private final JwtTokenProvider jwtTokenProvider;
        JwtTokenFilter(JwtTokenProvider jwtTokenProvider) { this.jwtTokenProvider = jwtTokenProvider; }

        String resolveToken(String authorizationHeader) {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                return authorizationHeader.substring(7);
            }
            return null;
        }

        String doFilter(String authorizationHeader) {
            String token = resolveToken(authorizationHeader);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                return jwtTokenProvider.getUsername(token);
            }
            return null;
        }
    }

    // GET /secure
    static class SecureController {
        String secure(String authenticatedUser) {
            return "This is a secure endpoint, hello " + authenticatedUser;
        }
    }

    private static final Logger LOGGER = new Logger("JwtSecureCommunicationDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        JwtConfig jwtConfig = new JwtConfig();
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtConfig);
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider);
        SecureController secureController = new SecureController();

        String token = jwtTokenProvider.createToken("john.doe");
        LOGGER.info("generated JWT for user 'john.doe': " + token);

        String authorizationHeader = "Bearer " + token;
        LOGGER.info("curl command: curl -s -H \"Authorization: " + authorizationHeader + "\" http://localhost:8080/secure");
        String authenticatedUser = jwtTokenFilter.doFilter(authorizationHeader);
        if (authenticatedUser != null) {
            LOGGER.info("SecurityContextHolder authentication set for principal: " + authenticatedUser);
            LOGGER.info("HTTP/1.1 200");
            LOGGER.info("response=" + secureController.secure(authenticatedUser));
        }

        LOGGER.info("curl command: curl -s http://localhost:8080/secure");
        String noAuthResult = jwtTokenFilter.doFilter(null);
        LOGGER.info("resolveToken returned null, SecurityContextHolder left unauthenticated: " + noAuthResult);
        LOGGER.info("HTTP/1.1 401");

        String tamperedToken = token.substring(0, token.length() - 4) + "abcd";
        LOGGER.info("curl command: curl -s -H \"Authorization: Bearer " + tamperedToken + "\" http://localhost:8080/secure");
        String tamperedResult = jwtTokenFilter.doFilter("Bearer " + tamperedToken);
        LOGGER.info("validateToken failed signature check, authenticated user: " + tamperedResult);
        LOGGER.info("HTTP/1.1 401");

        LOGGER.info("End");
    }
}
