/*
 * Hands on 3 - Spring REST - EmployeeController GetMapping /employees, tested using postman
 */
import java.util.ArrayList;
import java.util.List;

public class EmployeeControllerGetAllEmployeesDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class Employee {
        String empId;
        String empName;
        String designation;
        double salary;

        Employee(String empId, String empName, String designation, double salary) {
            this.empId = empId;
            this.empName = empName;
            this.designation = designation;
            this.salary = salary;
        }

        String toJson() {
            return "{\"empId\":\"" + empId + "\",\"empName\":\"" + empName + "\",\"designation\":\""
                    + designation + "\",\"salary\":" + salary + "}";
        }
    }

    static class EmployeeDao {
        static final List<Employee> EMPLOYEE_LIST = new ArrayList<>();

        static {
            EMPLOYEE_LIST.add(new Employee("E001", "Arun Kumar", "Software Engineer", 45000.0));
            EMPLOYEE_LIST.add(new Employee("E002", "Divya Sundaram", "Senior Software Engineer", 65000.0));
            EMPLOYEE_LIST.add(new Employee("E003", "Karthik Raja", "HR Executive", 38000.0));
            EMPLOYEE_LIST.add(new Employee("E004", "Priya Natarajan", "HR Manager", 72000.0));
        }

        List<Employee> getAllEmployees() {
            return EMPLOYEE_LIST;
        }
    }

    static class EmployeeService {
        private final EmployeeDao employeeDao;

        EmployeeService(EmployeeDao employeeDao) { this.employeeDao = employeeDao; }

        List<Employee> getAllEmployees() { return employeeDao.getAllEmployees(); }
    }

    static class EmployeeController {
        private final EmployeeService employeeService;
        private final Logger logger;

        EmployeeController(EmployeeService employeeService, Logger logger) {
            this.employeeService = employeeService;
            this.logger = logger;
        }

        List<Employee> getAllEmployees() {
            logger.info("GET /employees invoked, annotation=@GetMapping(\"/employees\")");
            List<Employee> employees = employeeService.getAllEmployees();
            logger.debug("response status=200, employee count={}", employees.size());
            return employees;
        }
    }

    private static final Logger LOGGER = new Logger("EmployeeControllerGetAllEmployeesDemo");

    private static void simulatePostmanRequest(EmployeeController controller) {
        LOGGER.info("Postman request: GET http://localhost:8080/employees");
        List<Employee> employees = controller.getAllEmployees();
        StringBuilder responseBody = new StringBuilder("[");
        for (int i = 0; i < employees.size(); i++) {
            responseBody.append(employees.get(i).toJson());
            if (i != employees.size() - 1) responseBody.append(",");
        }
        responseBody.append("]");
        LOGGER.info("Postman response body: " + responseBody);
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        EmployeeDao employeeDao = new EmployeeDao();
        EmployeeService employeeService = new EmployeeService(employeeDao);
        EmployeeController employeeController = new EmployeeController(employeeService, LOGGER);

        simulatePostmanRequest(employeeController);

        LOGGER.info("End");
    }
}
