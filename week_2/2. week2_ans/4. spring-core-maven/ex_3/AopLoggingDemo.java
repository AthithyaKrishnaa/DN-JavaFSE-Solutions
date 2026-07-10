// Exercise 3 - Implementing Logging with Spring AOP
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopLoggingDemo {

    static final String APPLICATION_CONTEXT_XML =
            "<beans>\n" +
            "  <aop:aspectj-autoproxy/>\n" +
            "  <bean id=\"loggingAspect\" class=\"com.library.aspect.LoggingAspect\"/>\n" +
            "  <bean id=\"bookService\" class=\"com.library.service.BookService\"/>\n" +
            "</beans>";

    interface BookServiceApi {
        String addBook(String title);
    }

    static class BookService implements BookServiceApi {
        public String addBook(String title) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ignored) {
            }
            return "Added book: " + title;
        }
    }

    static class LoggingAspect implements InvocationHandler {
        private final Object target;

        LoggingAspect(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long start = System.nanoTime();
            System.out.println("[LoggingAspect] before " + method.getName() + "()");
            Object result = method.invoke(target, args);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            System.out.println("[LoggingAspect] after " + method.getName() + "() executed in " + elapsedMs + "ms");
            return result;
        }
    }

    static BookServiceApi wrapWithLogging(BookServiceApi target) {
        return (BookServiceApi) Proxy.newProxyInstance(
                AopLoggingDemo.class.getClassLoader(),
                new Class<?>[]{BookServiceApi.class},
                new LoggingAspect(target));
    }

    public static void main(String[] args) {
        System.out.println("Loading configuration:\n" + APPLICATION_CONTEXT_XML);

        BookServiceApi bookService = wrapWithLogging(new BookService());
        String result = bookService.addBook("Clean Code");
        System.out.println(result);
    }
}
