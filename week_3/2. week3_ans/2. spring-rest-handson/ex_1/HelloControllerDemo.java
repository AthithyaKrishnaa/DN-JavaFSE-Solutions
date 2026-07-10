// Hands on - Hello World RESTful Web Service
import java.util.LinkedHashMap;
import java.util.Map;

public class HelloControllerDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-25s %s%n", "main", level, name, msg);
        }
    }

    static class HttpRequest {
        final String method;
        final String url;

        HttpRequest(String method, String url) {
            this.method = method;
            this.url = url;
        }
    }

    static class HttpResponse {
        final int status;
        final String contentType;
        final String body;

        HttpResponse(int status, String contentType, String body) {
            this.status = status;
            this.contentType = contentType;
            this.body = body;
        }

        @Override
        public String toString() {
            return "HTTP/1.1 " + status + " OK\nContent-Type: " + contentType + "\n\n" + body;
        }
    }

    static class HelloController {
        private static final Logger LOGGER = new Logger("HelloController");

        String sayHello() {
            LOGGER.info("START sayHello()");
            String result = "Hello World!!";
            LOGGER.info("END sayHello()");
            return result;
        }
    }

    static class DispatcherServlet {
        private final Map<String, HelloController> getMappings = new LinkedHashMap<>();

        DispatcherServlet() {
            getMappings.put("/hello", new HelloController());
        }

        HttpResponse dispatch(HttpRequest request) {
            System.out.println(request.method + " " + request.url + " HTTP/1.1");
            if ("/hello".equals(request.url)) {
                String body = getMappings.get("/hello").sayHello();
                return new HttpResponse(200, "text/plain", body);
            }
            return new HttpResponse(404, "text/plain", "Not Found");
        }
    }

    public static void main(String[] args) {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        HttpResponse response = dispatcherServlet.dispatch(new HttpRequest("GET", "/hello"));
        System.out.println(response);
    }
}
