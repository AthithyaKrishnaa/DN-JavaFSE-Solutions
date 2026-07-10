// Exercise 7 - Implementing Constructor and Setter Injection
public class ConstructorSetterInjectionDemo {

    static final String APPLICATION_CONTEXT_XML =
            "<beans>\n" +
            "  <bean id=\"bookRepository\" class=\"com.library.repository.BookRepository\"/>\n" +
            "  <bean id=\"auditRepository\" class=\"com.library.repository.AuditRepository\"/>\n" +
            "  <bean id=\"bookService\" class=\"com.library.service.BookService\">\n" +
            "    <constructor-arg ref=\"bookRepository\"/>\n" +
            "    <property name=\"auditRepository\" ref=\"auditRepository\"/>\n" +
            "  </bean>\n" +
            "</beans>";

    static class BookRepository {
        String findBook() {
            return "War and Peace";
        }
    }

    static class AuditRepository {
        void record(String action) {
            System.out.println("[Audit] " + action);
        }
    }

    static class BookService {
        private final BookRepository bookRepository;
        private AuditRepository auditRepository;

        BookService(BookRepository bookRepository) {
            this.bookRepository = bookRepository;
        }

        void setAuditRepository(AuditRepository auditRepository) {
            this.auditRepository = auditRepository;
        }

        String borrowBook() {
            String title = bookRepository.findBook();
            if (auditRepository != null) {
                auditRepository.record("borrowed " + title);
            }
            return title;
        }
    }

    public static void main(String[] args) {
        System.out.println("Loading configuration:\n" + APPLICATION_CONTEXT_XML);

        BookRepository bookRepository = new BookRepository();
        AuditRepository auditRepository = new AuditRepository();
        BookService bookService = new BookService(bookRepository);
        bookService.setAuditRepository(auditRepository);

        System.out.println("borrowBook() -> " + bookService.borrowBook());
    }
}
