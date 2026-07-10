/*
 * Hands on 9 - Delete a country based on code
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class DeleteCountryDemo {

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
        void deleteById(String code) { table.remove(code); }
        int count() { return table.size(); }
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
        void addCountry(Country country) {
            countryRepository.save(country);
        }

        // @Transactional
        void deleteCountry(String code) {
            countryRepository.deleteById(code);
        }
    }

    private static CountryService countryService;

    private static void testDeleteCountry() {
        System.out.println("Start");

        // country added during the "add country" hands-on
        Country newCountry = new Country("SG", "Singapore");
        countryService.addCountry(newCountry);
        System.out.println("Added: " + newCountry);

        countryService.deleteCountry("SG");
        System.out.println("Deleted country with code SG");

        try {
            countryService.findCountryByCode("SG");
            System.out.println("Unexpected: country still present after delete");
        } catch (CountryNotFoundException e) {
            System.out.println("Confirmed deleted -> " + e.getMessage());
        }

        System.out.println("End");
    }

    public static void main(String[] args) {
        CountryRepository countryRepository = new CountryRepository();
        countryRepository.save(new Country("IN", "India"));
        countryRepository.save(new Country("US", "United States of America"));
        countryService = new CountryService(countryRepository);

        System.out.println("=== testDeleteCountry() ===");
        System.out.println("Countries before: " + countryRepository.count());
        testDeleteCountry();
        System.out.println("Countries after: " + countryRepository.count());
    }
}
