import java.util.Arrays;
import java.util.Comparator;

class Book {
    int bookId;
    String title;
    String author;

    Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }

    public String toString() {
        return "BookID: " + bookId +
                ", Title: " + title +
                ", Author: " + author;
    }
}

public class Main {

    // LINEAR SEARCH (by title)
    public static Book linearSearch(Book[] books, String title) {
        for (Book b : books) {
            if (b.title.equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    // BINARY SEARCH (by title)
    public static Book binarySearch(Book[] books, String title) {
        int low = 0;
        int high = books.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;

            int compare = books[mid].title.compareToIgnoreCase(title);

            if (compare == 0) {
                return books[mid];
            } else if (compare < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return null;
    }

    public static void main(String[] args) {

        Book[] books = {
                new Book(1, "Java Basics", "James"),
                new Book(2, "DSA Guide", "Mark"),
                new Book(3, "Operating Systems", "Galvin"),
                new Book(4, "Database Systems", "Korth"),
                new Book(5, "Python Basics", "Guido")
        };

        String searchTitle = "Operating Systems";

        // LINEAR SEARCH
        System.out.println("=== LINEAR SEARCH ===");
        Book result1 = linearSearch(books, searchTitle);

        if (result1 != null) {
            System.out.println("Book Found:");
            System.out.println(result1);
        } else {
            System.out.println("Book Not Found");
        }

        // SORT BOOKS FOR BINARY SEARCH
        Arrays.sort(books, Comparator.comparing(b -> b.title.toLowerCase()));

        System.out.println("\n=== SORTED BOOK LIST (For Binary Search) ===");
        for (Book b : books) {
            System.out.println(b);
        }

        // BINARY SEARCH
        System.out.println("\n=== BINARY SEARCH ===");
        Book result2 = binarySearch(books, searchTitle);

        if (result2 != null) {
            System.out.println("Book Found:");
            System.out.println(result2);
        } else {
            System.out.println("Book Not Found");
        }

        // ANALYSIS
        System.out.println("\n=== TIME COMPLEXITY ANALYSIS ===");

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
        System.out.println("Linear Search is used when data is unsorted or small.");
        System.out.println("Binary Search is used when data is sorted and large.");
        System.out.println("Binary Search is much faster for large datasets.");
    }
}