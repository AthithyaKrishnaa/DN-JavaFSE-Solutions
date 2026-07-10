// Exercise 5 - Employee Management System - Defining Query Methods
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryMethodsDemo {

    static class Employee {
        Long id;
        String name;
        String email;
        String departmentName;
        double salary;

        Employee(Long id, String name, String email, String departmentName, double salary) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.departmentName = departmentName;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', department='" + departmentName + "', salary=" + salary + "}";
        }
    }

    static class EmployeeRepository {
        private final List<Employee> table = new ArrayList<>();
        private final Map<String, String> namedQueries = new HashMap<>();

        EmployeeRepository() {
            namedQueries.put("Employee.findHighEarners", "SELECT e FROM Employee e WHERE e.salary > :threshold");
            namedQueries.put("Employee.findByDepartmentName", "SELECT e FROM Employee e WHERE e.departmentName = :departmentName");
        }

        Employee save(Employee employee) {
            table.add(employee);
            return employee;
        }

        List<Employee> findByDepartmentName(String departmentName) {
            List<Employee> result = new ArrayList<>();
            for (Employee employee : table) {
                if (employee.departmentName.equals(departmentName)) result.add(employee);
            }
            return result;
        }

        List<Employee> findByNameContaining(String fragment) {
            List<Employee> result = new ArrayList<>();
            for (Employee employee : table) {
                if (employee.name.contains(fragment)) result.add(employee);
            }
            return result;
        }

        List<Employee> findBySalaryGreaterThan(double threshold) {
            List<Employee> result = new ArrayList<>();
            for (Employee employee : table) {
                if (employee.salary > threshold) result.add(employee);
            }
            return result;
        }

        List<Employee> customQuery(String query, double threshold) {
            System.out.println("Executing @Query(\"" + query + "\") with threshold=" + threshold);
            return findBySalaryGreaterThan(threshold);
        }

        List<Employee> runNamedQuery(String name, double threshold) {
            String jpql = namedQueries.get(name);
            System.out.println("Executing @NamedQuery " + name + " -> " + jpql);
            return findBySalaryGreaterThan(threshold);
        }
    }

    public static void main(String[] args) {
        EmployeeRepository repository = new EmployeeRepository();
        repository.save(new Employee(1L, "Alice", "alice@company.com", "Engineering", 95000));
        repository.save(new Employee(2L, "Bob", "bob@company.com", "Engineering", 65000));
        repository.save(new Employee(3L, "Carol", "carol@company.com", "Sales", 72000));

        System.out.println("findByDepartmentName(\"Engineering\") -> " + repository.findByDepartmentName("Engineering"));
        System.out.println("findByNameContaining(\"ar\") -> " + repository.findByNameContaining("ar"));
        System.out.println("customQuery(...) -> " + repository.customQuery("SELECT e FROM Employee e WHERE e.salary > :threshold", 70000));
        System.out.println("runNamedQuery(...) -> " + repository.runNamedQuery("Employee.findHighEarners", 80000));
    }
}
