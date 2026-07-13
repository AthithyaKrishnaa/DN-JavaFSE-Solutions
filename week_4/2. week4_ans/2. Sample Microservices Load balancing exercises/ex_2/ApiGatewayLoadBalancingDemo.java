/*
 * Hands on 2 - Load Balancing in an API Gateway
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ApiGatewayLoadBalancingDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // spring.cloud.gateway.routes[0].id / uri=lb://example-service / predicates[0]
    static class RouteDefinition {
        final String id;
        final String uri;
        final String pathPredicate;
        RouteDefinition(String id, String uri, String pathPredicate) {
            this.id = id;
            this.uri = uri;
            this.pathPredicate = pathPredicate;
        }
    }

    static class ServiceInstance {
        final String host;
        final int port;
        ServiceInstance(String host, int port) { this.host = host; this.port = port; }
        @Override
        public String toString() { return host + ":" + port; }
    }

    // ServiceInstanceListSupplier - instances registered for 'example-service'
    static class ServiceInstanceListSupplier {
        private final List<ServiceInstance> instances = new ArrayList<>();
        void register(ServiceInstance instance) { instances.add(instance); }
        List<ServiceInstance> get() { return instances; }
    }

    // @Bean ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(...)
    static class RandomLoadBalancer {
        private final ServiceInstanceListSupplier instanceListSupplier;
        private final String serviceId;
        private final Random random;
        RandomLoadBalancer(ServiceInstanceListSupplier instanceListSupplier, String serviceId, Random random) {
            this.instanceListSupplier = instanceListSupplier;
            this.serviceId = serviceId;
            this.random = random;
        }

        ServiceInstance choose() {
            List<ServiceInstance> instances = instanceListSupplier.get();
            int index = random.nextInt(instances.size());
            return instances.get(index);
        }
    }

    private static final Logger LOGGER = new Logger("ApiGatewayLoadBalancingDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        RouteDefinition loadBalancedRoute = new RouteDefinition(
                "load_balanced_route", "lb://example-service", "/loadbalanced/**");
        LOGGER.info("registered route id=" + loadBalancedRoute.id + " uri=" + loadBalancedRoute.uri
                + " predicate=Path=" + loadBalancedRoute.pathPredicate);

        ServiceInstanceListSupplier instanceListSupplier = new ServiceInstanceListSupplier();
        instanceListSupplier.register(new ServiceInstance("10.0.0.11", 8081));
        instanceListSupplier.register(new ServiceInstance("10.0.0.12", 8081));
        instanceListSupplier.register(new ServiceInstance("10.0.0.13", 8081));
        LOGGER.info("service instances registered for 'example-service': " + instanceListSupplier.get());

        RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer(
                instanceListSupplier, "example-service", new Random(7));

        String[] incomingRequests = { "/loadbalanced/orders", "/loadbalanced/orders", "/loadbalanced/orders",
                "/loadbalanced/orders", "/loadbalanced/orders" };
        for (String requestPath : incomingRequests) {
            LOGGER.info("curl command: curl -s http://localhost:8080" + requestPath);
            ServiceInstance chosenInstance = randomLoadBalancer.choose();
            String forwardedUri = "http://" + chosenInstance + requestPath;
            LOGGER.info("RandomLoadBalancer chose instance " + chosenInstance + ", forwarding to " + forwardedUri);
            LOGGER.info("HTTP/1.1 200");
        }

        LOGGER.info("End");
    }
}
