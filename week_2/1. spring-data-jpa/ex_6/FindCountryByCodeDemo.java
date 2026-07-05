/*
 * Hands on 6 - Find a country based on country code
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class FindCountryByCodeDemo {

    // com.cognizant.spring-learn.service.exception.CountryNotFoundException
    static class CountryNotFoundException extends Exception {
        CountryNotFoundException(String countryCode) {
            super("Country not found for code: " + countryCode);
        }
    }

    static class Country {
        String code;
        String name;
        Country(String code, String name) { this.code = code; this.name = name; }
        @Override public String toString() { return "Country [code=" + code + ", name=" + name + "]"; }
    }

    static class CountryRepository {
        private final Map<String, Country> table = new LinkedHashMap<>();
        Country save(Country c) { table.put(c.code, c); return c; }
        Optional<Country> findById(String code) { return Optional.ofNullable(table.get(code)); }
    }

    static class CountryService {
        private final CountryRepository countryRepository;
        CountryService(CountryRepository countryRepository) { this.countryRepository = countryRepository; }

        // @Transactional
        Country findCountryByCode(String countryCode) throws CountryNotFoundException {
            Optional<Country> result = countryRepository.findById(countryCode);
            if (!result.isPresent()) {
                throw new CountryNotFoundException(countryCode);
            }
            Country country = result.get();
            return country;
        }
    }

    private static CountryService countryService;

    private static void getAllCountriesTest() {
        System.out.println("Start");
        try {
            Country country = countryService.findCountryByCode("IN");
            System.out.println("Country:" + country);
            boolean matches = "India".equals(country.name);
            System.out.println("Name matches expected value 'India': " + matches);
        } catch (CountryNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.println("End");
    }

    private static void notFoundTest() {
        System.out.println("Start");
        try {
            countryService.findCountryByCode("ZZ");
            System.out.println("Unexpected: no exception thrown");
        } catch (CountryNotFoundException e) {
            System.out.println("Caught expected CountryNotFoundException: " + e.getMessage());
        }
        System.out.println("End");
    }

    public static void main(String[] args) {
        CountryRepository countryRepository = new CountryRepository();
        countryRepository.save(new Country("IN", "India"));
        countryRepository.save(new Country("US", "United States of America"));
        countryService = new CountryService(countryRepository);

        System.out.println("=== findCountryByCode(\"IN\") ===");
        getAllCountriesTest();

        System.out.println("\n=== findCountryByCode(\"ZZ\") - not present ===");
        notFoundTest();
    }
}
