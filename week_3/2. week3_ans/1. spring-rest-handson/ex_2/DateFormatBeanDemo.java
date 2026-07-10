// Hands on 2 - Spring Core - Load SimpleDateFormat from Spring Configuration XML
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class DateFormatBeanDemo {

    static final String DATE_FORMAT_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<beans xmlns=\"http://www.springframework.org/schema/beans\">\n" +
            "  <bean id=\"dateFormat\" class=\"java.text.SimpleDateFormat\">\n" +
            "    <constructor-arg value=\"dd/MM/yyyy\" />\n" +
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

    static class ClassPathXmlApplicationContext {
        private final Map<String, Object> beans = new LinkedHashMap<>();

        ClassPathXmlApplicationContext(String xmlFile, String xmlContent) {
            System.out.println("Loading " + xmlFile + ":\n" + xmlContent);
            beans.put("dateFormat", new SimpleDateFormat("dd/MM/yyyy"));
        }

        @SuppressWarnings("unchecked")
        <T> T getBean(String name, Class<T> type) {
            return (T) beans.get(name);
        }
    }

    static void displayDate() throws ParseException {
        LOGGER.info("START");

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("date-format.xml", DATE_FORMAT_XML);
        SimpleDateFormat format = context.getBean("dateFormat", SimpleDateFormat.class);

        Date date = format.parse("31/12/2018");
        LOGGER.debug("Parsed date: " + date);
        System.out.println(date);

        LOGGER.info("END");
    }

    public static void main(String[] args) throws ParseException {
        displayDate();
    }
}
