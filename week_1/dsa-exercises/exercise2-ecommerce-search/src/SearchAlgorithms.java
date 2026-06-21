import java.util.Arrays;
import java.util.Comparator;

/**
 * Demonstrates linear search (unsorted array) and binary search (sorted array)
 * for finding a product by its name.
 */
public class SearchAlgorithms {

    /**
     * Linear search: scans every element until a match is found.
     * Time complexity: O(n) worst/average case, O(1) best case (first element).
     * Works on an unsorted array.
     */
    public static Product linearSearch(Product[] products, String targetName) {
        for (Product product : products) {
            if (product.getProductName().equalsIgnoreCase(targetName)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Binary search: repeatedly halves the search interval.
     * Time complexity: O(log n) worst/average case, O(1) best case (middle element).
     * Requires the array to be sorted by productName beforehand.
     */
    public static Product binarySearch(Product[] sortedProducts, String targetName) {
        int low = 0;
        int high = sortedProducts.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = sortedProducts[mid].getProductName().compareToIgnoreCase(targetName);

            if (comparison == 0) {
                return sortedProducts[mid];
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    /** Returns a new array sorted by product name, leaving the original untouched. */
    public static Product[] sortedByName(Product[] products) {
        Product[] copy = Arrays.copyOf(products, products.length);
        Arrays.sort(copy, Comparator.comparing(Product::getProductName, String::compareToIgnoreCase));
        return copy;
    }
}
