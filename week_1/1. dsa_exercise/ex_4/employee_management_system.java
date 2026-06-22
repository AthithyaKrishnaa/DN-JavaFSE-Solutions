class Employee {
    int employeeId;
    String name;
    String position;
    double salary;

    Employee(int employeeId, String name, String position, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    public String toString() {
        return "ID: " + employeeId +
                ", Name: " + name +
                ", Position: " + position +
                ", Salary: " + salary;
    }
}

public class Main {

    static Employee[] employees = new Employee[5];
    static int count = 0;

    // ADD
    public static void addEmployee(Employee e) {
        if (count < employees.length) {
            employees[count] = e;
            count++;
            System.out.println("Employee Added: " + e.employeeId);
        } else {
            System.out.println("Array Full! Cannot add employee.");
        }
    }

    // SEARCH
    public static void searchEmployee(int id) {
        System.out.println("\nSearching Employee ID: " + id);

        for (int i = 0; i < count; i++) {
            if (employees[i].employeeId == id) {
                System.out.println("Employee Found: " + employees[i]);
                return;
            }
        }
        System.out.println("Employee Not Found");
    }

    // TRAVERSE
    public static void displayEmployees() {
        System.out.println("\n=== Employee List ===");
        for (int i = 0; i < count; i++) {
            System.out.println(employees[i]);
        }
    }

    // DELETE
    public static void deleteEmployee(int id) {
        System.out.println("\nDeleting Employee ID: " + id);

        int index = -1;

        for (int i = 0; i < count; i++) {
            if (employees[i].employeeId == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Employee Not Found");
            return;
        }

        for (int i = index; i < count - 1; i++) {
            employees[i] = employees[i + 1];
        }

        count--;
        System.out.println("Employee Deleted Successfully");
    }

    public static void main(String[] args) {

        // ADD OPERATIONS
        System.out.println("=== ADD EMPLOYEES ===");

        addEmployee(new Employee(101, "Arun", "Developer", 50000));
        addEmployee(new Employee(102, "Bala", "Tester", 40000));
        addEmployee(new Employee(103, "Charan", "Manager", 80000));

        // DISPLAY
        displayEmployees();

        // SEARCH
        searchEmployee(102);

        // DELETE
        deleteEmployee(102);

        // DISPLAY AFTER DELETE
        displayEmployees();

        // ANALYSIS
        System.out.println("\n=== TIME COMPLEXITY ANALYSIS ===");

        System.out.println("Add Employee      : O(1) (if space available)");
        System.out.println("Search Employee   : O(n)");
        System.out.println("Traverse Employee : O(n)");
        System.out.println("Delete Employee   : O(n)");

        System.out.println("\n=== ARRAYS LIMITATIONS ===");
        System.out.println("- Fixed size (cannot grow dynamically)");
        System.out.println("- Slow deletion (shifting required)");
        System.out.println("- Slow search for large datasets");

        System.out.println("\n=== WHEN TO USE ARRAYS ===");
        System.out.println("- Small fixed-size data");
        System.out.println("- When memory layout matters");
        System.out.println("- When performance is predictable and simple");
    }
}