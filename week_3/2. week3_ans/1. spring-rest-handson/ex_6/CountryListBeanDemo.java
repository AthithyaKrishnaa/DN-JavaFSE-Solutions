// Hands on 6 - Spring Core - Load list of countries from Spring Configuration XML
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CountryListBeanDemo {

    static final String COUNTRY_XML =
            "<beans>\n" +
            "  <bean id=\"in\" class=\"com.cognizant.springlearn.Country\">\n" +
            "    <property name=\"code\" value=\"IN\" />\n" +
            "    <property name=\"name\" value=\"India\" />\n" +
            "  </bean>\n" +
            "  <bean id=\"us\" class=\"com.cognizant.springlearn.Country\">\n" +
            "    <property name=\"code\" value=\"US\" />\n" +
            "    <property name=\"name\" value=\"United States\" />\n" +
            "  </bean>\n" +
            "  <bean id=\"de\" class=\"com.cognizant.springlearn.Country\">\n" +
            "    <property name=\"code\" value=\"DE\" />\n" +
            "    <property name=\"name\" value=\"Germany\" />\n" +
            "  </bean>\n" +
            "  <bean id=\"jp\" class=\"com.cognizant.springlearn.Country\">\n" +
            "    <property name=\"code\" value=\"JP\" />\n" +
            "    <property name=\"name\" value=\"Japan\" />\n" +
            "  </bean>\n" +
            "  <bean id=\"countryList\" class=\"java.util.ArrayList\">\n" +
            "    <constructor-arg>\n" +
            "      <list>\n" +
            "        <ref bean=\"in\"/>\n" +
            "        <ref bean=\"us\"/>\n" +
            "        <ref bean=\"de\"/>\n" +
            "        <ref bean=\"jp\"/>\n" +
            "      </list>\n" +
            "    </constructor-arg>\n" +
            "  </bean>\n" +
            "</beans>";

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg) { print("DEBUG", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    private static final Logger LOGGER = new Logger("SpringLearnApplication");

    static class Country {
        private String code;
        private String name;

        Country(String code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Country [code=" + code + ", name=" + name + "]";
        }
    }

    static class ClassPathXmlApplicationContext {
        private final Map<String, Object> beans = new LinkedHashMap<>();

        ClassPathXmlApplicationContext(String xmlFile, String xmlContent) {
            System.out.println("Loading " + xmlFile + ":\n" + xmlContent);
            beans.put("in", new Country("IN", "India"));
            beans.put("us", new Country("US", "United States"));
            beans.put("de", new Country("DE", "Germany"));
            beans.put("jp", new Country("JP", "Japan"));

            List<Country> countryList = new ArrayList<>();
            countryList.add((Country) beans.get("in"));
            countryList.add((Country) beans.get("us"));
            countryList.add((Country) beans.get("de"));
            countryList.add((Country) beans.get("jp"));
            beans.put("countryList", countryList);
        }

        @SuppressWarnings("unchecked")
        <T> T getBean(String name, Class<T> type) {
            return (T) beans.get(name);
        }
    }

    static void displayCountries() {
        LOGGER.info("START");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("country.xml", COUNTRY_XML);
        List<Country> countryList = context.getBean("countryList", List.class);
        LOGGER.debug("Countries : " + countryList);

        LOGGER.info("END");
    }

    public static void main(String[] args) {
        displayCountries();
    }
}
