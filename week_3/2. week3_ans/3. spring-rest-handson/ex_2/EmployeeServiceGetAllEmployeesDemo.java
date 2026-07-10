/*
 * Hands on 2 - Spring REST - Employee Service getAllEmployees with Transactional
 */
import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceGetAllEmployeesDemo {

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

        @Override
        public String toString() {
            return "Employee{empId=" + empId + ", empName=" + empName + ", designation=" + designation
                    + ", salary=" + salary + "}";
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
        private final Logger logger;

        EmployeeService(EmployeeDao employeeDao, Logger logger) {
            this.employeeDao = employeeDao;
            this.logger = logger;
        }

        List<Employee> getAllEmployees() {
            logger.info("EmployeeService.getAllEmployees invoked, annotation=@Transactional");
            List<Employee> employees = employeeDao.getAllEmployees();
            logger.debug("employees fetched from dao, count={}", employees.size());
            return employees;
        }
    }

    private static final Logger LOGGER = new Logger("EmployeeServiceGetAllEmployeesDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        EmployeeDao employeeDao = new EmployeeDao();
        EmployeeService employeeService = new EmployeeService(employeeDao, LOGGER);

        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee employee : employees) {
            LOGGER.info(employee.toString());
        }

        LOGGER.info("End");
    }
}
