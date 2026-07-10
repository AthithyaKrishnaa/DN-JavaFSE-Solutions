/*
 * Hands on 4 - Implement many to one relationship between Employee and Department: Update Employee
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class UpdateEmployeeDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class Department {
        private int id;
        private String name;

        Department(int id, String name) { this.id = id; this.name = name; }

        int getId() { return id; }
        String getName() { return name; }

        @Override
        public String toString() {
            return "Department [id=" + id + ", name=" + name + "]";
        }
    }

    static class Employee {
        private int id;
        private String name;
        private Department department;

        Employee(int id, String name, Department department) {
            this.id = id;
            this.name = name;
            this.department = department;
        }

        int getId() { return id; }
        String getName() { return name; }
        Department getDepartment() { return department; }
        void setDepartment(Department department) { this.department = department; }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", name=" + name + ", department=" + department + "]";
        }
    }

    static class DepartmentRepository {
        private final Map<Integer, Department> table = new LinkedHashMap<>();
        Department save(Department department) { table.put(department.getId(), department); return department; }
        Optional<Department> findById(int id) { return Optional.ofNullable(table.get(id)); }
    }

    static class EmployeeRepository {
        private final Map<Integer, Employee> table = new LinkedHashMap<>();
        Employee save(Employee employee) { table.put(employee.getId(), employee); return employee; }
        Optional<Employee> findById(int id) { return Optional.ofNullable(table.get(id)); }
    }

    static class DepartmentService {
        private final DepartmentRepository departmentRepository;
        DepartmentService(DepartmentRepository departmentRepository) { this.departmentRepository = departmentRepository; }
        Department get(int id) { return departmentRepository.findById(id).get(); }
    }

    static class EmployeeService {
        private final EmployeeRepository employeeRepository;
        EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }
        Employee get(int id) { return employeeRepository.findById(id).get(); }
        void save(Employee employee) { employeeRepository.save(employee); }
    }

    private static final Logger LOGGER = new Logger("UpdateEmployeeDemo");
    private static DepartmentService departmentService;
    private static EmployeeService employeeService;

    private static void testUpdateEmployee() {
        LOGGER.info("Start");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Before update employee={}", employee);

        Department department = departmentService.get(2);
        employee.setDepartment(department);
        employeeService.save(employee);

        LOGGER.debug("After update employee={}", employee);
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        DepartmentRepository departmentRepository = new DepartmentRepository();
        Department it = departmentRepository.save(new Department(1, "IT"));
        departmentRepository.save(new Department(2, "HR"));
        departmentService = new DepartmentService(departmentRepository);

        EmployeeRepository employeeRepository = new EmployeeRepository();
        employeeRepository.save(new Employee(1, "John Smith", it));
        employeeService = new EmployeeService(employeeRepository);

        testUpdateEmployee();
    }
}
