/*
 * Hands on 1 - Spring REST - Create static employee list data using spring xml configuration
 */
import java.util.ArrayList;
import java.util.List;

public class EmployeeXmlConfigDaoDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class Employee {
        String empId;
        String empName;
        String designation;
        double salary;
        String departmentName;

        Employee(String empId, String empName, String designation, double salary, String departmentName) {
            this.empId = empId;
            this.empName = empName;
            this.designation = designation;
            this.salary = salary;
            this.departmentName = departmentName;
        }

        @Override
        public String toString() {
            return "Employee{empId=" + empId + ", empName=" + empName + ", designation=" + designation
                    + ", salary=" + salary + ", departmentName=" + departmentName + "}";
        }
    }

    static class Department {
        String deptId;
        String deptName;

        Department(String deptId, String deptName) {
            this.deptId = deptId;
            this.deptName = deptName;
        }

        @Override
        public String toString() {
            return "Department{deptId=" + deptId + ", deptName=" + deptName + "}";
        }
    }

    static final class EmployeeXmlConfig {
        static List<Department> loadDepartments() {
            List<Department> departments = new ArrayList<>();
            departments.add(new Department("D001", "Engineering"));
            departments.add(new Department("D002", "Human Resources"));
            departments.add(new Department("D003", "Finance"));
            return departments;
        }

        static List<Employee> loadEmployees() {
            List<Employee> employees = new ArrayList<>();
            employees.add(new Employee("E001", "Arun Kumar", "Software Engineer", 45000.0, "Engineering"));
            employees.add(new Employee("E002", "Divya Sundaram", "Senior Software Engineer", 65000.0, "Engineering"));
            employees.add(new Employee("E003", "Karthik Raja", "HR Executive", 38000.0, "Human Resources"));
            employees.add(new Employee("E004", "Priya Natarajan", "HR Manager", 72000.0, "Human Resources"));
            employees.add(new Employee("E005", "Suresh Babu", "Financial Analyst", 50000.0, "Finance"));
            employees.add(new Employee("E006", "Meena Ramesh", "Finance Manager", 78000.0, "Finance"));
            return employees;
        }
    }

    static class EmployeeDao {
        static final List<Employee> EMPLOYEE_LIST;

        static {
            EMPLOYEE_LIST = EmployeeXmlConfig.loadEmployees();
        }

        List<Employee> getAllEmployees() {
            return EMPLOYEE_LIST;
        }
    }

    private static final Logger LOGGER = new Logger("EmployeeXmlConfigDaoDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        List<Department> departments = EmployeeXmlConfig.loadDepartments();
        LOGGER.debug("departments loaded from xml config, count={}", departments.size());
        for (Department department : departments) {
            LOGGER.info(department.toString());
        }

        EmployeeDao employeeDao = new EmployeeDao();
        List<Employee> employees = employeeDao.getAllEmployees();
        LOGGER.debug("EMPLOYEE_LIST size={}", employees.size());
        for (Employee employee : employees) {
            LOGGER.info(employee.toString());
        }

        LOGGER.info("End");
    }
}
