// Exercise 2 - Employee Management System - Creating Entities
import java.util.ArrayList;
import java.util.List;

public class EntityMappingDemo {

    static class Department {
        Long id;
        String name;
        List<Employee> employees = new ArrayList<>();

        Department(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        void addEmployee(Employee employee) {
            employee.department = this;
            employees.add(employee);
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
        Department department;

        Employee(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', email='" + email + "', department="
                    + (department == null ? "null" : department.name) + "}";
        }
    }

    public static void main(String[] args) {
        System.out.println("Mapping: @Entity @Table(name=\"department\") class Department { @Id @GeneratedValue Long id; String name; @OneToMany(mappedBy=\"department\") List<Employee> employees; }");
        System.out.println("Mapping: @Entity @Table(name=\"employee\") class Employee { @Id @GeneratedValue Long id; String name; String email; @ManyToOne @JoinColumn(name=\"department_id\") Department department; }");

        Department engineering = new Department(1L, "Engineering");
        Employee alice = new Employee(1L, "Alice", "alice@company.com");
        Employee bob = new Employee(2L, "Bob", "bob@company.com");
        engineering.addEmployee(alice);
        engineering.addEmployee(bob);

        System.out.println("\n" + engineering);
        System.out.println("Employees in " + engineering.name + ":");
        for (Employee employee : engineering.employees) {
            System.out.println("   " + employee);
        }
    }
}
