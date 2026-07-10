// Hands on - REST - Country Web Service
import java.util.LinkedHashMap;
import java.util.Map;

public class CountryControllerDemo {

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

        Country getCountryIndia() {
            LOGGER.info("START getCountryIndia()");
            Country india = new Country("IN", "India");
            LOGGER.debug("Country loaded from spring xml configuration: " + india.code);
            LOGGER.info("END getCountryIndia()");
            return india;
        }
    }

    static class DispatcherServlet {
        private final Map<String, CountryController> requestMappings = new LinkedHashMap<>();

        DispatcherServlet() {
            requestMappings.put("/country", new CountryController());
        }

        HttpResponse dispatch(String method, String url) {
            System.out.println(method + " " + url + " HTTP/1.1");
            if ("/country".equals(url)) {
                Country india = requestMappings.get("/country").getCountryIndia();
                return new HttpResponse(200, "application/json", india.toJson());
            }
            return new HttpResponse(404, "text/plain", "Not Found");
        }
    }

    public static void main(String[] args) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        HttpResponse response = dispatcherServlet.dispatch("GET", "/country");
        System.out.println(response);
    }
}
