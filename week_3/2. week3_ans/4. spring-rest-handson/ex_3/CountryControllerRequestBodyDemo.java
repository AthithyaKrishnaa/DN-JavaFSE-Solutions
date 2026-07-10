/*
 * Hands on 3 - Spring REST - Read country data as a bean in RESTful Web Service
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class CountryControllerRequestBodyDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class Country {
        String code;
        String name;

        String toJson() {
            return "{\"code\":\"" + code + "\",\"name\":" + (name == null ? "null" : "\"" + name + "\"") + "}";
        }

        @Override
        public String toString() { return "Country{code=" + code + ", name=" + name + "}"; }
    }

    // simulates Jackson parser mapping JSON attributes to setters via initcaps + get/set convention
    static final class JacksonParser {
        static Country parse(String json) {
            Country country = new Country();
            Map<String, String> fields = new LinkedHashMap<>();
            String body = json.replace("{", "").replace("}", "");
            for (String pair : body.split(",")) {
                String[] kv = pair.split(":", 2);
                String key = kv[0].replace("\"", "").trim();
                String value = kv[1].replace("\"", "").trim();
                fields.put(key, value);
            }
            country.code = fields.get("code");
            country.name = fields.get("name");
            return country;
        }
    }

    // @RequestMapping("/countries")
    static class CountryController {
        private final Logger logger;

        CountryController(Logger logger) { this.logger = logger; }

        // @PostMapping()
        Country addCountry(Country country) {
            logger.info("Start");
            logger.info("country details=" + country);
            return country;
        }
    }

    private static final Logger LOGGER = new Logger("CountryControllerRequestBodyDemo");

    private static void invokeCurl(CountryController controller, String curlCommand, String payload) {
        LOGGER.info("curl command: " + curlCommand);
        Country country = JacksonParser.parse(payload);
        Country response = controller.addCountry(country);
        LOGGER.info("HTTP/1.1 200");
        LOGGER.info("Content-Type: application/json;charset=UTF-8");
        LOGGER.info("response body=" + response.toJson());
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        CountryController countryController = new CountryController(LOGGER);

        invokeCurl(countryController,
                "curl -i -H 'Content-Type: application/json' -X POST -s -d '{\"code\":\"IN\",\"name\":\"India\"}' http://localhost:8090/countries",
                "{\"code\":\"IN\",\"name\":\"India\"}");

        invokeCurl(countryController,
                "curl -i -H 'Content-Type: application/json' -X POST -s -d '{\"code\":\"IN\",\"nae\":\"India\"}' http://localhost:8090/countries",
                "{\"code\":\"IN\",\"nae\":\"India\"}");

        LOGGER.info("End");
    }
}
