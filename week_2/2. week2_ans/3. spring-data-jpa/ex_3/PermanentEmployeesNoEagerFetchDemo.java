/*
 * Hands on 2 - Optimizing HQL Solution by removing the EAGER fetch configuration
 */
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PermanentEmployeesNoEagerFetchDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        void error(String msg) { print("ERROR", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class LazyInitializationException extends RuntimeException {
        LazyInitializationException(String message) { super(message); }
    }

    static class Skill {
        private int id;
        private String name;
        Skill(int id, String name) { this.id = id; this.name = name; }
        @Override
        public String toString() { return "Skill [id=" + id + ", name=" + name + "]"; }
    }

    static class Department {
        private int id;
        private String name;
        Department(int id, String name) { this.id = id; this.name = name; }
        int getId() { return id; }
        @Override
        public String toString() { return "Department [id=" + id + ", name=" + name + "]"; }
    }

    static class Employee {
        private int id;
        private String name;
        private boolean permanent;
        private Department department;
        private Set<Skill> skillList;
        Employee(int id, String name, boolean permanent, Department department) {
            this.id = id;
            this.name = name;
            this.permanent = permanent;
            this.department = department;
        }
        boolean isPermanent() { return permanent; }
        Department getDepartment() { return department; }
        Set<Skill> getSkillList() {
            if (skillList == null) {
                throw new LazyInitializationException(
                        "failed to lazily initialize a collection of role: Employee.skillList, could not initialize proxy - no Session");
            }
            return skillList;
        }
        @Override
        public String toString() { return "Employee [id=" + id + ", name=" + name + "]"; }
    }

    static class EmployeeRepository {
        private final List<Employee> table = new ArrayList<>();
        private int queryCount;

        void seed(List<Employee> employees) { table.addAll(employees); }

        List<Employee> getAllPermanentEmployees(Logger queryLog) {
            queryCount++;
            queryLog.debug("Query {}: SELECT e FROM Employee e WHERE e.permanent = 1", queryCount);
            List<Employee> permanentEmployees = new ArrayList<>();
            for (Employee employee : table) {
                if (employee.isPermanent()) {
                    permanentEmployees.add(employee);
                }
            }

            Set<Integer> departmentsLoaded = new LinkedHashSet<>();
            for (Employee employee : permanentEmployees) {
                Department department = employee.getDepartment();
                if (departmentsLoaded.add(department.getId())) {
                    queryCount++;
                    queryLog.debug("Query {}: SELECT * FROM department WHERE dp_id = " + department.getId(), queryCount);
                }
            }
            return permanentEmployees;
        }

        int getQueryCount() { return queryCount; }
    }

    static class EmployeeService {
        private final EmployeeRepository employeeRepository;
        EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }
        List<Employee> getAllPermanentEmployees(Logger queryLog) { return employeeRepository.getAllPermanentEmployees(queryLog); }
    }

    private static final Logger LOGGER = new Logger("PermanentEmployeesNoEagerFetchDemo");
    private static EmployeeService employeeService;
    private static EmployeeRepository employeeRepository;

    private static void testGetAllPermanentEmployees() {
        LOGGER.info("Start");
        List<Employee> employees = employeeService.getAllPermanentEmployees(LOGGER);
        LOGGER.debug("Permanent Employees:{}", employees);
        for (Employee employee : employees) {
            try {
                LOGGER.debug("Skills:{}", employee.getSkillList());
            } catch (LazyInitializationException e) {
                LOGGER.error(e.getMessage());
            }
        }
        LOGGER.debug("Total queries executed:{}", employeeRepository.getQueryCount());
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        Department it = new Department(2, "IT");
        Department hr = new Department(3, "HR");

        Employee john = new Employee(1, "John Smith", true, it);
        Employee priya = new Employee(2, "Priya Nair", true, hr);
        Employee michael = new Employee(3, "Michael Brown", false, it);

        employeeRepository = new EmployeeRepository();
        employeeRepository.seed(List.of(john, priya, michael));
        employeeService = new EmployeeService(employeeRepository);

        testGetAllPermanentEmployees();
    }
}
