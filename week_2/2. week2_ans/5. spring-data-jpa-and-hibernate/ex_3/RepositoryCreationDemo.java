// Exercise 3 - Employee Management System - Creating Repositories
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositoryCreationDemo {

    static class Department {
        Long id;
        String name;

        Department(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Department{id=" + id + ", name='" + name + "'}";
        }
    }

    static class Employee {
        Long id;
        String name;
        String email;
        String departmentName;

        Employee(Long id, String name, String email, String departmentName) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.departmentName = departmentName;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', email='" + email + "', department='" + departmentName + "'}";
        }
    }

    interface JpaRepository<T, ID> {
        T save(T entity);
        Optional<T> findById(ID id);
        List<T> findAll();
        void deleteById(ID id);
    }

    static class DepartmentRepository implements JpaRepository<Department, Long> {
        private final Map<Long, Department> table = new LinkedHashMap<>();

        public Department save(Department department) {
            table.put(department.id, department);
            return department;
        }

        public Optional<Department> findById(Long id) {
            return Optional.ofNullable(table.get(id));
        }

        public List<Department> findAll() {
            return new ArrayList<>(table.values());
        }

        public void deleteById(Long id) {
            table.remove(id);
        }

        Optional<Department> findByName(String name) {
            return table.values().stream().filter(d -> d.name.equals(name)).findFirst();
        }
    }

    static class EmployeeRepository implements JpaRepository<Employee, Long> {
        private final Map<Long, Employee> table = new LinkedHashMap<>();

        public Employee save(Employee employee) {
            table.put(employee.id, employee);
            return employee;
        }

        public Optional<Employee> findById(Long id) {
            return Optional.ofNullable(table.get(id));
        }

        public List<Employee> findAll() {
            return new ArrayList<>(table.values());
        }

        public void deleteById(Long id) {
            table.remove(id);
        }

        List<Employee> findByDepartmentName(String departmentName) {
            List<Employee> result = new ArrayList<>();
            for (Employee employee : table.values()) {
                if (employee.departmentName.equals(departmentName)) {
                    result.add(employee);
                }
            }
            return result;
        }

        Optional<Employee> findByEmail(String email) {
            return table.values().stream().filter(e -> e.email.equals(email)).findFirst();
        }
    }

    public static void main(String[] args) {
        DepartmentRepository departmentRepository = new DepartmentRepository();
        EmployeeRepository employeeRepository = new EmployeeRepository();

        departmentRepository.save(new Department(1L, "Engineering"));
        employeeRepository.save(new Employee(1L, "Alice", "alice@company.com", "Engineering"));
        employeeRepository.save(new Employee(2L, "Bob", "bob@company.com", "Engineering"));

        System.out.println("findByName(\"Engineering\") -> " + departmentRepository.findByName("Engineering").orElse(null));
        System.out.println("findByDepartmentName(\"Engineering\") -> " + employeeRepository.findByDepartmentName("Engineering"));
        System.out.println("findByEmail(\"bob@company.com\") -> " + employeeRepository.findByEmail("bob@company.com").orElse(null));
        System.out.println("findAll() employees -> " + employeeRepository.findAll());
    }
}
