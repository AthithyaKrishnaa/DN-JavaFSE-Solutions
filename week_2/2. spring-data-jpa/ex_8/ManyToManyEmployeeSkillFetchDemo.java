/*
 * Hands on 6 - Implement many to many relationship between Employee and Skill: Fetching Employee along with Skills
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ManyToManyEmployeeSkillFetchDemo {

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

    static class Skill {
        private int id;
        private String name;

        Skill(int id, String name) { this.id = id; this.name = name; }

        @Override
        public String toString() {
            return "Skill [id=" + id + ", name=" + name + "]";
        }
    }

    static class Employee {
        private int id;
        private String name;
        private Set<Skill> skillList;
        private boolean skillListLoaded;

        Employee(int id, String name) { this.id = id; this.name = name; }

        Set<Skill> getSkillList() {
            if (!skillListLoaded) {
                throw new LazyInitializationException(
                        "failed to lazily initialize a collection of role: Employee.skillList, could not initialize proxy - no Session");
            }
            return skillList;
        }

        void loadSkillList(Set<Skill> skillList) {
            this.skillList = skillList;
            this.skillListLoaded = true;
        }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", name=" + name + "]";
        }
    }

    static class EmployeeRepository {
        private final Map<Integer, Employee> table = new LinkedHashMap<>();
        private final Map<Integer, List<Skill>> skillsByEmployee = new LinkedHashMap<>();
        private FetchType skillListFetchType = FetchType.LAZY;

        Employee save(Employee employee) { table.put(employee.id, employee); return employee; }

        void addSkill(int employeeId, Skill skill) {
            skillsByEmployee.computeIfAbsent(employeeId, k -> new ArrayList<>()).add(skill);
        }

        void setSkillListFetchType(FetchType fetchType) { this.skillListFetchType = fetchType; }

        Optional<Employee> findById(int id) {
            Employee employee = table.get(id);
            if (employee == null) {
                return Optional.empty();
            }
            if (skillListFetchType == FetchType.EAGER) {
                Set<Skill> skills = new LinkedHashSet<>(skillsByEmployee.getOrDefault(id, new ArrayList<>()));
                employee.loadSkillList(skills);
            }
            return Optional.of(employee);
        }
    }

    static class EmployeeService {
        private final EmployeeRepository employeeRepository;
        EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }
        Employee get(int id) { return employeeRepository.findById(id).get(); }
    }

    private static final Logger LOGGER = new Logger("ManyToManyEmployeeSkillFetchDemo");
    private static EmployeeRepository employeeRepository;
    private static EmployeeService employeeService;

    private static void testGetEmployee() {
        LOGGER.info("Start");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Employee:{}", employee);
        LOGGER.debug("Skills:{}", employee.getSkillList());
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        employeeRepository = new EmployeeRepository();
        employeeRepository.save(new Employee(1, "John Smith"));
        employeeRepository.addSkill(1, new Skill(1, "Java"));
        employeeRepository.addSkill(1, new Skill(2, "SQL"));
        employeeService = new EmployeeService(employeeRepository);

        try {
            testGetEmployee();
        } catch (LazyInitializationException e) {
            LOGGER.error(e.getMessage());
        }

        employeeRepository.setSkillListFetchType(FetchType.EAGER);
        testGetEmployee();
    }
}
