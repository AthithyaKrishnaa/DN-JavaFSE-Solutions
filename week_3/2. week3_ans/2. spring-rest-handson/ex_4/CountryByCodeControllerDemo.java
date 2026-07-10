// Hands on - REST - Get country based on country code
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CountryByCodeControllerDemo {

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

    static class CountryService {
        private static final Logger LOGGER = new Logger("CountryService");
        private final List<Country> countries = new ArrayList<>();

        CountryService() {
            countries.add(new Country("IN", "India"));
            countries.add(new Country("US", "United States"));
            countries.add(new Country("JP", "Japan"));
            countries.add(new Country("DE", "Germany"));
        }

        Optional<Country> getCountry(String code) {
            LOGGER.debug("Looking up country code: " + code);
            return countries.stream()
                    .filter(country -> country.code.equalsIgnoreCase(code))
                    .findFirst();
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
            return "HTTP/1.1 " + status + " OK\nContent-Type: " + contentType + "\n\n" + body;
        }
    }

    static class CountryController {
        private static final Logger LOGGER = new Logger("CountryController");
        private final CountryService countryService = new CountryService();

        Country getCountry(String code) {
            LOGGER.info("START getCountry(" + code + ")");
            Country country = countryService.getCountry(code).orElse(null);
            LOGGER.info("END getCountry(" + code + ")");
            return country;
        }
    }

    static class DispatcherServlet {
        private final Map<String, CountryController> pathVariableMappings = new LinkedHashMap<>();

        DispatcherServlet() {
            pathVariableMappings.put("/countries", new CountryController());
        }

        HttpResponse dispatch(String method, String url) {
            System.out.println(method + " " + url + " HTTP/1.1");
            String prefix = "/countries/";
            if (url.startsWith(prefix)) {
                String code = url.substring(prefix.length());
                Country country = pathVariableMappings.get("/countries").getCountry(code);
                if (country == null) return new HttpResponse(404, "text/plain", "Not Found");
                return new HttpResponse(200, "application/json", country.toJson());
            }
            return new HttpResponse(404, "text/plain", "Not Found");
        }
    }

    public static void main(String[] args) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        HttpResponse response = dispatcherServlet.dispatch("GET", "/countries/in");
        System.out.println(response);
    }
}
