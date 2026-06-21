import java.util.Arrays;
import java.util.Comparator;

/**
 * Provides linear and binary search over a library's book catalog, searching by title.
 */
public class LibrarySearch {

    /**
     * Linear search by title: checks each book in turn, in whatever order the
     * array happens to be in.
     * Time complexity: O(n) worst/average case.
     * Suitable for: small catalogs, or catalogs that change frequently and
     * aren't worth re-sorting after every update.
     */
    public static Book linearSearchByTitle(Book[] books, String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Binary search by title: requires the array to already be sorted by title.
     * Time complexity: O(log n) worst/average case.
     * Suitable for: large, mostly-static catalogs where the cost of sorting is
     * paid once and many searches benefit afterward.
     */
    public static Book binarySearchByTitle(Book[] sortedBooks, String title) {
        int low = 0;
        int high = sortedBooks.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = sortedBooks[mid].getTitle().compareToIgnoreCase(title);

            if (comparison == 0) {
                return sortedBooks[mid];
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    /** Returns a new array of books sorted alphabetically by title. */
    public static Book[] sortedByTitle(Book[] books) {
        Book[] copy = Arrays.copyOf(books, books.length);
        Arrays.sort(copy, Comparator.comparing(Book::getTitle, String::compareToIgnoreCase));
        return copy;
    }
}
