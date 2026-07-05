/*
 * Hands on 2 - Optimizing HQL solution by using 'fetch'
 */
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PermanentEmployeesJoinFetchDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
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
        @Override
        public String toString() { return "Department [id=" + id + ", name=" + name + "]"; }
    }

    static class Employee {
        private int id;
        private String name;
        private boolean permanent;
        private Department department;
        private Set<Skill> skillList = new LinkedHashSet<>();
        Employee(int id, String name, boolean permanent, Department department) {
            this.id = id;
            this.name = name;
            this.permanent = permanent;
            this.department = department;
        }
        boolean isPermanent() { return permanent; }
        Department getDepartment() { return department; }
        Set<Skill> getSkillList() { return skillList; }
        @Override
        public String toString() { return "Employee [id=" + id + ", name=" + name + "]"; }
    }

    static class EmployeeRepository {
        private final List<Employee> table = new ArrayList<>();
        private int queryCount;

        void seed(List<Employee> employees) { table.addAll(employees); }

        List<Employee> getAllPermanentEmployees(Logger queryLog) {
            queryCount++;
            queryLog.debug("Query {}: SELECT e FROM Employee e left join fetch e.department d "
                    + "left join fetch e.skillList WHERE e.permanent = 1", queryCount);
            List<Employee> permanentEmployees = new ArrayList<>();
            for (Employee employee : table) {
                if (employee.isPermanent()) {
                    permanentEmployees.add(employee);
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

    private static final Logger LOGGER = new Logger("PermanentEmployeesJoinFetchDemo");
    private static EmployeeService employeeService;
    private static EmployeeRepository employeeRepository;

    private static void testGetAllPermanentEmployees() {
        LOGGER.info("Start");
        List<Employee> employees = employeeService.getAllPermanentEmployees(LOGGER);
        LOGGER.debug("Permanent Employees:{}", employees);
        employees.forEach(e -> LOGGER.debug("Department:{}", e.getDepartment()));
        employees.forEach(e -> LOGGER.debug("Skills:{}", e.getSkillList()));
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

        john.getSkillList().add(new Skill(1, "Java"));
        priya.getSkillList().add(new Skill(2, "SQL"));

        employeeRepository = new EmployeeRepository();
        employeeRepository.seed(List.of(john, priya, michael));
        employeeService = new EmployeeService(employeeRepository);

        testGetAllPermanentEmployees();
    }
}
