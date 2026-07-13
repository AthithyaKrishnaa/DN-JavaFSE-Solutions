/*
 * Hands on 1 - Creating Microservices for Account and Loan
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class AccountLoanMicroservicesDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // @RestController AccountController -> GET /accounts/{number}
    static class AccountController {
        Map<String, Object> getAccount(String number) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("number", number);
            response.put("type", "savings");
            response.put("balance", 234343);
            return response;
        }
    }

    // @RestController LoanController -> GET /loans/{number}
    static class LoanController {
        Map<String, Object> getLoan(String number) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("number", number);
            response.put("type", "car");
            response.put("loan", 400000);
            response.put("emi", 3258);
            response.put("tenure", 18);
            return response;
        }
    }

    // spring.application.name + server.port for each microservice
    static class MicroserviceApplication {
        final String applicationName;
        final int configuredPort;
        boolean running = false;

        MicroserviceApplication(String applicationName, int configuredPort) {
            this.applicationName = applicationName;
            this.configuredPort = configuredPort;
        }

        // returns true if the port bind succeeds
        boolean start(boolean portAlreadyInUse) {
            if (portAlreadyInUse) {
                running = false;
                return false;
            }
            running = true;
            return true;
        }
    }

    private static final Logger LOGGER = new Logger("AccountLoanMicroservicesDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        AccountController accountController = new AccountController();
        LoanController loanController = new LoanController();

        LOGGER.info("mvn clean package - account project BUILD SUCCESS");
        MicroserviceApplication accountApp = new MicroserviceApplication("account", 8080);
        accountApp.start(false);
        LOGGER.info("account application started, Tomcat started on port(s): " + accountApp.configuredPort);

        LOGGER.info("curl command: curl -s http://localhost:8080/accounts/00987987973432");
        LOGGER.info("HTTP/1.1 200");
        LOGGER.info("response=" + accountController.getAccount("00987987973432"));

        LOGGER.info("mvn clean package - loan project BUILD SUCCESS");
        MicroserviceApplication loanAppDefaultPort = new MicroserviceApplication("loan", 8080);
        boolean started = loanAppDefaultPort.start(true);
        if (!started) {
            LOGGER.info("APPLICATION FAILED TO START");
            LOGGER.info("Description: Web server failed to start. Port 8080 was already in use.");
        }

        LOGGER.info("adding server.port=8081 to application.properties of loan project");
        MicroserviceApplication loanApp = new MicroserviceApplication("loan", 8081);
        loanApp.start(false);
        LOGGER.info("loan application started, Tomcat started on port(s): " + loanApp.configuredPort);

        LOGGER.info("curl command: curl -s http://localhost:8081/loans/H00987987972342");
        LOGGER.info("HTTP/1.1 200");
        LOGGER.info("response=" + loanController.getLoan("H00987987972342"));

        LOGGER.info("End");
    }
}
