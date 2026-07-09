// Hands on 5 - Spring Core - Demonstration of Singleton Scope and Prototype Scope
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanScopeDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
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

        void setCode(String code) { this.code = code; }
        void setName(String name) { this.name = name; }

        @Override
        public String toString() {
            return "Country [code=" + code + ", name=" + name + "]";
        }
    }

    static class ClassPathXmlApplicationContext {
        private final Map<String, Country> singletonBeans = new LinkedHashMap<>();
        private final boolean prototype;

        ClassPathXmlApplicationContext(boolean prototype) {
            this.prototype = prototype;
            System.out.println("Loading country.xml with scope=\"" + (prototype ? "prototype" : "singleton") + "\"");
            if (!prototype) {
                Country country = new Country();
                country.setCode("IN");
                country.setName("India");
                singletonBeans.put("country", country);
            }
        }

        Country getBean(String name, Class<Country> type) {
            if (prototype) {
                Country country = new Country();
                country.setCode("IN");
                country.setName("India");
                return country;
            }
            return singletonBeans.get(name);
        }
    }

    static void demonstrateSingletonScope() {
        System.out.println("\n-- Singleton scope --");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(false);
        Country country = context.getBean("country", Country.class);
        Country anotherCountry = context.getBean("country", Country.class);
        LOGGER.debug("country == anotherCountry -> " + (country == anotherCountry));
    }

    static void demonstratePrototypeScope() {
        System.out.println("\n-- Prototype scope --");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(true);
        Country country = context.getBean("country", Country.class);
        Country anotherCountry = context.getBean("country", Country.class);
        LOGGER.debug("country == anotherCountry -> " + (country == anotherCountry));
    }

    public static void main(String[] args) {
        demonstrateSingletonScope();
        demonstratePrototypeScope();
    }
}
