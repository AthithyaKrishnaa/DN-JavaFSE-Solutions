// Exercise 1 - Configuring a Basic Spring Application
import java.util.LinkedHashMap;
import java.util.Map;

public class BasicSpringApplicationDemo {

    static final String POM_XML =
            "<project>\n" +
            "  <groupId>com.library</groupId>\n" +
            "  <artifactId>LibraryManagement</artifactId>\n" +
            "  <version>1.0.0</version>\n" +
            "  <dependencies>\n" +
            "    <dependency>\n" +
            "      <groupId>org.springframework</groupId>\n" +
            "      <artifactId>spring-context</artifactId>\n" +
            "      <version>5.3.30</version>\n" +
            "    </dependency>\n" +
            "  </dependencies>\n" +
            "</project>";

    static final String APPLICATION_CONTEXT_XML =
            "<beans>\n" +
            "  <bean id=\"bookRepository\" class=\"com.library.repository.BookRepository\"/>\n" +
            "  <bean id=\"bookService\" class=\"com.library.service.BookService\"/>\n" +
            "</beans>";

    static class BookRepository {
        String findBooks() {
            return "BookRepository is wired and ready";
        }
    }

    static class BookService {
        String checkStatus() {
            return "BookService bean loaded successfully";
        }
    }

    static class ApplicationContext {
        private final Map<String, Object> beans = new LinkedHashMap<>();

        ApplicationContext(String xmlConfig) {
            System.out.println("Loading configuration:\n" + xmlConfig);
            beans.put("bookRepository", new BookRepository());
            beans.put("bookService", new BookService());
            System.out.println("Registered beans: " + beans.keySet());
        }

        @SuppressWarnings("unchecked")
        <T> T getBean(String name) {
            return (T) beans.get(name);
        }
    }

    public static void main(String[] args) {
        System.out.println("pom.xml:\n" + POM_XML);
        System.out.println();

        ApplicationContext context = new ApplicationContext(APPLICATION_CONTEXT_XML);

        BookRepository bookRepository = context.getBean("bookRepository");
        BookService bookService = context.getBean("bookService");

        System.out.println(bookRepository.findBooks());
        System.out.println(bookService.checkStatus());
    }
}
