/*
 * Hands on 7 - Spring REST - Implement REST service for updating an employee
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmployeeUpdateServiceDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // @ResponseStatus(HttpStatus.NOT_FOUND)
    static class EmployeeNotFoundException extends RuntimeException {
        EmployeeNotFoundException(String message) { super(message); }
    }

    static class Employee {
        Integer id;
        String name;
        Double salary;

        Employee(Integer id, String name, Double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

        @Override
        public String toString() { return "Employee{id=" + id + ", name=" + name + ", salary=" + salary + "}"; }
    }

    static class EmployeeDao {
        static final List<Employee> EMPLOYEE_LIST = new ArrayList<>();

        static {
            EMPLOYEE_LIST.add(new Employee(1, "Arun Kumar", 45000.0));
            EMPLOYEE_LIST.add(new Employee(2, "Divya Sundaram", 65000.0));
        }

        Employee updateEmployee(Employee employee) {
            for (int i = 0; i < EMPLOYEE_LIST.size(); i++) {
                if (EMPLOYEE_LIST.get(i).id.equals(employee.id)) {
                    EMPLOYEE_LIST.set(i, employee);
                    return employee;
                }
            }
            throw new EmployeeNotFoundException("Employee with id " + employee.id + " not found");
        }

        List<Employee> getAllEmployees() { return EMPLOYEE_LIST; }
    }

    static class EmployeeService {
        private final EmployeeDao employeeDao;

        EmployeeService(EmployeeDao employeeDao) { this.employeeDao = employeeDao; }

        Employee updateEmployee(Employee employee) { return employeeDao.updateEmployee(employee); }
    }

    // handles the scenario where the id field receives a non numeric value,
    // which fails during JSON to bean conversion before validation is reached
    static class GlobalExceptionHandler {
        Map<String, Object> handleHttpMessageNotReadable(String invalidField, int status) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", new Date());
            body.put("status", status);
            body.put("error", "Bad Request");
            body.put("message", "Incorrect format for field '" + invalidField + "'");
            return body;
        }

        Map<String, Object> handleEmployeeNotFound(EmployeeNotFoundException ex, int status) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", new Date());
            body.put("status", status);
            body.put("error", "Not Found");
            body.put("message", ex.getMessage());
            return body;
        }
    }

    // @RequestMapping("/employees")
    static class EmployeeController {
        private final EmployeeService employeeService;
        private final GlobalExceptionHandler exceptionHandler;
        private final Logger logger;

        EmployeeController(EmployeeService employeeService, GlobalExceptionHandler exceptionHandler, Logger logger) {
            this.employeeService = employeeService;
            this.exceptionHandler = exceptionHandler;
            this.logger = logger;
        }

        // public void updateEmployee(@RequestBody @Valid Employee employee) throws EmployeeNotFoundException
        // @PutMapping
        Object updateEmployee(Employee employee) {
            logger.info("PUT /employees invoked, annotation=@PutMapping");
            try {
                return employeeService.updateEmployee(employee);
            } catch (EmployeeNotFoundException e) {
                return exceptionHandler.handleEmployeeNotFound(e, 404);
            }
        }

        Object updateEmployeeWithInvalidId(String rawIdField) {
            logger.info("PUT /employees invoked with non numeric id, dispatched to global exception handler");
            return exceptionHandler.handleHttpMessageNotReadable("id", 400);
        }
    }

    private static final Logger LOGGER = new Logger("EmployeeUpdateServiceDemo");

    private static void mockMvcExceptionScenario(EmployeeController controller) {
        LOGGER.info("MockMvc test: PUT /employees with id='abc' (non numeric)");
        Object response = controller.updateEmployeeWithInvalidId("abc");
        LOGGER.info("MockMvc expected status=400, response body=" + response);
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        EmployeeDao employeeDao = new EmployeeDao();
        EmployeeService employeeService = new EmployeeService(employeeDao);
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        EmployeeController employeeController = new EmployeeController(employeeService, exceptionHandler, LOGGER);

        Object updateResult = employeeController.updateEmployee(new Employee(1, "Arun Kumar", 52000.0));
        LOGGER.info("update result=" + updateResult);
        LOGGER.info("getAllEmployees after update=" + employeeDao.getAllEmployees());

        Object notFoundResult = employeeController.updateEmployee(new Employee(99, "Unknown", 1000.0));
        LOGGER.info("update result (not found)=" + notFoundResult);

        mockMvcExceptionScenario(employeeController);

        LOGGER.info("End");
    }
}
