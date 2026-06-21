/**
 * Demonstrates add, search, traverse, and delete operations on an array-backed
 * employee store.
 */
public class Main {

    public static void main(String[] args) {
        EmployeeArrayManager manager = new EmployeeArrayManager(2); // small capacity to show resizing

        manager.add(new Employee("E001", "Anita Rao", "Software Engineer", 75000));
        manager.add(new Employee("E002", "Vikram Singh", "Product Manager", 95000));
        manager.add(new Employee("E003", "Priya Nair", "Data Analyst", 68000)); // triggers a resize
        manager.add(new Employee("E004", "Suresh Kumar", "QA Engineer", 62000));

        System.out.println("All employees after adding (note: capacity grew automatically):");
        manager.traverse();

        System.out.println("\nSearching for E003:");
        System.out.println(manager.search("E003"));

        System.out.println("\nSearching for a non-existent id E999:");
        System.out.println(manager.search("E999"));

        manager.delete("E002");
        System.out.println("\nAfter deleting E002, remaining employees:");
        manager.traverse();

        System.out.println("\nTotal employees: " + manager.size());
    }
}
