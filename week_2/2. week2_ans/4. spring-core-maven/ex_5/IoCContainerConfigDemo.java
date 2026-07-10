// Exercise 5 - Configuring the Spring IoC Container
import java.util.LinkedHashMap;
import java.util.Map;

public class IoCContainerConfigDemo {

    static final String APPLICATION_CONTEXT_XML =
            "<beans>\n" +
            "  <bean id=\"bookRepository\" class=\"com.library.repository.BookRepository\"/>\n" +
            "  <bean id=\"bookService\" class=\"com.library.service.BookService\">\n" +
            "    <property name=\"bookRepository\" ref=\"bookRepository\"/>\n" +
            "  </bean>\n" +
            "</beans>";

    static class BookRepository {
        Map<Integer, String> books = new LinkedHashMap<>();

        BookRepository() {
            books.put(1, "1984");
            books.put(2, "Brave New World");
        }

        String findById(int id) {
            return books.get(id);
        }
    }

    static class BookService {
        private BookRepository bookRepository;

        void setBookRepository(BookRepository bookRepository) {
            this.bookRepository = bookRepository;
        }

        String getBook(int id) {
            return bookRepository.findById(id);
        }
    }

    static class IoCContainer {
        private final Map<String, Object> beans = new LinkedHashMap<>();

        void loadConfiguration(String xmlConfig) {
            System.out.println("Loading configuration:\n" + xmlConfig);
            BookRepository bookRepository = new BookRepository();
            BookService bookService = new BookService();
            bookService.setBookRepository(bookRepository);
            beans.put("bookRepository", bookRepository);
            beans.put("bookService", bookService);
        }

        @SuppressWarnings("unchecked")
        <T> T getBean(String name) {
            return (T) beans.get(name);
        }
    }

    public static void main(String[] args) {
        IoCContainer container = new IoCContainer();
        container.loadConfiguration(APPLICATION_CONTEXT_XML);

        BookService bookService = container.getBean("bookService");
        System.out.println("getBook(1) -> " + bookService.getBook(1));
        System.out.println("getBook(2) -> " + bookService.getBook(2));
    }
}
