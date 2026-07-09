// Exercise 9 - Employee Management System - Customizing Data Source Configuration
import java.util.LinkedHashMap;
import java.util.Map;

public class DataSourceConfigDemo {

    static final String APPLICATION_PROPERTIES =
            "app.datasource.primary.url=jdbc:h2:mem:employeedb\n" +
            "app.datasource.primary.driver-class-name=org.h2.Driver\n" +
            "app.datasource.primary.username=sa\n" +
            "app.datasource.secondary.url=jdbc:h2:mem:reportingdb\n" +
            "app.datasource.secondary.driver-class-name=org.h2.Driver\n" +
            "app.datasource.secondary.username=sa";

    static class DataSourceProperties {
        String url;
        String driverClassName;
        String username;

        @Override
        public String toString() {
            return "DataSourceProperties{url='" + url + "', driverClassName='" + driverClassName + "', username='" + username + "'}";
        }
    }

    static Map<String, DataSourceProperties> parse(String properties, String... prefixes) {
        Map<String, DataSourceProperties> result = new LinkedHashMap<>();
        for (String prefix : prefixes) {
            result.put(prefix, new DataSourceProperties());
        }
        for (String line : properties.split("\n")) {
            for (String prefix : prefixes) {
                String key = "app.datasource." + prefix + ".";
                if (line.startsWith(key)) {
                    String rest = line.substring(key.length());
                    String[] parts = rest.split("=", 2);
                    DataSourceProperties dsProperties = result.get(prefix);
                    switch (parts[0]) {
                        case "url": dsProperties.url = parts[1]; break;
                        case "driver-class-name": dsProperties.driverClassName = parts[1]; break;
                        case "username": dsProperties.username = parts[1]; break;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("application.properties:\n" + APPLICATION_PROPERTIES);

        Map<String, DataSourceProperties> dataSources = parse(APPLICATION_PROPERTIES, "primary", "secondary");

        System.out.println("\n@Primary @Bean primaryDataSource() -> " + dataSources.get("primary"));
        System.out.println("@Bean secondaryDataSource() -> " + dataSources.get("secondary"));
        System.out.println("\nEmployeeManagementSystem auto-configured " + dataSources.size() + " data source(s)");
    }
}
