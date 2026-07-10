// Hands on 3 - Spring Core - Incorporate Logging
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggingIncorporationDemo {

    static final String APPLICATION_PROPERTIES =
            "logging.level.org.springframework=info\n" +
            "logging.level.com.cognizant.springlearn=debug\n" +
            "logging.pattern.console=%d{yyMMdd}|%d{HH:mm:ss.SSS}|%-20.20thread|%5p|%-25.25logger{25}|%25M|%m%n";

    enum Level { TRACE, DEBUG, INFO, WARN, ERROR }

    static final class Logger {
        private final String name;
        private final Level threshold;

        Logger(String name, Level threshold) {
            this.name = name;
            this.threshold = threshold;
        }

        void info(String msg) { log(Level.INFO, msg); }
        void debug(String msg) { log(Level.DEBUG, msg); }

        private void log(Level level, String msg) {
            if (level.ordinal() < threshold.ordinal()) return;
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    private static final Logger LOGGER = new Logger("SpringLearnApplication", Level.DEBUG);

    static void displayDate() throws ParseException {
        LOGGER.info("START");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse("31/12/2018");
        LOGGER.debug(date.toString());

        LOGGER.info("END");
    }

    public static void main(String[] args) throws ParseException {
        System.out.println("application.properties:\n" + APPLICATION_PROPERTIES);
        System.out.println();
        displayDate();
    }
}
