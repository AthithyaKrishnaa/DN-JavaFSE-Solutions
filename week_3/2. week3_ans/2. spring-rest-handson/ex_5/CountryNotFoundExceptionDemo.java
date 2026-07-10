// Hands on - REST - Get country exceptional scenario
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CountryNotFoundExceptionDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg) { print("DEBUG", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class Country {
        String code;
        String name;

        Country(String code, String name) {
            this.code = code;
            this.name = name;
        }

        String toJson() {
            return "{\n  \"code\": \"" + code + "\",\n  \"name\": \"" + name + "\"\n}";
        }
    }

    static class CountryNotFoundException extends RuntimeException {
        static final int STATUS = 404;
        static final String REASON = "Country not found";

        CountryNotFoundException() {
            super(REASON);
        }
    }

    static class CountryService {
        private static final Logger LOGGER = new Logger("CountryService");
        private final List<Country> countries = new ArrayList<>();

        CountryService() {
            countries.add(new Country("IN", "India"));
            countries.add(new Country("US", "United States"));
            countries.add(new Country("JP", "Japan"));
            countries.add(new Country("DE", "Germany"));
        }

        Country getCountry(String code) {
            LOGGER.debug("Looking up country code: " + code);
            return countries.stream()
                    .filter(country -> country.code.equalsIgnoreCase(code))
                    .findFirst()
                    .orElseThrow(CountryNotFoundException::new);
        }
    }

    static class HttpResponse {
        final int status;
        final String contentType;
        final String body;

        HttpResponse(int status, String contentType, String body) {
            this.status = status;
            this.contentType = contentType;
            this.body = body;
        }

        @Override
        public String toString() {
            return "HTTP/1.1 " + status + "\nContent-Type: " + contentType + "\n\n" + body;
        }
    }

    static class CountryController {
        private static final Logger LOGGER = new Logger("CountryController");
        private final CountryService countryService = new CountryService();

        Country getCountry(String code) {
            LOGGER.info("START getCountry(" + code + ")");
            Country country = countryService.getCountry(code);
            LOGGER.info("END getCountry(" + code + ")");
            return country;
        }
    }

    static String errorJson(int status, String error, String message, String path) {
        return "{\n\"timestamp\": \"" + Instant.now() + "\",\n\"status\": " + status
                + ",\n\"error\": \"" + error + "\",\n\"message\": \"" + message + "\",\n\"path\": \"" + path + "\"\n}";
    }

    static class DispatcherServlet {
        private final Map<String, CountryController> pathVariableMappings = new LinkedHashMap<>();

        DispatcherServlet() {
            pathVariableMappings.put("/country", new CountryController());
        }

        HttpResponse dispatch(String method, String url) {
            System.out.println(method + " " + url + " HTTP/1.1");
            String prefix = "/country/";
            if (url.startsWith(prefix)) {
                String code = url.substring(prefix.length());
                try {
                    Country country = pathVariableMappings.get("/country").getCountry(code);
                    return new HttpResponse(200, "application/json", country.toJson());
                } catch (CountryNotFoundException e) {
                    String body = errorJson(CountryNotFoundException.STATUS, "Not Found", e.getMessage(), url);
                    return new HttpResponse(CountryNotFoundException.STATUS, "application/json", body);
                }
            }
            return new HttpResponse(404, "text/plain", "Not Found");
        }
    }

    public static void main(String[] args) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        System.out.println(dispatcherServlet.dispatch("GET", "/country/in"));
        System.out.println();
        System.out.println(dispatcherServlet.dispatch("GET", "/country/az"));
    }
}
