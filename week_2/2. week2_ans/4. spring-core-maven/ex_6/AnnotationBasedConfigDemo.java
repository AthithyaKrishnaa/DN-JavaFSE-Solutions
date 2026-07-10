// Exercise 6 - Configuring Beans with Annotations
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnnotationBasedConfigDemo {

    static final String APPLICATION_CONTEXT_XML =
            "<beans>\n" +
            "  <context:component-scan base-package=\"com.library\"/>\n" +
            "</beans>";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Service {
        String value() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Repository {
        String value() default "";
    }

    @Repository("bookRepository")
    static class BookRepository {
        String status() {
            return "BookRepository ready";
        }
    }

    @Service("bookService")
    static class BookService {
        String status() {
            return "BookService ready";
        }
    }

    static Map<String, Object> componentScan(Class<?>... candidates) {
        Map<String, Object> registry = new LinkedHashMap<>();
        for (Class<?> candidate : candidates) {
            try {
                if (candidate.isAnnotationPresent(Service.class)) {
                    Service annotation = candidate.getAnnotation(Service.class);
                    registry.put(annotation.value(), candidate.getDeclaredConstructor().newInstance());
                } else if (candidate.isAnnotationPresent(Repository.class)) {
                    Repository annotation = candidate.getAnnotation(Repository.class);
                    registry.put(annotation.value(), candidate.getDeclaredConstructor().newInstance());
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        return registry;
    }

    public static void main(String[] args) {
        System.out.println("Loading configuration:\n" + APPLICATION_CONTEXT_XML);

        Map<String, Object> beans = componentScan(BookRepository.class, BookService.class);
        System.out.println("Beans registered by component scan: " + beans.keySet());

        BookRepository bookRepository = (BookRepository) beans.get("bookRepository");
        BookService bookService = (BookService) beans.get("bookService");
        System.out.println(bookRepository.status());
        System.out.println(bookService.status());
    }
}
