import java.util.Arrays;
import java.util.Comparator;

class Product {
    int productId;
    String productName;
    String category;

    Product(int productId, String productName, String category) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
    }

    public String toString() {
        return "ID: " + productId +
                ", Name: " + productName +
                ", Category: " + category;
    }
}

public class Main {

    // Linear Search
    public static Product linearSearch(Product[] products, int targetId) {
        for (Product p : products) {
            if (p.productId == targetId) {
                return p;
            }
        }
        return null;
    }

    // Binary Search
    public static Product binarySearch(Product[] products, int targetId) {
        int low = 0;
        int high = products.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (products[mid].productId == targetId) {
                return products[mid];
            } else if (products[mid].productId < targetId) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    public static void main(String[] args) {

        Product[] products = {
                new Product(104, "Laptop", "Electronics"),
                new Product(101, "Mouse", "Electronics"),
                new Product(103, "Shoes", "Fashion"),
                new Product(102, "Book", "Education"),
                new Product(105, "Phone", "Electronics")
        };

        int searchId = 103;

        // LINEAR SEARCH
        System.out.println("=== LINEAR SEARCH ===");
        Product result1 = linearSearch(products, searchId);

        if (result1 != null) {
            System.out.println("Product Found:");
            System.out.println(result1);
        } else {
            System.out.println("Product Not Found");
        }

        // SORT FOR BINARY SEARCH
        Arrays.sort(products, Comparator.comparingInt(p -> p.productId));

        System.out.println("\n=== SORTED ARRAY ===");
        for (Product p : products) {
            System.out.println(p);
        }

        // BINARY SEARCH
        System.out.println("\n=== BINARY SEARCH ===");
        Product result2 = binarySearch(products, searchId);

        if (result2 != null) {
            System.out.println("Product Found:");
            System.out.println(result2);
        } else {
            System.out.println("Product Not Found");
        }

        // ANALYSIS
        System.out.println("\n=== BIG O ANALYSIS ===");

        System.out.println("Linear Search:");
        System.out.println("Best Case    : O(1)");
        System.out.println("Average Case : O(n)");
        System.out.println("Worst Case   : O(n)");

        System.out.println("\nBinary Search:");
        System.out.println("Best Case    : O(1)");
        System.out.println("Average Case : O(log n)");
        System.out.println("Worst Case   : O(log n)");

        // CONCLUSION
        System.out.println("\n=== CONCLUSION ===");
        System.out.println("Binary Search is more efficient for large");
        System.out.println("e-commerce platforms because it reduces");
        System.out.println("the search space by half in each step.");
        System.out.println("However, the products must be sorted.");
    }
}