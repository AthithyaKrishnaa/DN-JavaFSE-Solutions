/*
 * Hands on 6 - Implement many to many relationship between Employee and Skill: Add Skill to Employee
 */
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AddSkillToEmployeeDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class Skill {
        private int id;
        private String name;

        Skill(int id, String name) { this.id = id; this.name = name; }

        int getId() { return id; }

        @Override
        public String toString() {
            return "Skill [id=" + id + ", name=" + name + "]";
        }
    }

    static class Employee {
        private int id;
        private String name;
        private Set<Skill> skillList = new LinkedHashSet<>();

        Employee(int id, String name) { this.id = id; this.name = name; }

        Set<Skill> getSkillList() { return skillList; }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", name=" + name + "]";
        }
    }

    static class EmployeeRepository {
        private final Map<Integer, Employee> table = new LinkedHashMap<>();
        Employee save(Employee employee) { table.put(employee.id, employee); return employee; }
        Optional<Employee> findById(int id) { return Optional.ofNullable(table.get(id)); }
    }

    static class SkillRepository {
        private final Map<Integer, Skill> table = new LinkedHashMap<>();
        Skill save(Skill skill) { table.put(skill.getId(), skill); return skill; }
        Optional<Skill> findById(int id) { return Optional.ofNullable(table.get(id)); }
    }

    static class EmployeeService {
        private final EmployeeRepository employeeRepository;
        EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }
        Employee get(int id) { return employeeRepository.findById(id).get(); }
        void save(Employee employee) { employeeRepository.save(employee); }
    }

    static class SkillService {
        private final SkillRepository skillRepository;
        SkillService(SkillRepository skillRepository) { this.skillRepository = skillRepository; }
        Skill get(int id) { return skillRepository.findById(id).get(); }
    }

    private static final Logger LOGGER = new Logger("AddSkillToEmployeeDemo");
    private static EmployeeService employeeService;
    private static SkillService skillService;

    private static void testAddSkillToEmployee() {
        LOGGER.info("Start");
        Employee employee = employeeService.get(3);
        LOGGER.debug("Before add skillList={}", employee.getSkillList());

        Skill skill = skillService.get(3);
        employee.getSkillList().add(skill);
        employeeService.save(employee);

        LOGGER.debug("After add skillList={}", employee.getSkillList());
        LOGGER.info("End");
    }

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        SkillRepository skillRepository = new SkillRepository();
        skillRepository.save(new Skill(1, "Java"));
        skillRepository.save(new Skill(2, "SQL"));
        skillRepository.save(new Skill(3, "Communication"));
        skillService = new SkillService(skillRepository);

        EmployeeRepository employeeRepository = new EmployeeRepository();
        employeeRepository.save(new Employee(3, "Michael Brown"));
        employeeService = new EmployeeService(employeeRepository);

        testAddSkillToEmployee();
    }
}
