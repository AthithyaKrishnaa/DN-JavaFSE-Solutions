/*
 * Exercise 1 - User and Order Management System
 */
import java.util.HashMap;
import java.util.Map;

public class UserOrderManagementDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-30s %s%n", "main", level, name, msg);
        }
    }

    // @Entity User { id, name, email } -- persisted via Spring Data JPA on MySQL
    static class User {
        final Long id;
        final String name;
        final String email;
        User(Long id, String name, String email) { this.id = id; this.name = name; this.email = email; }
        @Override
        public String toString() { return "User{id=" + id + ", name=" + name + ", email=" + email + "}"; }
    }

    // @Entity Order { id, userId, item, amount } -- persisted via Spring Data JPA on MySQL
    static class Order {
        final Long id;
        final Long userId;
        final String item;
        final double amount;
        Order(Long id, Long userId, String item, double amount) {
            this.id = id; this.userId = userId; this.item = item; this.amount = amount;
        }
        @Override
        public String toString() {
            return "Order{id=" + id + ", userId=" + userId + ", item=" + item + ", amount=" + amount + "}";
        }
    }

    // @RestController UserController -- backed by UserRepository (Spring Data JPA)
    static class UserService {
        private final Map<Long, User> users = new HashMap<>();
        private final Logger logger;
        UserService(Logger logger) { this.logger = logger; }

        User save(User user) {
            users.put(user.id, user);
            logger.info("POST /api/users -> saved " + user);
            return user;
        }

        User findById(Long id) {
            logger.info("GET /api/users/" + id);
            User user = users.get(id);
            logger.info("response=" + user);
            return user;
        }
    }

    // @RestController OrderController -- calls UserService via WebClient (WebFlux)
    static class OrderService {
        private final Map<Long, Order> orders = new HashMap<>();
        private final UserService userServiceClient;
        private final Logger logger;
        OrderService(UserService userServiceClient, Logger logger) {
            this.userServiceClient = userServiceClient;
            this.logger = logger;
        }

        Order placeOrder(Order order) {
            logger.info("POST /api/orders -> validating userId=" + order.userId + " via WebClient GET http://user-service/api/users/" + order.userId);
            User user = userServiceClient.findById(order.userId);
            if (user == null) {
                logger.info("response=404 user not found, order rejected");
                return null;
            }
            orders.put(order.id, order);
            logger.info("order persisted " + order + " for " + user.name);
            return order;
        }

        void listOrdersForUser(Long userId) {
            logger.info("GET /api/orders?userId=" + userId);
            orders.values().stream()
                    .filter(order -> order.userId.equals(userId))
                    .forEach(order -> logger.info("  -> " + order));
        }
    }

    private static final Logger LOGGER = new Logger("UserOrderManagementDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        UserService userService = new UserService(LOGGER);
        OrderService orderService = new OrderService(userService, LOGGER);

        userService.save(new User(1L, "Athithya", "athithya@example.com"));
        userService.save(new User(2L, "Keshav", "keshav@example.com"));

        orderService.placeOrder(new Order(101L, 1L, "Mechanical Keyboard", 3499.00));
        orderService.placeOrder(new Order(102L, 2L, "Wireless Mouse", 899.50));
        orderService.placeOrder(new Order(103L, 1L, "USB-C Hub", 1299.00));
        orderService.placeOrder(new Order(104L, 5L, "Monitor Stand", 599.00));

        orderService.listOrdersForUser(1L);

        LOGGER.info("End");
    }
}
