/*
 * Hands on 1 - Implementing Edge Services for Routing and Filtering
 */
import java.util.ArrayList;
import java.util.List;

public class GatewayRoutingFilteringDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // spring.cloud.gateway.routes[0].id / uri / predicates[0]
    static class RouteDefinition {
        final String id;
        final String uri;
        final String pathPredicate;
        RouteDefinition(String id, String uri, String pathPredicate) {
            this.id = id;
            this.uri = uri;
            this.pathPredicate = pathPredicate;
        }

        boolean matches(String requestPath) {
            String prefix = pathPredicate.substring(0, pathPredicate.indexOf("/**"));
            return requestPath.startsWith(prefix);
        }
    }

    // @Component GlobalFilter LoggingFilter
    static class LoggingFilter {
        private final Logger logger;
        LoggingFilter(Logger logger) { this.logger = logger; }

        void filter(String requestUri) {
            logger.info("Request: " + requestUri);
        }
    }

    static class RouteLocator {
        private final List<RouteDefinition> routes = new ArrayList<>();
        void addRoute(RouteDefinition route) { routes.add(route); }

        RouteDefinition resolve(String requestPath) {
            for (RouteDefinition route : routes) {
                if (route.matches(requestPath)) return route;
            }
            return null;
        }
    }

    private static final Logger LOGGER = new Logger("GatewayRoutingFilteringDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        RouteDefinition exampleRoute = new RouteDefinition("example_route", "http://example.org", "/example/**");
        RouteLocator routeLocator = new RouteLocator();
        routeLocator.addRoute(exampleRoute);
        LOGGER.info("registered route id=" + exampleRoute.id + " uri=" + exampleRoute.uri + " predicate=Path=" + exampleRoute.pathPredicate);

        LoggingFilter loggingFilter = new LoggingFilter(LOGGER);

        String[] incomingRequests = { "/example/orders", "/example/customers/42", "/other/health" };
        for (String requestPath : incomingRequests) {
            String requestUri = "http://localhost:8080" + requestPath;
            LOGGER.info("curl command: curl -s " + requestUri);
            RouteDefinition matchedRoute = routeLocator.resolve(requestPath);
            if (matchedRoute != null) {
                loggingFilter.filter(requestUri);
                String forwardedUri = matchedRoute.uri + requestPath;
                LOGGER.info("matched route '" + matchedRoute.id + "', forwarding to " + forwardedUri);
                LOGGER.info("HTTP/1.1 200");
            } else {
                LOGGER.info("no route predicate matched " + requestPath);
                LOGGER.info("HTTP/1.1 404");
            }
        }

        LOGGER.info("End");
    }
}
