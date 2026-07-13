/*
 * Exercise 3 - API Gateway routing to Customer Service and Billing Service
 */
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ApiGatewayRoutingDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // spring.cloud.gateway.routes[].id / uri / predicates / filters
    static class RouteDefinition {
        final String id;
        final String uri;
        final String pathPredicate;
        final String rewritePath;
        RouteDefinition(String id, String uri, String pathPredicate, String rewritePath) {
            this.id = id; this.uri = uri; this.pathPredicate = pathPredicate; this.rewritePath = rewritePath;
        }
    }

    // RedisRateLimiter(replenishRate, burstCapacity) simulated with a token bucket
    static class RateLimiter {
        private final int replenishRatePerRequest;
        private int tokens;
        RateLimiter(int replenishRate, int burstCapacity) {
            this.replenishRatePerRequest = replenishRate;
            this.tokens = burstCapacity;
        }
        boolean tryConsume() {
            if (tokens > 0) {
                tokens -= 1;
                return true;
            }
            tokens += replenishRatePerRequest;
            return false;
        }
    }

    // LocalResponseCache / simple cache filter keyed by request path
    static class ResponseCache {
        private final Map<String, String> cache = new HashMap<>();
        String get(String path) { return cache.get(path); }
        void put(String path, String response) { cache.put(path, response); }
    }

    static class DownstreamService {
        final String name;
        final String host;
        int callCount = 0;
        DownstreamService(String name, String host) { this.name = name; this.host = host; }
        String handle(String path) {
            callCount++;
            return "200 OK from " + name + " for " + path;
        }
    }

    // GatewayFilterFactory chain: RewritePath -> RequestRateLimiter -> LocalResponseCache
    static class ApiGateway {
        private final Map<String, RouteDefinition> routes = new HashMap<>();
        private final Map<String, DownstreamService> services = new HashMap<>();
        private final Map<String, RateLimiter> rateLimiters = new HashMap<>();
        private final ResponseCache responseCache = new ResponseCache();
        private final Deque<String> requestLog = new ArrayDeque<>();
        private final Logger logger;

        ApiGateway(Logger logger) { this.logger = logger; }

        void addRoute(RouteDefinition route, DownstreamService service, RateLimiter limiter) {
            routes.put(route.id, route);
            services.put(route.id, service);
            rateLimiters.put(route.id, limiter);
            logger.info("registered route id=" + route.id + " uri=" + route.uri
                    + " predicate=Path=" + route.pathPredicate + " filter=RewritePath(->" + route.rewritePath + ")");
        }

        void routeRequest(String routeId, String incomingPath) {
            RouteDefinition route = routes.get(routeId);
            String rewrittenPath = route.rewritePath;
            logger.info("curl command: curl -s http://localhost:8080" + incomingPath);
            logger.info("RewritePath filter: " + incomingPath + " -> " + rewrittenPath);

            RateLimiter limiter = rateLimiters.get(routeId);
            if (!limiter.tryConsume()) {
                logger.info("RequestRateLimiter: route '" + routeId + "' exceeded, response=429 Too Many Requests");
                return;
            }
            logger.info("RequestRateLimiter: route '" + routeId + "' token consumed, request allowed");

            String cached = responseCache.get(rewrittenPath);
            if (cached != null) {
                logger.info("LocalResponseCache: cache hit for " + rewrittenPath + ", response=" + cached);
                return;
            }

            DownstreamService service = services.get(routeId);
            String response = service.handle(rewrittenPath);
            responseCache.put(rewrittenPath, response);
            logger.info("forwarded to " + service.host + rewrittenPath + ", response=" + response);
        }
    }

    private static final Logger LOGGER = new Logger("ApiGatewayRoutingDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        ApiGateway gateway = new ApiGateway(LOGGER);

        DownstreamService customerService = new DownstreamService("customer-service", "10.0.0.31:8081");
        DownstreamService billingService = new DownstreamService("billing-service", "10.0.0.32:8082");

        gateway.addRoute(
                new RouteDefinition("customer_route", "lb://customer-service", "/customers/**", "/api/customers"),
                customerService, new RateLimiter(1, 2));

        gateway.addRoute(
                new RouteDefinition("billing_route", "lb://billing-service", "/billing/**", "/api/billing"),
                billingService, new RateLimiter(1, 2));

        String[] customerRequests = { "/customers/1001", "/customers/1001", "/customers/1002", "/customers/1002" };
        for (String path : customerRequests) {
            gateway.routeRequest("customer_route", path);
        }

        String[] billingRequests = { "/billing/invoice/501", "/billing/invoice/502", "/billing/invoice/503" };
        for (String path : billingRequests) {
            gateway.routeRequest("billing_route", path);
        }

        LOGGER.info("End");
    }
}
