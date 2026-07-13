/*
 * Hands on 3 - Spring Cloud API Gateway with Global Filter Logging
 */
import java.util.ArrayList;
import java.util.List;

public class ApiGatewayGlobalFilterDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // @RestController GreetController -> GET /greet
    static class GreetController {
        String sayHello() {
            return "Hello World!!";
        }
    }

    // spring.application.name=greet-service, server.port=8080
    static class GreetService {
        final String applicationName = "greet-service";
        final int port = 8080;
        final GreetController controller = new GreetController();
    }

    // server.port=8761, @EnableEurekaServer
    static class EurekaServer {
        final int port = 8761;
        private final List<String> registeredServices = new ArrayList<>();
        void register(String applicationName) { registeredServices.add(applicationName.toUpperCase()); }
        List<String> instancesRegistered() { return registeredServices; }
    }

    // spring.cloud.gateway.discovery.locator.enabled=true, lower-case-service-id=true
    static class ApiGateway {
        final String applicationName = "api-gateway";
        final int port = 9090;
        final boolean discoveryLocatorEnabled = true;
        final boolean lowerCaseServiceId = true;

        // routes a request path of the form /{service-id}/{path} to the registered service
        String route(EurekaServer eurekaServer, GreetService greetService, String requestPath) {
            String[] parts = requestPath.substring(1).split("/", 2);
            String serviceId = parts[0];
            String remainingPath = parts.length > 1 ? "/" + parts[1] : "/";
            boolean serviceKnown = eurekaServer.instancesRegistered().contains(serviceId.toUpperCase());
            if (serviceKnown && serviceId.equalsIgnoreCase(greetService.applicationName) && remainingPath.equals("/greet")) {
                return greetService.controller.sayHello();
            }
            return null;
        }
    }

    // @Component GlobalFilter LogFilter -> logger.info("====>Request URL {}", exchange.getRequest().getURI())
    static class LogFilter {
        private final Logger logger;
        LogFilter(Logger logger) { this.logger = logger; }
        void filter(String requestUri) {
            logger.info("====>Request URL " + requestUri);
        }
    }

    private static final Logger LOGGER = new Logger("ApiGatewayGlobalFilterDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        GreetService greetService = new GreetService();
        LOGGER.info("greet-service started, Tomcat started on port(s): " + greetService.port);

        LOGGER.info("curl command: curl -s http://localhost:8080/greet");
        LOGGER.info("HTTP/1.1 200");
        LOGGER.info("response=" + greetService.controller.sayHello());

        EurekaServer eurekaServer = new EurekaServer();
        LOGGER.info("eureka-server started, Tomcat started on port(s): " + eurekaServer.port);
        eurekaServer.register(greetService.applicationName);
        LOGGER.info("Instances currently registered with Eureka: " + eurekaServer.instancesRegistered());

        ApiGateway apiGateway = new ApiGateway();
        LOGGER.info("api-gateway started, Tomcat started on port(s): " + apiGateway.port
                + " with discovery.locator.enabled=" + apiGateway.discoveryLocatorEnabled);

        LogFilter logFilter = new LogFilter(LOGGER);

        String[] incomingRequests = { "/GREET-SERVICE/greet", "/greet-service/greet" };
        for (String requestPath : incomingRequests) {
            String requestUri = "http://localhost:" + apiGateway.port + requestPath;
            LOGGER.info("curl command: curl -s " + requestUri);
            logFilter.filter(requestUri);
            String response = apiGateway.route(eurekaServer, greetService, requestPath.toLowerCase());
            if (response != null) {
                LOGGER.info("HTTP/1.1 200");
                LOGGER.info("response=" + response);
            } else {
                LOGGER.info("HTTP/1.1 404");
            }
        }

        LOGGER.info("End");
    }
}
