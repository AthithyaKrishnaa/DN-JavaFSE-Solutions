// Exercise 9 - Creating a Spring Boot Application
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpringBootLibraryAppDemo {

    static final String APPLICATION_PROPERTIES =
            "spring.datasource.url=jdbc:h2:mem:librarydb\n" +
            "spring.datasource.driver-class-name=org.h2.Driver\n" +
            "spring.jpa.hibernate.ddl-auto=update\n" +
            "spring.h2.console.enabled=true";

    static class Book {
        Long id;
        String title;
        String author;

        Book(Long id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
        }

        @Override
        public String toString() {
            return "Book{id=" + id + ", title='" + title + "', author='" + author + "'}";
        }
    }

    static class BookRepository {
        private final Map<Long, Book> table = new LinkedHashMap<>();
        private long nextId = 1;

        List<Book> findAll() {
            return new ArrayList<>(table.values());
        }

        Optional<Book> findById(Long id) {
            return Optional.ofNullable(table.get(id));
        }

        Book save(Book book) {
            if (book.id == null) {
                book.id = nextId++;
            }
            table.put(book.id, book);
            return book;
        }

        void deleteById(Long id) {
            table.remove(id);
        }
    }

    static class BookController {
        private final BookRepository bookRepository;

        BookController(BookRepository bookRepository) {
            this.bookRepository = bookRepository;
        }

        List<Book> getAllBooks() {
            System.out.println("GET /api/books");
            return bookRepository.findAll();
        }

        Book getBookById(Long id) {
            System.out.println("GET /api/books/" + id);
            return bookRepository.findById(id).orElse(null);
        }

        Book addBook(String title, String author) {
            System.out.println("POST /api/books");
            return bookRepository.save(new Book(null, title, author));
        }

        void deleteBook(Long id) {
            System.out.println("DELETE /api/books/" + id);
            bookRepository.deleteById(id);
        }
    }

    public static void main(String[] args) {
        System.out.println("application.properties:\n" + APPLICATION_PROPERTIES);
        System.out.println("\nStarting LibraryManagement Spring Boot application...");

        BookRepository bookRepository = new BookRepository();
        BookController bookController = new BookController(bookRepository);

        bookController.addBook("Dune", "Frank Herbert");
        bookController.addBook("Foundation", "Isaac Asimov");

        System.out.println("\n-- Test REST endpoints --");
        for (Book book : bookController.getAllBooks()) {
            System.out.println("   " + book);
        }
        System.out.println("getBookById(1) -> " + bookController.getBookById(1L));

        bookController.deleteBook(1L);
        System.out.println("\nAfter delete:");
        for (Book book : bookController.getAllBooks()) {
            System.out.println("   " + book);
        }
    }
}
