/*
 * Hands on 8 - Spring REST - Implement REST DELETE Service
 */
import java.util.ArrayList;
import java.util.List;

public class EmployeeDeleteServiceDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class EmployeeNotFoundException extends RuntimeException {
        EmployeeNotFoundException(String message) { super(message); }
    }

    static class Employee {
        Integer id;
        String name;

        Employee(Integer id, String name) { this.id = id; this.name = name; }

        @Override
        public String toString() { return "Employee{id=" + id + ", name=" + name + "}"; }
    }

    static class EmployeeDao {
        static final List<Employee> EMPLOYEE_LIST = new ArrayList<>();

        static {
            EMPLOYEE_LIST.add(new Employee(1, "Arun Kumar"));
            EMPLOYEE_LIST.add(new Employee(2, "Divya Sundaram"));
            EMPLOYEE_LIST.add(new Employee(3, "Karthik Raja"));
        }

        void deleteEmployee(Integer id) {
            for (int i = 0; i < EMPLOYEE_LIST.size(); i++) {
                if (EMPLOYEE_LIST.get(i).id.equals(id)) {
                    EMPLOYEE_LIST.remove(i);
                    return;
                }
            }
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }

        List<Employee> getAllEmployees() { return EMPLOYEE_LIST; }
    }

    static class EmployeeService {
        private final EmployeeDao employeeDao;

        EmployeeService(EmployeeDao employeeDao) { this.employeeDao = employeeDao; }

        void deleteEmployee(Integer id) { employeeDao.deleteEmployee(id); }
    }

    // @RequestMapping("/employees")
    static class EmployeeController {
        private final EmployeeService employeeService;
        private final Logger logger;

        EmployeeController(EmployeeService employeeService, Logger logger) {
            this.employeeService = employeeService;
            this.logger = logger;
        }

        // @DeleteMapping("/{id}")
        void deleteEmployee(Integer id) {
            logger.info("DELETE /employees/" + id + " invoked, annotation=@DeleteMapping(\"/{id}\")");
            employeeService.deleteEmployee(id);
        }
    }

    private static final Logger LOGGER = new Logger("EmployeeDeleteServiceDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        EmployeeDao employeeDao = new EmployeeDao();
        EmployeeService employeeService = new EmployeeService(employeeDao);
        EmployeeController employeeController = new EmployeeController(employeeService, LOGGER);

        LOGGER.info("getAllEmployees before delete=" + employeeDao.getAllEmployees());

        employeeController.deleteEmployee(2);
        LOGGER.info("getAllEmployees after delete=" + employeeDao.getAllEmployees());

        try {
            employeeController.deleteEmployee(99);
        } catch (EmployeeNotFoundException e) {
            LOGGER.info("caught EmployeeNotFoundException: " + e.getMessage());
        }

        LOGGER.info("End");
    }
}
