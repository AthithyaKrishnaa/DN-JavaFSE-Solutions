// Hands on 4 - Spring Core - Load Country from Spring Configuration XML
import java.util.LinkedHashMap;
import java.util.Map;

public class CountryBeanDemo {

    static final String COUNTRY_XML =
            "<beans>\n" +
            "  <bean id=\"country\" class=\"com.cognizant.springlearn.Country\">\n" +
            "    <property name=\"code\" value=\"IN\" />\n" +
            "    <property name=\"name\" value=\"India\" />\n" +
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
        private static final Logger CLASS_LOGGER = new Logger("Country");
        private String code;
        private String name;

        Country() {
            CLASS_LOGGER.debug("Inside Country Constructor.");
        }

        String getCode() {
            CLASS_LOGGER.debug("Inside getCode() -> " + code);
            return code;
        }

        void setCode(String code) {
            CLASS_LOGGER.debug("Inside setCode() -> " + code);
            this.code = code;
        }

        String getName() {
            CLASS_LOGGER.debug("Inside getName() -> " + name);
            return name;
        }

        void setName(String name) {
            CLASS_LOGGER.debug("Inside setName() -> " + name);
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
            Country country = new Country();
            country.setCode("IN");
            country.setName("India");
            beans.put("country", country);
        }

        @SuppressWarnings("unchecked")
        <T> T getBean(String name, Class<T> type) {
            return (T) beans.get(name);
        }
    }

    static void displayCountry() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("country.xml", COUNTRY_XML);
        Country country = context.getBean("country", Country.class);
        LOGGER.debug("Country : " + country.toString());
    }

    public static void main(String[] args) {
        displayCountry();
    }
}
