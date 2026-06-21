/**
 * Demonstrates add, update, delete, and lookup operations on the inventory.
 */
public class Main {

    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // Add products
        manager.addProduct(new Product("P001", "Wireless Mouse", 50, 19.99));
        manager.addProduct(new Product("P002", "Mechanical Keyboard", 30, 79.99));
        manager.addProduct(new Product("P003", "USB-C Hub", 100, 24.50));

        System.out.println("Initial inventory:");
        manager.getAllProducts().forEach(System.out::println);

        // Update a product
        manager.updateProduct("P002", 25, 74.99);
        System.out.println("\nAfter updating P002:");
        System.out.println(manager.getProduct("P002"));

        // Delete a product
        manager.deleteProduct("P001");
        System.out.println("\nAfter deleting P001, remaining inventory:");
        manager.getAllProducts().forEach(System.out::println);

        System.out.println("\nTotal products in inventory: " + manager.size());
    }
}
