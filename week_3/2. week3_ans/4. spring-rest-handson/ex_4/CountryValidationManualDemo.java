/*
 * Hands on 4 - Spring REST - Validating country code
 */
import java.util.ArrayList;
import java.util.List;

public class CountryValidationManualDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class ResponseStatusException extends RuntimeException {
        final int status;
        ResponseStatusException(int status, String message) {
            super(message);
            this.status = status;
        }
    }

    static class Country {
        // @NotNull
        // @Size(min=2, max=2, message="Country code should be 2 characters")
        String code;
        String name;

        Country(String code, String name) { this.code = code; this.name = name; }

        @Override
        public String toString() { return "Country{code=" + code + ", name=" + name + "}"; }
    }

    // simulates javax.validation ValidatorFactory / Validator against Country annotations
    static final class Validator {
        List<String> validate(Country country) {
            List<String> errors = new ArrayList<>();
            if (country.code == null) {
                errors.add("Country code should not be null");
            } else if (country.code.length() != 2) {
                errors.add("Country code should be 2 characters");
            }
            return errors;
        }
    }

    // @RequestMapping("/countries")
    static class CountryController {
        private final Logger logger;
        private final Validator validator = new Validator();

        CountryController(Logger logger) { this.logger = logger; }

        // @PostMapping()
        Country addCountry(Country country) {
            logger.info("Start");

            List<String> errors = validator.validate(country);
            if (!errors.isEmpty()) {
                throw new ResponseStatusException(400, errors.toString());
            }

            logger.info("country details=" + country);
            return country;
        }
    }

    private static final Logger LOGGER = new Logger("CountryValidationManualDemo");

    private static void invokeCurl(CountryController controller, String curlCommand, Country payload) {
        LOGGER.info("curl command: " + curlCommand);
        try {
            Country response = controller.addCountry(payload);
            LOGGER.info("HTTP/1.1 200, response body=" + response);
        } catch (ResponseStatusException e) {
            LOGGER.info("HTTP/1.1 " + e.status);
            LOGGER.info("error message=" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        CountryController countryController = new CountryController(LOGGER);

        invokeCurl(countryController,
                "curl -i -H 'Content-Type: application/json' -X POST -s -d '{\"code\":\"IN\",\"name\":\"India\"}' http://localhost:8090/countries",
                new Country("IN", "India"));

        invokeCurl(countryController,
                "curl -i -H 'Content-Type: application/json' -X POST -s -d '{\"code\":\"I\",\"name\":\"India\"}' http://localhost:8090/countries",
                new Country("I", "India"));

        LOGGER.info("End");
    }
}
