/*
 * Hands on 6 - Spring REST - Employee, Department and Skill bean validations
 */
import java.util.ArrayList;
import java.util.List;

public class EmployeeValidationBeansDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class Skill {
        // @NotNull, should be a number
        Integer id;
        // @NotNull @NotBlank @Size(min=1, max=30)
        String name;

        Skill(Integer id, String name) { this.id = id; this.name = name; }

        @Override
        public String toString() { return "Skill{id=" + id + ", name=" + name + "}"; }
    }

    static class Department {
        // @NotNull, should be a number
        Integer id;
        // @NotNull @NotBlank @Size(min=1, max=30)
        String name;

        Department(Integer id, String name) { this.id = id; this.name = name; }

        @Override
        public String toString() { return "Department{id=" + id + ", name=" + name + "}"; }
    }

    static class Employee {
        // @NotNull, should be a number
        Integer id;
        // @NotNull @NotBlank @Size(min=1, max=30)
        String name;
        // @NotNull, should be zero or above
        Double salary;
        // @NotNull
        Boolean permanent;
        // @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
        String dateOfBirth;

        Employee(Integer id, String name, Double salary, Boolean permanent, String dateOfBirth) {
            this.id = id;
            this.name = name;
            this.salary = salary;
            this.permanent = permanent;
            this.dateOfBirth = dateOfBirth;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name=" + name + ", salary=" + salary
                    + ", permanent=" + permanent + ", dateOfBirth=" + dateOfBirth + "}";
        }
    }

    static final class BeanValidator {
        List<String> validateSkill(Skill skill) {
            List<String> errors = new ArrayList<>();
            if (skill.id == null) errors.add("Skill id should not be null");
            if (skill.name == null || skill.name.trim().isEmpty()) errors.add("Skill name should not be blank");
            else if (skill.name.length() > 30) errors.add("Skill name should be maximum 30 characters");
            return errors;
        }

        List<String> validateDepartment(Department department) {
            List<String> errors = new ArrayList<>();
            if (department.id == null) errors.add("Department id should not be null");
            if (department.name == null || department.name.trim().isEmpty()) errors.add("Department name should not be blank");
            else if (department.name.length() > 30) errors.add("Department name should be maximum 30 characters");
            return errors;
        }

        List<String> validateEmployee(Employee employee) {
            List<String> errors = new ArrayList<>();
            if (employee.id == null) errors.add("Employee id should not be null");
            if (employee.name == null || employee.name.trim().isEmpty()) errors.add("Employee name should not be blank");
            else if (employee.name.length() > 30) errors.add("Employee name should be maximum 30 characters");
            if (employee.salary == null) errors.add("Employee salary should not be null");
            else if (employee.salary < 0) errors.add("Employee salary should be zero or above");
            if (employee.permanent == null) errors.add("Employee permanent should not be null");
            if (employee.dateOfBirth == null || !employee.dateOfBirth.matches("\\d{2}/\\d{2}/\\d{4}")) {
                errors.add("Employee dateOfBirth should match pattern dd/MM/yyyy");
            }
            return errors;
        }
    }

    private static final Logger LOGGER = new Logger("EmployeeValidationBeansDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        BeanValidator validator = new BeanValidator();

        Skill validSkill = new Skill(1, "Java");
        Skill invalidSkill = new Skill(null, "");
        LOGGER.info("validateSkill(valid)=" + validator.validateSkill(validSkill));
        LOGGER.info("validateSkill(invalid)=" + validator.validateSkill(invalidSkill));

        Department validDepartment = new Department(1, "Engineering");
        Department invalidDepartment = new Department(null, "");
        LOGGER.info("validateDepartment(valid)=" + validator.validateDepartment(validDepartment));
        LOGGER.info("validateDepartment(invalid)=" + validator.validateDepartment(invalidDepartment));

        Employee validEmployee = new Employee(1, "Arun Kumar", 45000.0, true, "15/08/1995");
        Employee invalidEmployee = new Employee(null, "", -100.0, null, "1995-08-15");
        LOGGER.info("validateEmployee(valid)=" + validator.validateEmployee(validEmployee));
        LOGGER.info("validateEmployee(invalid)=" + validator.validateEmployee(invalidEmployee));

        LOGGER.info("End");
    }
}
