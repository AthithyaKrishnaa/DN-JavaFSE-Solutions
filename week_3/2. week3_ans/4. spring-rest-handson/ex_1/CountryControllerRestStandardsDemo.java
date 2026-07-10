/*
 * Hands on 1 - Spring REST - Modify CountryController to adhere to REST URL naming standards
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CountryControllerRestStandardsDemo {

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

        Country(String code, String name) { this.code = code; this.name = name; }

        @Override
        public String toString() { return "Country{code=" + code + ", name=" + name + "}"; }
    }

    static class CountryDao {
        static final Map<String, Country> TABLE = new LinkedHashMap<>();

        static {
            TABLE.put("IN", new Country("IN", "India"));
            TABLE.put("US", new Country("US", "United States of America"));
        }

        List<Country> findAll() { return new ArrayList<>(TABLE.values()); }

        Country findByCode(String code) { return TABLE.get(code); }

        Country save(Country country) { TABLE.put(country.code, country); return country; }

        void deleteByCode(String code) { TABLE.remove(code); }
    }

    // @RequestMapping("/countries") at class level
    static class CountryController {
        private final CountryDao countryDao;
        private final Logger logger;

        CountryController(CountryDao countryDao, Logger logger) {
            this.countryDao = countryDao;
            this.logger = logger;
        }

        // @GetMapping
        List<Country> getAllCountries() {
            logger.info("GET /countries invoked, annotation=@GetMapping");
            return countryDao.findAll();
        }

        // @GetMapping("/{id}")
        Country getCountry(String code) {
            logger.info("GET /countries/" + code + " invoked, annotation=@GetMapping(\"/{id}\")");
            return countryDao.findByCode(code);
        }

        // @PostMapping
        Country addCountry(Country country) {
            logger.info("POST /countries invoked, annotation=@PostMapping");
            return countryDao.save(country);
        }

        // @PutMapping
        Country updateCountry(Country country) {
            logger.info("PUT /countries invoked, annotation=@PutMapping");
            return countryDao.save(country);
        }

        // @DeleteMapping("/{id}")
        void deleteCountry(String code) {
            logger.info("DELETE /countries/" + code + " invoked, annotation=@DeleteMapping(\"/{id}\")");
            countryDao.deleteByCode(code);
        }
    }

    private static final Logger LOGGER = new Logger("CountryControllerRestStandardsDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main, base RequestMapping=/countries");

        CountryDao countryDao = new CountryDao();
        CountryController countryController = new CountryController(countryDao, LOGGER);

        LOGGER.info("getAllCountries result: " + countryController.getAllCountries());
        LOGGER.info("getCountry(IN) result: " + countryController.getCountry("IN"));
        LOGGER.info("addCountry result: " + countryController.addCountry(new Country("SG", "Singapore")));
        LOGGER.info("updateCountry result: " + countryController.updateCountry(new Country("SG", "Republic of Singapore")));
        countryController.deleteCountry("SG");
        LOGGER.info("getAllCountries after delete: " + countryController.getAllCountries());

        LOGGER.info("End");
    }
}
