/*
 * Hands on 1 - Implementing Centralized Authentication with OAuth 2.1/OIDC
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class OidcCentralizedAuthenticationDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // spring.security.oauth2.client.registration.my-client
    static class ClientRegistration {
        final String clientId = "YOUR_CLIENT_ID";
        final String clientSecret = "YOUR_CLIENT_SECRET";
        final String scope = "openid, profile, email";
        final String authorizationGrantType = "authorization_code";
        final String redirectUri = "{baseUrl}/login/oauth2/code/{registrationId}";
    }

    // spring.security.oauth2.client.provider.my-provider
    static class ProviderDetails {
        final String authorizationUri = "https://accounts.google.com/o/oauth2/auth";
        final String tokenUri = "https://oauth2.googleapis.com/token";
        final String userInfoUri = "https://openidconnect.googleapis.com/v1/userinfo";
        final String userNameAttribute = "sub";
    }

    // @EnableWebSecurity SecurityConfig -> http.authorizeRequests().anyRequest().authenticated().and().oauth2Login()
    static class SecurityConfig {
        private final ProviderDetails provider;
        SecurityConfig(ProviderDetails provider) { this.provider = provider; }

        String buildAuthorizationRedirect(ClientRegistration registration, String state) {
            return provider.authorizationUri
                    + "?response_type=code"
                    + "&client_id=" + registration.clientId
                    + "&scope=" + registration.scope.replace(" ", "")
                    + "&redirect_uri=" + registration.redirectUri
                    + "&state=" + state;
        }
    }

    // simulates the OAuth2/OIDC provider issuing an authorization code, then an id_token/access_token
    static class OAuth2Provider {
        Map<String, Object> issueTokens(String code) {
            Map<String, Object> tokenResponse = new LinkedHashMap<>();
            tokenResponse.put("access_token", "ya29." + UUID.randomUUID());
            tokenResponse.put("id_token", "eyJhbGciOiJSUzI1NiJ9." + UUID.randomUUID());
            tokenResponse.put("token_type", "Bearer");
            tokenResponse.put("expires_in", 3600);
            return tokenResponse;
        }

        Map<String, Object> userInfo(String subject) {
            Map<String, Object> principal = new LinkedHashMap<>();
            principal.put("sub", subject);
            principal.put("name", "Jane Doe");
            principal.put("email", "jane.doe@example.com");
            principal.put("email_verified", true);
            return principal;
        }
    }

    // GET /user -> Principal user(Principal principal)
    static class UserController {
        Map<String, Object> user(Map<String, Object> principal) {
            return principal;
        }
    }

    private static final Logger LOGGER = new Logger("OidcCentralizedAuthenticationDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        ClientRegistration registration = new ClientRegistration();
        ProviderDetails provider = new ProviderDetails();
        SecurityConfig securityConfig = new SecurityConfig(provider);
        OAuth2Provider oAuth2Provider = new OAuth2Provider();
        UserController userController = new UserController();

        LOGGER.info("curl command: curl -s http://localhost:8080/user");
        String state = UUID.randomUUID().toString();
        String redirect = securityConfig.buildAuthorizationRedirect(registration, state);
        LOGGER.info("HTTP/1.1 302 Found");
        LOGGER.info("Location: " + redirect);

        LOGGER.info("user authenticates at provider and grants consent");
        String authorizationCode = "auth_code_" + UUID.randomUUID();
        LOGGER.info("redirected to: " + registration.redirectUri.replace("{baseUrl}", "http://localhost:8080")
                .replace("{registrationId}", "my-client") + "?code=" + authorizationCode + "&state=" + state);

        LOGGER.info("exchanging authorization code at token-uri: " + provider.tokenUri);
        Map<String, Object> tokens = oAuth2Provider.issueTokens(authorizationCode);
        LOGGER.info("token response=" + tokens);

        LOGGER.info("fetching principal at user-info-uri: " + provider.userInfoUri);
        Map<String, Object> principal = oAuth2Provider.userInfo(provider.userNameAttribute + "-98432");

        LOGGER.info("curl command: curl -s http://localhost:8080/user");
        LOGGER.info("HTTP/1.1 200");
        LOGGER.info("response=" + userController.user(principal));

        LOGGER.info("End");
    }
}
