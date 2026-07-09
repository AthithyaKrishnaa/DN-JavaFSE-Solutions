// Hands on 1 - Create a Spring Web Project using Maven
public class SpringLearnApplicationDemo {

    static final String POM_XML =
            "<project>\n" +
            "  <groupId>com.cognizant</groupId>\n" +
            "  <artifactId>spring-learn</artifactId>\n" +
            "  <version>0.0.1-SNAPSHOT</version>\n" +
            "  <dependencies>\n" +
            "    <dependency><artifactId>spring-boot-starter-web</artifactId></dependency>\n" +
            "    <dependency><artifactId>spring-boot-devtools</artifactId><scope>runtime</scope></dependency>\n" +
            "  </dependencies>\n" +
            "</project>";

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    private static final Logger LOGGER = new Logger("SpringLearnApplication");

    static class SpringApplication {
        static void run(Class<?> applicationClass, String[] args) {
            System.out.println("Starting SpringLearnApplication using Spring Boot");
            System.out.println("Embedded Tomcat started on port 8080");
        }
    }

    public static void main(String[] args) {
        LOGGER.info("START main()");
        SpringApplication.run(SpringLearnApplicationDemo.class, args);
        LOGGER.info("SpringLearnApplication started successfully");
        LOGGER.info("END main()");

        System.out.println("\npom.xml:\n" + POM_XML);
        System.out.println("\nsrc/main/java      -> application code");
        System.out.println("src/main/resources -> application configuration");
        System.out.println("src/test/java      -> test code");
    }
}
