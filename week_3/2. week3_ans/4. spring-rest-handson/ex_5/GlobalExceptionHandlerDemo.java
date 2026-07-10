/*
 * Hands on 5 - Spring REST - Include global exception handler for validation errors
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GlobalExceptionHandlerDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
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

    static class MethodArgumentNotValidException extends RuntimeException {
        final List<String> fieldErrors;
        MethodArgumentNotValidException(List<String> fieldErrors) {
            super("validation failed");
            this.fieldErrors = fieldErrors;
        }
    }

    // simulates javax.validation triggered by @Valid on the controller parameter
    static final class BeanValidator {
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

    // @ControllerAdvice class GlobalExceptionHandler extends ResponseEntityExceptionHandler
    static class GlobalExceptionHandler {
        private final Logger logger;

        GlobalExceptionHandler(Logger logger) { this.logger = logger; }

        // @Override handleMethodArgumentNotValid(...)
        Map<String, Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, int status) {
            logger.info("Start");
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", new Date());
            body.put("status", status);
            body.put("errors", ex.fieldErrors);
            logger.info("End");
            return body;
        }
    }

    // @RequestMapping("/countries")
    static class CountryController {
        private final Logger logger;
        private final BeanValidator validator = new BeanValidator();
        private final GlobalExceptionHandler exceptionHandler;

        CountryController(Logger logger, GlobalExceptionHandler exceptionHandler) {
            this.logger = logger;
            this.exceptionHandler = exceptionHandler;
        }

        // public Country addCountry(@RequestBody @Valid Country country)
        Object addCountry(Country country) {
            List<String> errors = validator.validate(country);
            if (!errors.isEmpty()) {
                // spring intercepts before the controller body runs and dispatches to the handler
                return exceptionHandler.handleMethodArgumentNotValid(new MethodArgumentNotValidException(errors), 400);
            }
            logger.info("Start");
            logger.info("country details=" + country);
            return country;
        }
    }

    private static final Logger LOGGER = new Logger("GlobalExceptionHandlerDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler(LOGGER);
        CountryController countryController = new CountryController(LOGGER, exceptionHandler);

        LOGGER.info("curl command: curl -i -H 'Content-Type: application/json' -X POST -s -d '{\"code\":\"I\",\"name\":\"India\"}' http://localhost:8090/countries");
        Object response = countryController.addCountry(new Country("I", "India"));
        LOGGER.info("HTTP/1.1 400, response body=" + response);
        LOGGER.info("note: CountryController logs are absent, confirming the global exception handler intercepted the request");

        LOGGER.info("End");
    }
}
