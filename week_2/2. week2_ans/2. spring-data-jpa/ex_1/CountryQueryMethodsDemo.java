/*
 * Hands on 1 - Write queries on country table using Query Methods
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CountryQueryMethodsDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class Country {
        private String code;
        private String name;

        Country(String code, String name) { this.code = code; this.name = name; }

        String getCode() { return code; }
        String getName() { return name; }

        @Override
        public String toString() {
            return "Country [code=" + code + ", name=" + name + "]";
        }
    }

    static class CountryRepository {
        private final Map<String, Country> table = new LinkedHashMap<>();

        Country save(Country country) { table.put(country.getCode(), country); return country; }

        List<Country> findByNameContainingIgnoreCase(String text) {
            List<Country> result = new ArrayList<>();
            for (Country country : table.values()) {
                if (country.getName().toLowerCase().contains(text.toLowerCase())) {
                    result.add(country);
                }
            }
            return result;
        }

        List<Country> findByNameContainingIgnoreCaseOrderByNameAsc(String text) {
            List<Country> result = findByNameContainingIgnoreCase(text);
            result.sort((a, b) -> a.getName().compareTo(b.getName()));
            return result;
        }

        List<Country> findByNameStartingWith(String prefix) {
            List<Country> result = new ArrayList<>();
            for (Country country : table.values()) {
                if (country.getName().startsWith(prefix)) {
                    result.add(country);
                }
            }
            return result;
        }

        void seed() {
            save(new Country("IN", "India"));
            save(new Country("US", "United States of America"));
            save(new Country("GB", "United Kingdom"));
            save(new Country("DE", "Germany"));
            save(new Country("FR", "France"));
            save(new Country("JP", "Japan"));
            save(new Country("CA", "Canada"));
            save(new Country("AU", "Australia"));
            save(new Country("BR", "Brazil"));
            save(new Country("CN", "China"));
            save(new Country("BV", "Bouvet Island"));
            save(new Country("DJ", "Djibouti"));
            save(new Country("GP", "Guadeloupe"));
            save(new Country("GS", "South Georgia and the South Sandwich Islands"));
            save(new Country("LU", "Luxembourg"));
            save(new Country("SS", "South Sudan"));
            save(new Country("TF", "French Southern Territories"));
            save(new Country("UM", "United States Minor Outlying Islands"));
            save(new Country("ZA", "South Africa"));
            save(new Country("ZM", "Zambia"));
            save(new Country("ZW", "Zimbabwe"));
        }
    }

    private static final Logger LOGGER = new Logger("CountryQueryMethodsDemo");
    private static CountryRepository countryRepository;

    private static void testFindByNameContainingIgnoreCase() {
        LOGGER.info("Start");
        List<Country> countries = countryRepository.findByNameContainingIgnoreCase("ou");
        LOGGER.debug("countries={}", countries);
        LOGGER.info("End");
    }

    private static void testFindByNameContainingIgnoreCaseOrderByNameAsc() {
        LOGGER.info("Start");
        List<Country> countries = countryRepository.findByNameContainingIgnoreCaseOrderByNameAsc("ou");
        LOGGER.debug("countries={}", countries);
        LOGGER.info("End");
    }

    private static void testFindByNameStartingWith() {
        LOGGER.info("Start");
        List<Country> countries = countryRepository.findByNameStartingWith("Z");
        LOGGER.debug("countries={}", countries);
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        countryRepository = new CountryRepository();
        countryRepository.seed();

        testFindByNameContainingIgnoreCase();
        testFindByNameContainingIgnoreCaseOrderByNameAsc();
        testFindByNameStartingWith();
    }
}
