// Hands on - REST - Get all countries
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CountryListControllerDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
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
            return "{ \"code\": \"" + code + "\", \"name\": \"" + name + "\"}";
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

        List<Country> getAllCountries() {
            LOGGER.info("START getAllCountries()");
            List<Country> countries = new ArrayList<>();
            countries.add(new Country("IN", "India"));
            countries.add(new Country("US", "United States"));
            countries.add(new Country("JP", "Japan"));
            countries.add(new Country("DE", "Germany"));
            LOGGER.info("END getAllCountries()");
            return countries;
        }
    }

    static String toJsonArray(List<Country> countries) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < countries.size(); i++) {
            json.append("  ").append(countries.get(i).toJson());
            if (i < countries.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");
        return json.toString();
    }

    static class DispatcherServlet {
        private final Map<String, CountryController> getMappings = new LinkedHashMap<>();

        DispatcherServlet() {
            getMappings.put("/countries", new CountryController());
        }

        HttpResponse dispatch(String method, String url) {
            System.out.println(method + " " + url + " HTTP/1.1");
            if ("/countries".equals(url)) {
                List<Country> countries = getMappings.get("/countries").getAllCountries();
                return new HttpResponse(200, "application/json", toJsonArray(countries));
            }
            return new HttpResponse(404, "text/plain", "Not Found");
        }
    }

    public static void main(String[] args) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        HttpResponse response = dispatcherServlet.dispatch("GET", "/countries");
        System.out.println(response);
    }
}
