// Exercise 8 - Employee Management System - Creating Projections
import java.util.ArrayList;
import java.util.List;

public class ProjectionsDemo {

    static class Employee {
        Long id;
        String name;
        String email;
        double salary;

        Employee(Long id, String name, String email, double salary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.salary = salary;
        }
    }

    interface EmployeeNameOnly {
        String getName();
    }

    static class EmployeeNameProjection implements EmployeeNameOnly {
        private final Employee employee;

        EmployeeNameProjection(Employee employee) {
            this.employee = employee;
        }

        public String getName() {
            return employee.name;
        }

        @Override
        public String toString() {
            return "EmployeeNameOnly{name='" + getName() + "'}";
        }
    }

    static class EmployeeSummary {
        private final String name;
        private final String email;

        EmployeeSummary(String name, String email) {
            this.name = name;
            this.email = email;
        }

        @Override
        public String toString() {
            return "EmployeeSummary{name='" + name + "', email='" + email + "'}";
        }
    }

    static class EmployeeRepository {
        private final List<Employee> table = new ArrayList<>();

        void save(Employee employee) {
            table.add(employee);
        }

        List<EmployeeNameOnly> findNameProjectionBySalaryGreaterThan(double threshold) {
            List<EmployeeNameOnly> result = new ArrayList<>();
            for (Employee employee : table) {
                if (employee.salary > threshold) result.add(new EmployeeNameProjection(employee));
            }
            return result;
        }

        List<EmployeeSummary> findSummaryByEmailContaining(String fragment) {
            List<EmployeeSummary> result = new ArrayList<>();
            for (Employee employee : table) {
                if (employee.email.contains(fragment)) result.add(new EmployeeSummary(employee.name, employee.email));
            }
            return result;
        }
    }

    public static void main(String[] args) {
        EmployeeRepository repository = new EmployeeRepository();
        repository.save(new Employee(1L, "Alice", "alice@company.com", 95000));
        repository.save(new Employee(2L, "Bob", "bob@company.com", 65000));
        repository.save(new Employee(3L, "Carol", "carol@company.com", 72000));

        System.out.println("Interface-based projection, salary > 70000:");
        for (EmployeeNameOnly projection : repository.findNameProjectionBySalaryGreaterThan(70000)) {
            System.out.println("   " + projection);
        }

        System.out.println("Class-based projection, email containing \"company\":");
        for (EmployeeSummary summary : repository.findSummaryByEmailContaining("company")) {
            System.out.println("   " + summary);
        }
    }
}
