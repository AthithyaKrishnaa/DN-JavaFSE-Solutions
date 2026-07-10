/*
 * Hands on 3 - Create payroll tables and bean mapping
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PayrollBeanMappingDemo {

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

    static class Skill {
        private int id;
        private String name;

        Skill(int id, String name) { this.id = id; this.name = name; }

        int getId() { return id; }
        String getName() { return name; }

        @Override
        public String toString() {
            return "Skill [id=" + id + ", name=" + name + "]";
        }
    }

    static class Employee {
        private int id;
        private String name;
        private double salary;
        private boolean permanent;
        private LocalDate dateOfBirth;

        Employee(int id, String name, double salary, boolean permanent, LocalDate dateOfBirth) {
            this.id = id;
            this.name = name;
            this.salary = salary;
            this.permanent = permanent;
            this.dateOfBirth = dateOfBirth;
        }

        int getId() { return id; }
        String getName() { return name; }
        double getSalary() { return salary; }
        boolean isPermanent() { return permanent; }
        LocalDate getDateOfBirth() { return dateOfBirth; }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", name=" + name + ", salary=" + salary
                    + ", permanent=" + permanent + ", dateOfBirth=" + dateOfBirth + "]";
        }
    }

    static class DepartmentRepository {
        private final Map<Integer, Department> table = new LinkedHashMap<>();
        Department save(Department department) { table.put(department.getId(), department); return department; }
        List<Department> findAll() { return new ArrayList<>(table.values()); }
        Optional<Department> findById(int id) { return Optional.ofNullable(table.get(id)); }
    }

    static class SkillRepository {
        private final Map<Integer, Skill> table = new LinkedHashMap<>();
        Skill save(Skill skill) { table.put(skill.getId(), skill); return skill; }
        List<Skill> findAll() { return new ArrayList<>(table.values()); }
        Optional<Skill> findById(int id) { return Optional.ofNullable(table.get(id)); }
    }

    static class EmployeeRepository {
        private final Map<Integer, Employee> table = new LinkedHashMap<>();
        Employee save(Employee employee) { table.put(employee.getId(), employee); return employee; }
        List<Employee> findAll() { return new ArrayList<>(table.values()); }
        Optional<Employee> findById(int id) { return Optional.ofNullable(table.get(id)); }
    }

    private static final Logger LOGGER = new Logger("PayrollBeanMappingDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        DepartmentRepository departmentRepository = new DepartmentRepository();
        departmentRepository.save(new Department(1, "IT"));
        departmentRepository.save(new Department(2, "HR"));
        departmentRepository.save(new Department(3, "Finance"));

        SkillRepository skillRepository = new SkillRepository();
        skillRepository.save(new Skill(1, "Java"));
        skillRepository.save(new Skill(2, "SQL"));
        skillRepository.save(new Skill(3, "Communication"));

        EmployeeRepository employeeRepository = new EmployeeRepository();
        employeeRepository.save(new Employee(1, "John Smith", 55000, true, LocalDate.of(1990, 5, 12)));
        employeeRepository.save(new Employee(2, "Emma Davis", 62000, true, LocalDate.of(1988, 11, 23)));
        employeeRepository.save(new Employee(3, "Michael Brown", 48000, false, LocalDate.of(1995, 2, 17)));

        LOGGER.info("Start");
        LOGGER.debug("departments={}", departmentRepository.findAll());
        LOGGER.debug("skills={}", skillRepository.findAll());
        LOGGER.debug("employees={}", employeeRepository.findAll());
        LOGGER.info("End");
    }
}
