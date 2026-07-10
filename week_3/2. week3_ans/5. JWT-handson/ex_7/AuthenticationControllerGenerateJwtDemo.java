/*
 * Hands on 7 - JWT - Generate token based on the user
 */
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AuthenticationControllerGenerateJwtDemo {

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
        private static final String SECRET_KEY = "secretkey";
        private static final long TOKEN_VALIDITY_MS = 1200000L; // 20 minutes

        private final Logger logger;

        AuthenticationController(Logger logger) { this.logger = logger; }

        private String getUser(String authHeader) {
            String encodedCredentials = authHeader.substring("Basic ".length());
            byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
            String decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
            return decodedCredentials.substring(0, decodedCredentials.indexOf(':'));
        }

        private String base64UrlEncode(String data) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
        }

        private String hmacSha256(String data) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
                return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        // builder.setSubject(user).setIssuedAt(now).setExpiration(now+20m).signWith(HS256, "secretkey")
        private String generateJwt(String user) {
            long issuedAt = new Date().getTime();
            long expiration = issuedAt + TOKEN_VALIDITY_MS;

            String header = "{\"alg\":\"HS256\"}";
            String payload = "{\"sub\":\"" + user + "\",\"iat\":" + issuedAt + ",\"exp\":" + expiration + "}";

            String signingInput = base64UrlEncode(header) + "." + base64UrlEncode(payload);
            String signature = hmacSha256(signingInput);

            String token = signingInput + "." + signature;
            logger.debug("generated token={}", token);
            return token;
        }

        // @GetMapping("/authenticate")
        Map<String, String> authenticate(String authHeader) {
            logger.info("Start");
            logger.debug("authHeader={}", authHeader);

            String user = getUser(authHeader);
            String token = generateJwt(user);

            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            logger.info("End");
            return map;
        }
    }

    private static final Logger LOGGER = new Logger("AuthenticationControllerGenerateJwtDemo");

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
