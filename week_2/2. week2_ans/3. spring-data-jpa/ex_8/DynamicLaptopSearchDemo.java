/*
 * Hands on 6 - Criteria Query
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DynamicLaptopSearchDemo {

    static final class Logger {
        private final String name;
        Logger(String name) { this.name = name; }
        void info(String msg) { print("INFO", msg); }
        void debug(String msg, Object arg) { print("DEBUG", msg.replace("{}", String.valueOf(arg))); }
        private void print(String level, String msg) {
            System.out.printf("%-20s %5s %-20s %s%n", "main", level, name, msg);
        }
    }

    static class Laptop {
        private String model;
        private String ramSize;
        private String hardDiskSize;
        private String operatingSystem;
        private String cpu;
        private double customerReview;

        Laptop(String model, String ramSize, String hardDiskSize, String operatingSystem, String cpu, double customerReview) {
            this.model = model;
            this.ramSize = ramSize;
            this.hardDiskSize = hardDiskSize;
            this.operatingSystem = operatingSystem;
            this.cpu = cpu;
            this.customerReview = customerReview;
        }

        @Override
        public String toString() {
            return "Laptop [model=" + model + ", ramSize=" + ramSize + ", hardDiskSize=" + hardDiskSize
                    + ", operatingSystem=" + operatingSystem + ", cpu=" + cpu + ", customerReview=" + customerReview + "]";
        }
    }

    static class CriteriaBuilder {
        private final List<Predicate<Laptop>> predicates = new ArrayList<>();

        CriteriaBuilder ramSize(String ramSize) {
            predicates.add(laptop -> laptop.ramSize.equals(ramSize));
            return this;
        }

        CriteriaBuilder operatingSystem(String operatingSystem) {
            predicates.add(laptop -> laptop.operatingSystem.equals(operatingSystem));
            return this;
        }

        CriteriaBuilder cpu(String cpu) {
            predicates.add(laptop -> laptop.cpu.equals(cpu));
            return this;
        }

        CriteriaBuilder minimumCustomerReview(double minimumReview) {
            predicates.add(laptop -> laptop.customerReview >= minimumReview);
            return this;
        }

        List<Laptop> search(List<Laptop> laptops) {
            List<Laptop> result = new ArrayList<>();
            for (Laptop laptop : laptops) {
                boolean matches = true;
                for (Predicate<Laptop> predicate : predicates) {
                    if (!predicate.test(laptop)) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    result.add(laptop);
                }
            }
            return result;
        }
    }

    private static final Logger LOGGER = new Logger("DynamicLaptopSearchDemo");

    public static void main(String[] args) {
        LOGGER.info("Inside main");

        List<Laptop> laptops = List.of(
                new Laptop("Aria 14", "8GB", "512GB SSD", "Windows", "Intel i5", 4.2),
                new Laptop("Nimbus Pro", "16GB", "1TB SSD", "Windows", "Intel i7", 4.6),
                new Laptop("Zephyr Air", "16GB", "512GB SSD", "macOS", "Apple M2", 4.8),
                new Laptop("Vantage Lite", "8GB", "256GB SSD", "Linux", "AMD Ryzen 5", 3.9));

        Map<String, String> userSelectedCriteria = new LinkedHashMap<>();
        userSelectedCriteria.put("ramSize", "16GB");
        userSelectedCriteria.put("operatingSystem", "Windows");

        LOGGER.info("Start");
        CriteriaBuilder firstSearch = new CriteriaBuilder();
        for (Map.Entry<String, String> criteria : userSelectedCriteria.entrySet()) {
            if (criteria.getKey().equals("ramSize")) {
                firstSearch.ramSize(criteria.getValue());
            } else if (criteria.getKey().equals("operatingSystem")) {
                firstSearch.operatingSystem(criteria.getValue());
            }
        }
        LOGGER.debug("results={}", firstSearch.search(laptops));
        LOGGER.info("End");

        LOGGER.info("Start");
        CriteriaBuilder secondSearch = new CriteriaBuilder()
                .cpu("Apple M2")
                .minimumCustomerReview(4.5);
        LOGGER.debug("results={}", secondSearch.search(laptops));
        LOGGER.info("End");
    }
}
