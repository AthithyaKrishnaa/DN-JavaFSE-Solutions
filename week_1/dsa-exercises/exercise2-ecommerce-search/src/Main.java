/**
 * Demonstrates and times linear search vs binary search for product lookup.
 */
public class Main {

    public static void main(String[] args) {
        Product[] products = {
                new Product("P001", "Bluetooth Speaker", "Electronics"),
                new Product("P002", "Yoga Mat", "Fitness"),
                new Product("P003", "Air Fryer", "Kitchen"),
                new Product("P004", "Desk Lamp", "Home"),
                new Product("P005", "Running Shoes", "Fitness"),
                new Product("P006", "Coffee Grinder", "Kitchen"),
                new Product("P007", "Notebook Stand", "Office")
        };

        String target = "Air Fryer";

        // Linear search works directly on the unsorted array.
        Product linearResult = SearchAlgorithms.linearSearch(products, target);
        System.out.println("Linear search result: " + linearResult);

        // Binary search requires a sorted array.
        Product[] sorted = SearchAlgorithms.sortedByName(products);
        System.out.println("\nArray sorted by name for binary search:");
        for (Product p : sorted) {
            System.out.println("  " + p);
        }

        Product binaryResult = SearchAlgorithms.binarySearch(sorted, target);
        System.out.println("\nBinary search result: " + binaryResult);

        // Demonstrate a miss case for both.
        String missingTarget = "Drone";
        System.out.println("\nSearching for non-existent product \"" + missingTarget + "\":");
        System.out.println("Linear search result: " + SearchAlgorithms.linearSearch(products, missingTarget));
        System.out.println("Binary search result: " + SearchAlgorithms.binarySearch(sorted, missingTarget));
    }
}
