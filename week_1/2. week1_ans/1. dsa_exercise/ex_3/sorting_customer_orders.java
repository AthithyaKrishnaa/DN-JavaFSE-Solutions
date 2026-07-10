class Order {
    int orderId;
    String customerName;
    double totalPrice;

    Order(int orderId, String customerName, double totalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
    }

    public String toString() {
        return "OrderID: " + orderId +
                ", Customer: " + customerName +
                ", Price: " + totalPrice;
    }
}

public class Main {

    // Bubble Sort
    public static void bubbleSort(Order[] arr) {
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].totalPrice > arr[j + 1].totalPrice) {
                    Order temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Quick Sort helper
    public static void quickSort(Order[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    public static int partition(Order[] arr, int low, int high) {
        double pivot = arr[high].totalPrice;
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].totalPrice < pivot) {
                i++;
                Order temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Order temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    public static void printOrders(String title, Order[] arr) {
        System.out.println("\n" + title);
        for (Order o : arr) {
            System.out.println(o);
        }
    }

    public static void main(String[] args) {

        Order[] orders = {
                new Order(101, "Arun", 5000),
                new Order(102, "Bala", 1200),
                new Order(103, "Charan", 8000),
                new Order(104, "Deepa", 3000),
                new Order(105, "Esha", 2000)
        };

        // Bubble Sort
        Order[] bubbleArr = orders.clone();
        bubbleSort(bubbleArr);
        printOrders("=== Bubble Sort Result ===", bubbleArr);

        // Quick Sort
        Order[] quickArr = orders.clone();
        quickSort(quickArr, 0, quickArr.length - 1);
        printOrders("=== Quick Sort Result ===", quickArr);

        // Analysis
        System.out.println("\n=== TIME COMPLEXITY ANALYSIS ===");

        System.out.println("Bubble Sort:");
        System.out.println("Best Case    : O(n)");
        System.out.println("Average Case : O(n^2)");
        System.out.println("Worst Case   : O(n^2)");

        System.out.println("\nQuick Sort:");
        System.out.println("Best Case    : O(n log n)");
        System.out.println("Average Case : O(n log n)");
        System.out.println("Worst Case   : O(n^2)");

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("Quick Sort is faster because it divides the array");
        System.out.println("into smaller parts using divide-and-conquer.");
        System.out.println("Bubble Sort is slow because it repeatedly swaps");
        System.out.println("adjacent elements and compares many times.");
    }
}