/*
 * Hands on 4 - Implement many to one relationship between Employee and Department: Add Employee
 */
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AddEmployeeDemo {

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
        private double salary;
        private boolean permanent;
        private LocalDate dateOfBirth;
        private Department department;

        int getId() { return id; }
        void setId(int id) { this.id = id; }
        void setName(String name) { this.name = name; }
        void setSalary(double salary) { this.salary = salary; }
        void setPermanent(boolean permanent) { this.permanent = permanent; }
        void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        Department getDepartment() { return department; }
        void setDepartment(Department department) { this.department = department; }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", name=" + name + ", salary=" + salary
                    + ", permanent=" + permanent + ", dateOfBirth=" + dateOfBirth
                    + ", department=" + department + "]";
        }
    }

    static class DepartmentRepository {
        private final Map<Integer, Department> table = new LinkedHashMap<>();
        Department save(Department department) { table.put(department.getId(), department); return department; }
        Optional<Department> findById(int id) { return Optional.ofNullable(table.get(id)); }
    }

    static class EmployeeRepository {
        private final Map<Integer, Employee> table = new LinkedHashMap<>();
        private int nextId = 1;
        Employee save(Employee employee) {
            if (employee.getId() == 0) {
                employee.setId(nextId++);
            }
            table.put(employee.getId(), employee);
            return employee;
        }
    }

    static class DepartmentService {
        private final DepartmentRepository departmentRepository;
        DepartmentService(DepartmentRepository departmentRepository) { this.departmentRepository = departmentRepository; }
        Department get(int id) { return departmentRepository.findById(id).get(); }
    }

    static class EmployeeService {
        private final EmployeeRepository employeeRepository;
        EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }
        void save(Employee employee) { employeeRepository.save(employee); }
    }

    private static final Logger LOGGER = new Logger("AddEmployeeDemo");
    private static DepartmentService departmentService;
    private static EmployeeService employeeService;

    private static void testAddEmployee() {
        LOGGER.info("Start");
        Employee employee = new Employee();
        employee.setName("Priya Nair");
        employee.setSalary(51000);
        employee.setPermanent(true);
        employee.setDateOfBirth(LocalDate.of(1993, 8, 4));

        Department department = departmentService.get(1);
        employee.setDepartment(department);

        employeeService.save(employee);
        LOGGER.debug("employee={}", employee);
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        DepartmentRepository departmentRepository = new DepartmentRepository();
        departmentRepository.save(new Department(1, "IT"));
        departmentRepository.save(new Department(2, "HR"));
        departmentService = new DepartmentService(departmentRepository);

        EmployeeRepository employeeRepository = new EmployeeRepository();
        employeeService = new EmployeeService(employeeRepository);

        testAddEmployee();
    }
}
