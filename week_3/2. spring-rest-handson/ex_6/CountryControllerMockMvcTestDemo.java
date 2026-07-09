// Hands on - MockMVC - Test get country service
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountryControllerMockMvcTestDemo {

    static class Country {
        String code;
        String name;

        Country(String code, String name) {
            this.code = code;
            this.name = name;
        }

        String toJson() {
            return "{\n  \"code\": \"" + code + "\",\n  \"name\": \"" + name + "\"\n}";
        }
    }

    static class CountryNotFoundException extends RuntimeException {
        static final int STATUS = 400;
        static final String REASON = "Country Not found";

        CountryNotFoundException() {
            super(REASON);
        }
    }

    static class CountryController {
        Country getCountry(String code) {
            if (!"IN".equalsIgnoreCase(code)) {
                throw new CountryNotFoundException();
            }
            return new Country("IN", "India");
        }
    }

    static class HttpResponse {
        final int status;
        final String reason;
        final String body;

        HttpResponse(int status, String reason, String body) {
            this.status = status;
            this.reason = reason;
            this.body = body;
        }
    }

    static class MockMvc {
        private final CountryController countryController;

        MockMvc(CountryController countryController) {
            this.countryController = countryController;
        }

        HttpResponse performGetCountry(String code) {
            try {
                Country country = countryController.getCountry(code);
                return new HttpResponse(200, null, country.toJson());
            } catch (CountryNotFoundException e) {
                return new HttpResponse(CountryNotFoundException.STATUS, e.getMessage(), null);
            }
        }
    }

    static String jsonPathValue(String json, String field) {
        Matcher matcher = Pattern.compile("\"" + field + "\":\\s*\"([^\"]*)\"").matcher(json);
        return matcher.find() ? matcher.group(1) : null;
    }

    static class TestRunner {
        private final List<String> results = new ArrayList<>();
        private int passed = 0;
        private int failed = 0;

        void assertTrue(String testName, boolean condition, String detail) {
            if (condition) {
                passed++;
                results.add("PASS - " + testName + " - " + detail);
            } else {
                failed++;
                results.add("FAIL - " + testName + " - " + detail);
            }
        }

        void printSummary() {
            for (String result : results) {
                System.out.println(result);
            }
            System.out.println("\nTests run: " + (passed + failed) + ", Passed: " + passed + ", Failed: " + failed);
        }
    }

    static void contextLoads(TestRunner runner, CountryController countryController) {
        runner.assertTrue("contextLoads", countryController != null, "countryController is not null");
    }

    static void testGetCountry(TestRunner runner, MockMvc mvc) {
        HttpResponse response = mvc.performGetCountry("IN");
        runner.assertTrue("testGetCountry", response.status == 200, "status().isOk() -> " + response.status);
        String code = jsonPathValue(response.body, "code");
        runner.assertTrue("testGetCountry", code != null, "$.code exists -> " + code);
        runner.assertTrue("testGetCountry", "IN".equals(code), "$.code value(\"IN\") -> " + code);
        String name = jsonPathValue(response.body, "name");
        runner.assertTrue("testGetCountry", name != null, "$.name exists -> " + name);
        runner.assertTrue("testGetCountry", "India".equals(name), "$.name value(\"India\") -> " + name);
    }

    static void testGetCountryException(TestRunner runner, MockMvc mvc) {
        HttpResponse response = mvc.performGetCountry("ZZ");
        runner.assertTrue("testGetCountryException", response.status == CountryNotFoundException.STATUS,
                "status().isBadRequest() -> " + response.status);
        runner.assertTrue("testGetCountryException", CountryNotFoundException.REASON.equals(response.reason),
                "status().reason(\"" + CountryNotFoundException.REASON + "\") -> " + response.reason);
    }

    public static void main(String[] args) {
        CountryController countryController = new CountryController();
        MockMvc mvc = new MockMvc(countryController);
        TestRunner runner = new TestRunner();

        contextLoads(runner, countryController);
        testGetCountry(runner, mvc);
        testGetCountryException(runner, mvc);

        runner.printSummary();
    }
}
