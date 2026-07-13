/*
 * Exercise 2 - Inventory Management System with Service Discovery
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDiscoveryInventoryDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    static class ServiceInstance {
        final String serviceId;
        final String host;
        final int port;
        ServiceInstance(String serviceId, String host, int port) {
            this.serviceId = serviceId; this.host = host; this.port = port;
        }
        @Override
        public String toString() { return host + ":" + port; }
    }

    // Eureka Server - @EnableEurekaServer, in-memory service registry
    static class EurekaServer {
        private final Map<String, List<ServiceInstance>> registry = new HashMap<>();
        private final Logger logger;
        EurekaServer(Logger logger) { this.logger = logger; }

        void register(ServiceInstance instance) {
            registry.computeIfAbsent(instance.serviceId, key -> new ArrayList<>()).add(instance);
            logger.info("EurekaServer: registered " + instance.serviceId + " at " + instance);
        }

        ServiceInstance discover(String serviceId) {
            List<ServiceInstance> instances = registry.get(serviceId);
            ServiceInstance chosen = instances.get(0);
            logger.info("EurekaServer: lookup '" + serviceId + "' -> " + chosen);
            return chosen;
        }
    }

    // Config Server - @EnableConfigServer, serves centralized application.yml properties
    static class ConfigServer {
        private final Map<String, String> sharedProperties = new HashMap<>();
        private final Logger logger;
        ConfigServer(Logger logger) {
            this.logger = logger;
            sharedProperties.put("inventory.low-stock-threshold", "10");
            sharedProperties.put("inventory.warehouse.region", "ap-south-1");
        }

        String getProperty(String serviceId, String key) {
            String value = sharedProperties.get(key);
            logger.info("ConfigServer: " + serviceId + " fetched config '" + key + "'=" + value);
            return value;
        }
    }

    // @Entity Product { id, name, unitPrice } -- registered with Eureka as 'product-service'
    static class Product {
        final Long id;
        final String name;
        final double unitPrice;
        Product(Long id, String name, double unitPrice) { this.id = id; this.name = name; this.unitPrice = unitPrice; }
    }

    // @RestController ProductController -- @EnableDiscoveryClient
    static class ProductService {
        private final Map<Long, Product> products = new HashMap<>();
        private final Logger logger;
        ProductService(Logger logger) { this.logger = logger; }

        void addProduct(Product product) {
            products.put(product.id, product);
            logger.info("POST /api/products -> added productId=" + product.id + " name=" + product.name);
        }

        Product getProduct(Long id) {
            return products.get(id);
        }
    }

    // @RestController InventoryController -- @EnableDiscoveryClient, discovers 'product-service' via Eureka
    static class InventoryService {
        private final Map<Long, Integer> stockLevels = new HashMap<>();
        private final EurekaServer eurekaServer;
        private final ConfigServer configServer;
        private final Logger logger;

        InventoryService(EurekaServer eurekaServer, ConfigServer configServer, Logger logger) {
            this.eurekaServer = eurekaServer;
            this.configServer = configServer;
            this.logger = logger;
        }

        void setStock(Long productId, int quantity) {
            stockLevels.put(productId, quantity);
            logger.info("PUT /api/inventory/" + productId + " -> quantity=" + quantity);
        }

        void checkStock(Long productId, ProductService productServiceClient) {
            ServiceInstance productServiceInstance = eurekaServer.discover("product-service");
            logger.info("InventoryService: calling GET http://" + productServiceInstance + "/api/products/" + productId);
            Product product = productServiceClient.getProduct(productId);

            int threshold = Integer.parseInt(configServer.getProperty("inventory-service", "inventory.low-stock-threshold"));
            int quantity = stockLevels.getOrDefault(productId, 0);
            logger.info("GET /api/inventory/" + productId + " -> product=" + product.name
                    + ", stock=" + quantity + ", threshold=" + threshold);
            if (quantity < threshold) {
                logger.info("ALERT: low stock for " + product.name + " (stock=" + quantity + " < threshold=" + threshold + ")");
            }
        }
    }

    private static final Logger LOGGER = new Logger("ServiceDiscoveryInventoryDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        EurekaServer eurekaServer = new EurekaServer(LOGGER);
        ConfigServer configServer = new ConfigServer(LOGGER);

        eurekaServer.register(new ServiceInstance("product-service", "10.0.0.21", 8081));
        eurekaServer.register(new ServiceInstance("inventory-service", "10.0.0.22", 8082));

        ProductService productService = new ProductService(LOGGER);
        InventoryService inventoryService = new InventoryService(eurekaServer, configServer, LOGGER);

        productService.addProduct(new Product(1L, "Laptop", 55000.00));
        productService.addProduct(new Product(2L, "Wireless Charger", 1500.00));

        inventoryService.setStock(1L, 25);
        inventoryService.setStock(2L, 6);

        inventoryService.checkStock(1L, productService);
        inventoryService.checkStock(2L, productService);

        LOGGER.info("End");
    }
}
