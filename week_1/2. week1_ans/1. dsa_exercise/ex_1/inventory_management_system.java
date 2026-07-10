import java.util.HashMap;

class Product {
    int productId;
    String productName;
    int quantity;
    double price;

    Product(int productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String toString() {
        return "ID: " + productId +
                ", Name: " + productName +
                ", Quantity: " + quantity +
                ", Price: " + price;
    }
}

public class Main {
    public static void main(String[] args) {

        HashMap<Integer, Product> inventory = new HashMap<>();

        // ADD PRODUCTS
        System.out.println("=== ADD PRODUCTS ===");

        inventory.put(101, new Product(101, "Laptop", 10, 50000));
        System.out.println("Product 101 Added");

        inventory.put(102, new Product(102, "Mouse", 50, 500));
        System.out.println("Product 102 Added");

        System.out.println("\nInventory After Adding:");
        for (Product p : inventory.values()) {
            System.out.println(p);
        }

        // UPDATE PRODUCT
        System.out.println("\n=== UPDATE PRODUCT ===");

        Product p = inventory.get(101);

        if (p != null) {
            p.quantity = 15;
            p.price = 55000;
            System.out.println("Product 101 Updated");
        }

        System.out.println("\nInventory After Updating:");
        for (Product product : inventory.values()) {
            System.out.println(product);
        }

        // DELETE PRODUCT
        System.out.println("\n=== DELETE PRODUCT ===");

        inventory.remove(102);
        System.out.println("Product 102 Deleted");

        System.out.println("\nInventory After Deletion:");
        for (Product product : inventory.values()) {
            System.out.println(product);
        }

        // ANALYSIS
        System.out.println("\n=== TIME COMPLEXITY ANALYSIS ===");
        System.out.println("Add Product    : O(1)");
        System.out.println("Update Product : O(1)");
        System.out.println("Delete Product : O(1)");
        System.out.println("Display All    : O(n)");

        System.out.println("\n=== OPTIMIZATION ===");
        System.out.println("HashMap is used because it provides");
        System.out.println("fast insertion, update and deletion.");
        System.out.println("It is more efficient than ArrayList");
        System.out.println("for large inventories.");
    }
}