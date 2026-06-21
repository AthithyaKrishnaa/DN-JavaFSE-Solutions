/**
 * Demonstrates searching the library catalog by title using linear and binary search.
 */
public class Main {

    public static void main(String[] args) {
        Book[] catalog = {
                new Book("B001", "The Pragmatic Programmer", "David Thomas"),
                new Book("B002", "Clean Code", "Robert C. Martin"),
                new Book("B003", "Introduction to Algorithms", "Thomas H. Cormen"),
                new Book("B004", "Effective Java", "Joshua Bloch"),
                new Book("B005", "Design Patterns", "Erich Gamma")
        };

        String target = "Effective Java";

        // Linear search works on the catalog as-is, regardless of order.
        Book linearResult = LibrarySearch.linearSearchByTitle(catalog, target);
        System.out.println("Linear search result: " + linearResult);

        // Binary search needs the catalog sorted by title first.
        Book[] sortedCatalog = LibrarySearch.sortedByTitle(catalog);
        System.out.println("\nCatalog sorted by title:");
        for (Book b : sortedCatalog) {
            System.out.println("  " + b);
        }

        Book binaryResult = LibrarySearch.binarySearchByTitle(sortedCatalog, target);
        System.out.println("\nBinary search result: " + binaryResult);

        String missingTitle = "The Hobbit";
        System.out.println("\nSearching for a title not in the catalog (\"" + missingTitle + "\"):");
        System.out.println("Linear search result: " + LibrarySearch.linearSearchByTitle(catalog, missingTitle));
        System.out.println("Binary search result: " + LibrarySearch.binarySearchByTitle(sortedCatalog, missingTitle));
    }
}
