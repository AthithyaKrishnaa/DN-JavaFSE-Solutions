// Exercise 4 - Employee Management System - Implementing CRUD Operations
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CrudOperationsDemo {

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

        Employee(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', email='" + email + "'}";
        }
    }

    static class EmployeeRepository {
        private final Map<Long, Employee> table = new LinkedHashMap<>();
        private long nextId = 1;

        Employee save(Employee employee) {
            if (employee.id == null) employee.id = nextId++;
            table.put(employee.id, employee);
            return employee;
        }

        Optional<Employee> findById(Long id) {
            return Optional.ofNullable(table.get(id));
        }

        List<Employee> findAll() {
            return new ArrayList<>(table.values());
        }

        void deleteById(Long id) {
            table.remove(id);
        }
    }

    static class DepartmentRepository {
        private final Map<Long, Department> table = new LinkedHashMap<>();
        private long nextId = 1;

        Department save(Department department) {
            if (department.id == null) department.id = nextId++;
            table.put(department.id, department);
            return department;
        }

        List<Department> findAll() {
            return new ArrayList<>(table.values());
        }
    }

    static class EmployeeController {
        private final EmployeeRepository employeeRepository;

        EmployeeController(EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
        }

        Employee create(String name, String email) {
            System.out.println("POST /api/employees");
            return employeeRepository.save(new Employee(null, name, email));
        }

        List<Employee> readAll() {
            System.out.println("GET /api/employees");
            return employeeRepository.findAll();
        }

        Employee update(Long id, String name, String email) {
            System.out.println("PUT /api/employees/" + id);
            Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
            employee.name = name;
            employee.email = email;
            return employeeRepository.save(employee);
        }

        void delete(Long id) {
            System.out.println("DELETE /api/employees/" + id);
            employeeRepository.deleteById(id);
        }
    }

    static class DepartmentController {
        private final DepartmentRepository departmentRepository;

        DepartmentController(DepartmentRepository departmentRepository) {
            this.departmentRepository = departmentRepository;
        }

        Department create(String name) {
            System.out.println("POST /api/departments");
            return departmentRepository.save(new Department(null, name));
        }

        List<Department> readAll() {
            System.out.println("GET /api/departments");
            return departmentRepository.findAll();
        }
    }

    public static void main(String[] args) {
        EmployeeController employeeController = new EmployeeController(new EmployeeRepository());
        DepartmentController departmentController = new DepartmentController(new DepartmentRepository());

        departmentController.create("Engineering");
        System.out.println(departmentController.readAll());

        employeeController.create("Alice", "alice@company.com");
        employeeController.create("Bob", "bob@company.com");
        System.out.println(employeeController.readAll());

        employeeController.update(1L, "Alice Smith", "alice.smith@company.com");
        System.out.println(employeeController.readAll());

        employeeController.delete(2L);
        System.out.println(employeeController.readAll());
    }
}
