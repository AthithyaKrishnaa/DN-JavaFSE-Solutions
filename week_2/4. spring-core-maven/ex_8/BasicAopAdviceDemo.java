// Exercise 8 - Implementing Basic AOP with Spring
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class BasicAopAdviceDemo {

    static final String APPLICATION_CONTEXT_XML =
            "<beans>\n" +
            "  <aop:aspectj-autoproxy proxy-target-class=\"true\"/>\n" +
            "  <bean id=\"loggingAspect\" class=\"com.library.aspect.LoggingAspect\"/>\n" +
            "  <bean id=\"bookService\" class=\"com.library.service.BookService\"/>\n" +
            "</beans>";

    interface BookServiceApi {
        String removeBook(String title);
    }

    static class BookService implements BookServiceApi {
        public String removeBook(String title) {
            return "Removed book: " + title;
        }
    }

    static class LoggingAspect implements InvocationHandler {
        private final Object target;

        LoggingAspect(Object target) {
            this.target = target;
        }

        void beforeAdvice(Method method) {
            System.out.println("[LoggingAspect] Before advice: about to call " + method.getName() + "()");
        }

        void afterAdvice(Method method) {
            System.out.println("[LoggingAspect] After advice: completed " + method.getName() + "()");
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            beforeAdvice(method);
            Object result = method.invoke(target, args);
            afterAdvice(method);
            return result;
        }
    }

    static BookServiceApi wrapWithAspect(BookServiceApi target) {
        return (BookServiceApi) Proxy.newProxyInstance(
                BasicAopAdviceDemo.class.getClassLoader(),
                new Class<?>[]{BookServiceApi.class},
                new LoggingAspect(target));
    }

    public static void main(String[] args) {
        System.out.println("Loading configuration:\n" + APPLICATION_CONTEXT_XML);

        BookServiceApi bookService = wrapWithAspect(new BookService());
        System.out.println(bookService.removeBook("Moby Dick"));
    }
}
