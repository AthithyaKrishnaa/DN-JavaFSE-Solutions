// Exercise 2 - Implementing Dependency Injection
public class DependencyInjectionDemo {

    static final String APPLICATION_CONTEXT_XML =
            "<beans>\n" +
            "  <bean id=\"bookRepository\" class=\"com.library.repository.BookRepository\"/>\n" +
            "  <bean id=\"bookService\" class=\"com.library.service.BookService\">\n" +
            "    <property name=\"bookRepository\" ref=\"bookRepository\"/>\n" +
            "  </bean>\n" +
            "</beans>";

    static class BookRepository {
        String getBookTitle(int id) {
            return id == 1 ? "The Alchemist" : "Unknown Book";
        }
    }

    static class BookService {
        private BookRepository bookRepository;

        void setBookRepository(BookRepository bookRepository) {
            this.bookRepository = bookRepository;
        }

        String fetchBook(int id) {
            if (bookRepository == null) {
                throw new IllegalStateException("bookRepository is not injected");
            }
            return bookRepository.getBookTitle(id);
        }
    }

    static class LibraryManagementApplication {
        static BookService loadContext() {
            System.out.println("Loading configuration:\n" + APPLICATION_CONTEXT_XML);
            BookRepository bookRepository = new BookRepository();
            BookService bookService = new BookService();
            bookService.setBookRepository(bookRepository);
            System.out.println("Injected BookRepository into BookService via setter");
            return bookService;
        }
    }

    public static void main(String[] args) {
        BookService bookService = LibraryManagementApplication.loadContext();
        System.out.println("fetchBook(1) -> " + bookService.fetchBook(1));
    }
}
