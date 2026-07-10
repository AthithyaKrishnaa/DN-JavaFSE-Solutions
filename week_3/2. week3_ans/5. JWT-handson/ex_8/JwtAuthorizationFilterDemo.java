/*
 * Hands on 8 - JWT - Authorize based on JWT using a Spring Filter
 */
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtAuthorizationFilterDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static final class JwtException extends RuntimeException {
        JwtException(String message) { super(message); }
    }

    static final class UsernamePasswordAuthenticationToken {
        final String principal;
        UsernamePasswordAuthenticationToken(String principal) { this.principal = principal; }
        @Override
        public String toString() { return "UsernamePasswordAuthenticationToken{principal=" + principal + "}"; }
    }

    static final class SecurityContextHolder {
        private static UsernamePasswordAuthenticationToken authentication;
        static void setAuthentication(UsernamePasswordAuthenticationToken token) { authentication = token; }
        static UsernamePasswordAuthenticationToken getAuthentication() { return authentication; }
        static void clear() { authentication = null; }
    }

    // extends BasicAuthenticationFilter
    static class JwtAuthorizationFilter {
        private static final String SECRET_KEY = "secretkey";
        private final Logger logger;

        JwtAuthorizationFilter(Logger logger) {
            this.logger = logger;
            logger.info("Start");
            logger.debug("authenticationManager={}", "AuthenticationManager");
        }

        private String base64UrlEncode(String data) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
        }

        private byte[] hmacSha256(String data) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private String parseSubject(String token) {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new JwtException("malformed token");

            String signingInput = parts[0] + "." + parts[1];
            byte[] expectedSignature = hmacSha256(signingInput);
            byte[] actualSignature = Base64.getUrlDecoder().decode(parts[2]);
            if (!Arrays.equals(expectedSignature, actualSignature)) {
                throw new JwtException("signature mismatch");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            String expField = payloadJson.substring(payloadJson.indexOf("\"exp\":") + 6);
            long expiration = Long.parseLong(expField.replaceAll("[^0-9]", ""));
            if (new Date().getTime() > expiration) {
                throw new JwtException("token expired");
            }

            int subStart = payloadJson.indexOf("\"sub\":\"") + 7;
            int subEnd = payloadJson.indexOf("\"", subStart);
            return payloadJson.substring(subStart, subEnd);
        }

        private UsernamePasswordAuthenticationToken getAuthentication(String header) {
            if (header == null) return null;
            try {
                String user = parseSubject(header.replace("Bearer ", ""));
                logger.debug("user={}", user);
                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user);
                }
            } catch (JwtException ex) {
                logger.info("JwtException: " + ex.getMessage());
                return null;
            }
            return null;
        }

        // @Override protected void doFilterInternal(req, res, chain)
        boolean doFilterInternal(String authorizationHeader) {
            logger.info("Start");
            logger.debug("header={}", authorizationHeader);

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                logger.info("no bearer token present, passing request through the filter chain unauthenticated");
                return false;
            }

            UsernamePasswordAuthenticationToken authentication = getAuthentication(authorizationHeader);
            if (authentication == null) {
                logger.info("invalid token, request remains unauthenticated");
                return false;
            }

            SecurityContextHolder.setAuthentication(authentication);
            logger.info("End");
            return true;
        }
    }

    private static String generateToken(String user, long validityMs) {
        try {
            String header = "{\"alg\":\"HS256\"}";
            String payload = "{\"sub\":\"" + user + "\",\"iat\":" + new Date().getTime()
                    + ",\"exp\":" + (new Date().getTime() + validityMs) + "}";
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec("secretkey".getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String signingInput = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8))
                    + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
            byte[] signatureBytes = mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
            String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
            return signingInput + "." + signature;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static final Logger LOGGER = new Logger("JwtAuthorizationFilterDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        JwtAuthorizationFilter filter = new JwtAuthorizationFilter(LOGGER);

        String validToken = generateToken("user", 1200000L);
        LOGGER.info("curl command: curl -s -H \"Authorization: Bearer " + validToken + "\" http://localhost:8090/countries");
        boolean authorized = filter.doFilterInternal("Bearer " + validToken);
        LOGGER.info("authorized=" + authorized + ", SecurityContextHolder.getAuthentication()=" + SecurityContextHolder.getAuthentication());
        SecurityContextHolder.clear();

        String tamperedToken = validToken.substring(0, validToken.length() - 4) + "abcd";
        LOGGER.info("curl command: curl -s -H \"Authorization: Bearer " + tamperedToken + "\" http://localhost:8090/countries");
        authorized = filter.doFilterInternal("Bearer " + tamperedToken);
        LOGGER.info("authorized=" + authorized + ", SecurityContextHolder.getAuthentication()=" + SecurityContextHolder.getAuthentication());

        LOGGER.info("End");
    }
}
