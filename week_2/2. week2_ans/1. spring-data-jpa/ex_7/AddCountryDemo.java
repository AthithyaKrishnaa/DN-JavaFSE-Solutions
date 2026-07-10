/*
 * Hands on 7 - Add a new country
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AddCountryDemo {

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
        int count() { return table.size(); }
    }

    static class CountryService {
        private final CountryRepository countryRepository;
        CountryService(CountryRepository countryRepository) { this.countryRepository = countryRepository; }

        // @Transactional
        Country findCountryByCode(String countryCode) throws CountryNotFoundException {
            Optional<Country> result = countryRepository.findById(countryCode);
            if (!result.isPresent()) throw new CountryNotFoundException(countryCode);
            return result.get();
        }

        // @Transactional
        void addCountry(Country country) {
            countryRepository.save(country);
        }
    }

    private static CountryService countryService;

    private static void testAddCountry() throws CountryNotFoundException {
        System.out.println("Start");

        Country newCountry = new Country("SG", "Singapore");
        countryService.addCountry(newCountry);
        System.out.println("Added: " + newCountry);

        Country fetched = countryService.findCountryByCode("SG");
        System.out.println("Fetched after add: " + fetched);
        System.out.println("Name matches: " + newCountry.name.equals(fetched.name));

        System.out.println("End");
    }

    public static void main(String[] args) throws CountryNotFoundException {
        CountryRepository countryRepository = new CountryRepository();
        countryRepository.save(new Country("IN", "India"));
        countryRepository.save(new Country("US", "United States of America"));
        countryService = new CountryService(countryRepository);

        System.out.println("=== testAddCountry() ===");
        System.out.println("Countries before add: " + countryRepository.count());
        testAddCountry();
        System.out.println("Countries after add: " + countryRepository.count());
    }
}
