/*
 * Hands on 1 - Spring Data JPA - Quick Example
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpringDataJpaQuickExample {

    // ---- simple logger, mirrors the LOGGER.info/debug calls in the hands-on ----
    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    // ---- @Entity @Table(name="country") ----
    static class Country {
        @SuppressWarnings("unused")
        private String code; // @Id @Column(name="code")
        private String name; // @Column(name="name")

        Country() {}
        Country(String code, String name) { this.code = code; this.name = name; }

        String getCode() { return code; }
        void setCode(String code) { this.code = code; }
        String getName() { return name; }
        void setName(String name) { this.name = name; }

        @Override
        public String toString() {
            return "Country [code=" + code + ", name=" + name + "]";
        }
    }

    // ---- @Repository interface CountryRepository extends JpaRepository<Country, String> ----
    static class CountryRepository {
        private final Map<String, Country> table = new LinkedHashMap<>();

        List<Country> findAll() { return new ArrayList<>(table.values()); }

        Optional<Country> findById(String code) { return Optional.ofNullable(table.get(code)); }

        Country save(Country country) { table.put(country.getCode(), country); return country; }

        void deleteById(String code) { table.remove(code); }

        // seed data, equivalent to the two insert statements in the hands-on
        void seed() {
            save(new Country("IN", "India"));
            save(new Country("US", "United States of America"));
        }
    }

    // ---- @Service class CountryService ----
    static class CountryService {
        private final CountryRepository countryRepository; // @Autowired

        CountryService(CountryRepository countryRepository) {
            this.countryRepository = countryRepository;
        }

        // @Transactional
        List<Country> getAllCountries() {
            return countryRepository.findAll();
        }
    }

    private static final Logger LOGGER = new Logger("SpringDataJpaQuickExample");
    private static CountryService countryService;

    private static void testGetAllCountries() {
        LOGGER.info("Start");
        List<Country> countries = countryService.getAllCountries();
        LOGGER.debug("countries={}", countries);
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        // equivalent to context.getBean(CountryService.class) after
        // SpringApplication.run(OrmLearnApplication.class, args)
        CountryRepository countryRepository = new CountryRepository();
        countryRepository.seed(); // create schema ormlearn; insert into country ...
        countryService = new CountryService(countryRepository);

        testGetAllCountries();
    }
}
