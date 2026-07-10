/*
 * Hands on 4 - JWT - Understanding JWT structure - header, payload, signature
 */
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtStructureDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    private static String base64UrlEncode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String hmacSha256(String data, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
    }

    private static final Logger LOGGER = new Logger("JwtStructureDemo");

    public static void main(String[] args) throws Exception {
        LOGGER.info("Inside main");

        String secretKey = "secretkey";

        // Header: Contains the encryption algorithm
        String headerJson = "{\"alg\":\"HS256\"}";
        // Payload: Contains application specific data - user id and role
        String payloadJson = "{\"sub\":\"1234567890\",\"name\":\"John Doe\",\"admin\":true}";

        String encodedHeader = base64UrlEncode(headerJson);
        String encodedPayload = base64UrlEncode(payloadJson);
        LOGGER.info("header json=" + headerJson + " -> encoded=" + encodedHeader);
        LOGGER.info("payload json=" + payloadJson + " -> encoded=" + encodedPayload);

        // Signature: Computed based on the formula defined using header and payload
        String signingInput = encodedHeader + "." + encodedPayload;
        String signature = hmacSha256(signingInput, secretKey);
        LOGGER.info("signature computed with HMACSHA256 and secretkey '" + secretKey + "' = " + signature);

        String token = signingInput + "." + signature;
        LOGGER.info("generated JWT: " + token);
        LOGGER.info("compare this token against the Encoded section on https://jwt.io using the same header, payload and secretkey");

        LOGGER.info("End");
    }
}
