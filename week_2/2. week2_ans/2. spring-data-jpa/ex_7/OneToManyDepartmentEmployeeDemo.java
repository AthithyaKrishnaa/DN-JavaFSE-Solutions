/*
 * Hands on 5 - Implement one to many relationship between Employee and Department
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class OneToManyDepartmentEmployeeDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        void error(String msg) { print("ERROR", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class LazyInitializationException extends RuntimeException {
        LazyInitializationException(String message) { super(message); }
    }

    enum FetchType { LAZY, EAGER }

    static class Department {
        private int id;
        private String name;
        private Set<Employee> employeeList;
        private boolean employeeListLoaded;

        Department(int id, String name) { this.id = id; this.name = name; }

        int getId() { return id; }
        String getName() { return name; }

        Set<Employee> getEmployeeList() {
            if (!employeeListLoaded) {
                throw new LazyInitializationException(
                        "failed to lazily initialize a collection of role: Department.employeeList, could not initialize proxy - no Session");
            }
            return employeeList;
        }

        void loadEmployeeList(Set<Employee> employeeList) {
            this.employeeList = employeeList;
            this.employeeListLoaded = true;
        }

        @Override
        public String toString() {
            return "Department [id=" + id + ", name=" + name + "]";
        }
    }

    static class Employee {
        private int id;
        private String name;

        Employee(int id, String name) { this.id = id; this.name = name; }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", name=" + name + "]";
        }
    }

    static class DepartmentRepository {
        private final Map<Integer, Department> table = new LinkedHashMap<>();
        private final Map<Integer, List<Employee>> employeesByDepartment = new LinkedHashMap<>();
        private FetchType employeeListFetchType = FetchType.LAZY;

        Department save(Department department) { table.put(department.getId(), department); return department; }

        void addEmployee(int departmentId, Employee employee) {
            employeesByDepartment.computeIfAbsent(departmentId, k -> new ArrayList<>()).add(employee);
        }

        void setEmployeeListFetchType(FetchType fetchType) { this.employeeListFetchType = fetchType; }

        Optional<Department> findById(int id) {
            Department department = table.get(id);
            if (department == null) {
                return Optional.empty();
            }
            if (employeeListFetchType == FetchType.EAGER) {
                Set<Employee> employees = new LinkedHashSet<>(
                        employeesByDepartment.getOrDefault(id, new ArrayList<>()));
                department.loadEmployeeList(employees);
            }
            return Optional.of(department);
        }
    }

    static class DepartmentService {
        private final DepartmentRepository departmentRepository;
        DepartmentService(DepartmentRepository departmentRepository) { this.departmentRepository = departmentRepository; }
        Department get(int id) { return departmentRepository.findById(id).get(); }
    }

    private static final Logger LOGGER = new Logger("OneToManyDepartmentEmployeeDemo");
    private static DepartmentRepository departmentRepository;
    private static DepartmentService departmentService;

    private static void testGetDepartment() {
        LOGGER.info("Start");
        Department department = departmentService.get(1);
        LOGGER.debug("department={}", department);
        LOGGER.debug("employeeList={}", department.getEmployeeList());
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        departmentRepository = new DepartmentRepository();
        departmentRepository.save(new Department(1, "IT"));
        departmentRepository.addEmployee(1, new Employee(1, "John Smith"));
        departmentRepository.addEmployee(1, new Employee(2, "Priya Nair"));
        departmentService = new DepartmentService(departmentRepository);

        try {
            testGetDepartment();
        } catch (LazyInitializationException e) {
            LOGGER.error(e.getMessage());
        }

        departmentRepository.setEmployeeListFetchType(FetchType.EAGER);
        testGetDepartment();
    }
}
