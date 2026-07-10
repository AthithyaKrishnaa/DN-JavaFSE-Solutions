/*
 * Hands on 5 - Get all employees using Native Query
 */
import java.util.ArrayList;
import java.util.List;

public class AllEmployeesNativeQueryDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class Employee {
        private int id;
        private String name;
        private double salary;
        Employee(int id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }
        @Override
        public String toString() {
            return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + "]";
        }
    }

    static class NativeQueryExecutor {
        private final List<String[]> employeeTable;
        NativeQueryExecutor(List<String[]> employeeTable) { this.employeeTable = employeeTable; }

        List<Employee> execute(String sql) {
            List<Employee> result = new ArrayList<>();
            for (String[] row : employeeTable) {
                result.add(new Employee(Integer.parseInt(row[0]), row[1], Double.parseDouble(row[2])));
            }
            return result;
        }
    }

    static class EmployeeRepository {
        private final NativeQueryExecutor nativeQueryExecutor;
        EmployeeRepository(NativeQueryExecutor nativeQueryExecutor) { this.nativeQueryExecutor = nativeQueryExecutor; }
        List<Employee> getAllEmployeesNative() { return nativeQueryExecutor.execute("SELECT * FROM employee"); }
    }

    static class EmployeeService {
        private final EmployeeRepository employeeRepository;
        EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }
        List<Employee> getAllEmployeesNative() { return employeeRepository.getAllEmployeesNative(); }
    }

    private static final Logger LOGGER = new Logger("AllEmployeesNativeQueryDemo");
    private static EmployeeService employeeService;

    private static void testGetAllEmployeesNative() {
        LOGGER.info("Start");
        List<Employee> employees = employeeService.getAllEmployeesNative();
        LOGGER.debug("employees={}", employees);
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        List<String[]> employeeTable = List.of(
                new String[]{"1", "John Smith", "55000"},
                new String[]{"2", "Priya Nair", "62000"},
                new String[]{"3", "Michael Brown", "48000"});

        NativeQueryExecutor nativeQueryExecutor = new NativeQueryExecutor(employeeTable);
        EmployeeRepository employeeRepository = new EmployeeRepository(nativeQueryExecutor);
        employeeService = new EmployeeService(employeeRepository);

        testGetAllEmployeesNative();
    }
}
