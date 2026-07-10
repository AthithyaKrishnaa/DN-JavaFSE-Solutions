/*
 * Hands on 3 - JWT - Limitations of Basic Authentication, Base64 encoding is not encryption
 */
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthBase64Demo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    private static String encodeCredentials(String credentials) {
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodeCredentials(String encoded) {
        return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
    }

    private static final Logger LOGGER = new Logger("BasicAuthBase64Demo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        String credentials = "admin:pwd";
        String authorizationHeader = "Basic " + encodeCredentials(credentials);
        LOGGER.info("curl command: curl -s -v -u admin:pwd http://localhost:8090/countries");
        LOGGER.info("Authorization request header: " + authorizationHeader);

        String encodedOnly = authorizationHeader.substring("Basic ".length());
        String decoded = decodeCredentials(encodedOnly);
        LOGGER.info("Base64 decode of '" + encodedOnly + "' = '" + decoded + "'");
        LOGGER.info("conclusion: Base64 is reversible encoding, not encryption; credentials are exposed on the wire");

        LOGGER.info("End");
    }
}
