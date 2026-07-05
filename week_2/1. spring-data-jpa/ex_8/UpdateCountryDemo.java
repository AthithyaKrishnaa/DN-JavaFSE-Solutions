/*
 * Hands on 8 - Update a country based on code
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class UpdateCountryDemo {

    static class CountryNotFoundException extends Exception {
        CountryNotFoundException(String countryCode) {
            super("Country not found for code: " + countryCode);
        }
    }

    static class Country {
        String code;
        String name;
        Country(String code, String name) { this.code = code; this.name = name; }
        void setName(String name) { this.name = name; }
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

        Country findCountryByCode(String countryCode) throws CountryNotFoundException {
            Optional<Country> result = countryRepository.findById(countryCode);
            if (!result.isPresent()) throw new CountryNotFoundException(countryCode);
            return result.get();
        }

        // @Transactional
        void updateCountry(String code, String name) throws CountryNotFoundException {
            Optional<Country> result = countryRepository.findById(code);
            if (!result.isPresent()) throw new CountryNotFoundException(code);
            Country country = result.get();
            country.setName(name);
            countryRepository.save(country);
        }
    }

    private static CountryService countryService;

    private static void testUpdateCountry() throws CountryNotFoundException {
        System.out.println("Start");

        Country before = countryService.findCountryByCode("IN");
        String nameBeforeUpdate = before.name; // capture value, since 'before' shares the same
                                                // in-memory reference as the repository's record
        System.out.println("Before update: " + before);

        countryService.updateCountry("IN", "Republic of India");

        Country after = countryService.findCountryByCode("IN");
        System.out.println("After update: " + after);
        System.out.println("Name was modified: " + !nameBeforeUpdate.equals(after.name));

        System.out.println("End");
    }

    public static void main(String[] args) throws CountryNotFoundException {
        CountryRepository countryRepository = new CountryRepository();
        countryRepository.save(new Country("IN", "India"));
        countryRepository.save(new Country("US", "United States of America"));
        countryService = new CountryService(countryRepository);

        System.out.println("=== testUpdateCountry() ===");
        testUpdateCountry();
    }
}
