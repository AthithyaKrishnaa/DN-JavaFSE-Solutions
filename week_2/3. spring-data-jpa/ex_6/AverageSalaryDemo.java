/*
 * Hands on 4 - Get average salary using HQL
 */
import java.util.List;

public class AverageSalaryDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-20s %s%n", "main", level, name, msg);
        }
    }

    static class Employee {
        private int id;
        private double salary;
        private int departmentId;
        Employee(int id, double salary, int departmentId) {
            this.id = id;
            this.salary = salary;
            this.departmentId = departmentId;
        }
    }

    static class EmployeeRepository {
        private final List<Employee> table;
        EmployeeRepository(List<Employee> table) { this.table = table; }

        double getAverageSalary() {
            double total = 0;
            for (Employee employee : table) {
                total += employee.salary;
            }
            return total / table.size();
        }

        double getAverageSalary(int id) {
            double total = 0;
            int count = 0;
            for (Employee employee : table) {
                if (employee.departmentId == id) {
                    total += employee.salary;
                    count++;
                }
            }
            return count == 0 ? 0 : total / count;
        }
    }

    static class EmployeeService {
        private final EmployeeRepository employeeRepository;
        EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }
        double getAverageSalary() { return employeeRepository.getAverageSalary(); }
        double getAverageSalary(int id) { return employeeRepository.getAverageSalary(id); }
    }

    private static final Logger LOGGER = new Logger("AverageSalaryDemo");
    private static EmployeeService employeeService;

    private static void testGetAverageSalary() {
        LOGGER.info("Start");
        double averageSalary = employeeService.getAverageSalary();
        LOGGER.debug("Average Salary:{}", averageSalary);
        LOGGER.info("End");
    }

    private static void testGetAverageSalaryByDepartment() {
        LOGGER.info("Start");
        double averageSalaryIt = employeeService.getAverageSalary(1);
        LOGGER.debug("Average Salary for department 1:{}", averageSalaryIt);
        double averageSalaryHr = employeeService.getAverageSalary(2);
        LOGGER.debug("Average Salary for department 2:{}", averageSalaryHr);
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        EmployeeRepository employeeRepository = new EmployeeRepository(List.of(
                new Employee(1, 55000, 1),
                new Employee(2, 62000, 1),
                new Employee(3, 48000, 2),
                new Employee(4, 51000, 2)));
        employeeService = new EmployeeService(employeeRepository);

        testGetAverageSalary();
        testGetAverageSalaryByDepartment();
    }
}
