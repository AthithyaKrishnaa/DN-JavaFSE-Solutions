import java.util.Arrays;

/**
 * Demonstrates sorting customer orders by totalPrice using Bubble Sort and Quick Sort.
 */
public class Main {

    public static void main(String[] args) {
        Order[] ordersForBubbleSort = {
                new Order("O001", "Asha", 250.50),
                new Order("O002", "Ravi", 89.99),
                new Order("O003", "Meera", 430.00),
                new Order("O004", "Karthik", 12.75),
                new Order("O005", "Divya", 199.99)
        };

        // Make an identical copy so both algorithms sort the same starting data.
        Order[] ordersForQuickSort = Arrays.copyOf(ordersForBubbleSort, ordersForBubbleSort.length);

        System.out.println("Original orders:");
        Arrays.stream(ordersForBubbleSort).forEach(System.out::println);

        SortingAlgorithms.bubbleSort(ordersForBubbleSort);
        System.out.println("\nSorted with Bubble Sort (ascending by totalPrice):");
        Arrays.stream(ordersForBubbleSort).forEach(System.out::println);

        SortingAlgorithms.quickSort(ordersForQuickSort);
        System.out.println("\nSorted with Quick Sort (ascending by totalPrice):");
        Arrays.stream(ordersForQuickSort).forEach(System.out::println);
    }
}
