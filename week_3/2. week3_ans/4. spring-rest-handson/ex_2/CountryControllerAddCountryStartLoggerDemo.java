/*
 * Hands on 2 - Spring REST - Create RESTful Web Service to handle POST request of Country
 */
public class CountryControllerAddCountryStartLoggerDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // @RequestMapping("/countries")
    static class CountryController {
        private final Logger logger;

        CountryController(Logger logger) { this.logger = logger; }

        // @PostMapping()
        void addCountry() {
            logger.info("Start");
        }
    }

    private static final Logger LOGGER = new Logger("CountryControllerAddCountryStartLoggerDemo");

    private static void invokeCurl(CountryController controller, String curlCommand) {
        LOGGER.info("curl command: " + curlCommand);
        controller.addCountry();
        LOGGER.info("HTTP/1.1 200");
        LOGGER.info("Content-Length: 0");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        CountryController countryController = new CountryController(LOGGER);
        invokeCurl(countryController, "curl -i -X POST -s http://localhost:8090/countries");

        LOGGER.info("End");
    }
}
