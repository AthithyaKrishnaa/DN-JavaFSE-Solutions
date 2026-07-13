/*
 * Hands on 2 - Creating Eureka Discovery Server and Registering Microservices
 */
import java.util.ArrayList;
import java.util.List;

public class EurekaServiceRegistrationDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // server.port=8761, eureka.client.register-with-eureka=false, eureka.client.fetch-registry=false
    static class EurekaDiscoveryServer {
        private final List<String> registeredServices = new ArrayList<>();
        final int port = 8761;

        void register(String applicationName) {
            registeredServices.add(applicationName.toUpperCase());
        }

        List<String> instancesRegistered() {
            return registeredServices;
        }
    }

    // spring.application.name=account-service / loan-service, @EnableDiscoveryClient
    static class DiscoveryClientApplication {
        final String applicationName;
        DiscoveryClientApplication(String applicationName) { this.applicationName = applicationName; }
    }

    private static final Logger LOGGER = new Logger("EurekaServiceRegistrationDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        EurekaDiscoveryServer eurekaServer = new EurekaDiscoveryServer();
        LOGGER.info("eureka-discovery-server started, Tomcat started on port(s): " + eurekaServer.port);

        LOGGER.info("curl command: curl -s http://localhost:8761/");
        LOGGER.info("Instances currently registered with Eureka: " + eurekaServer.instancesRegistered());

        DiscoveryClientApplication accountApp = new DiscoveryClientApplication("account-service");
        LOGGER.info("account application started with @EnableDiscoveryClient, spring.application.name=" + accountApp.applicationName);
        eurekaServer.register(accountApp.applicationName);
        LOGGER.info("DiscoveryClient_ACCOUNT-SERVICE/account - registration status: 204");

        LOGGER.info("curl command: curl -s http://localhost:8761/");
        LOGGER.info("Instances currently registered with Eureka: " + eurekaServer.instancesRegistered());

        DiscoveryClientApplication loanApp = new DiscoveryClientApplication("loan-service");
        LOGGER.info("loan application started with @EnableDiscoveryClient, spring.application.name=" + loanApp.applicationName);
        eurekaServer.register(loanApp.applicationName);
        LOGGER.info("DiscoveryClient_LOAN-SERVICE/loan - registration status: 204");

        LOGGER.info("curl command: curl -s http://localhost:8761/");
        LOGGER.info("Instances currently registered with Eureka: " + eurekaServer.instancesRegistered());

        LOGGER.info("End");
    }
}
