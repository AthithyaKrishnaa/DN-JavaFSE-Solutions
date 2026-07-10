// Exercise 1 - Employee Management System - Overview and Setup
public class EmployeeManagementSetupDemo {

    static final String POM_XML =
            "<project>\n" +
            "  <groupId>com.company</groupId>\n" +
            "  <artifactId>EmployeeManagementSystem</artifactId>\n" +
            "  <version>1.0.0</version>\n" +
            "  <dependencies>\n" +
            "    <dependency><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>\n" +
            "    <dependency><artifactId>h2</artifactId></dependency>\n" +
            "    <dependency><artifactId>spring-boot-starter-web</artifactId></dependency>\n" +
            "    <dependency><artifactId>lombok</artifactId></dependency>\n" +
            "  </dependencies>\n" +
            "</project>";

    static final String APPLICATION_PROPERTIES =
            "spring.datasource.url=jdbc:h2:mem:testdb\n" +
            "spring.datasource.driverClassName=org.h2.Driver\n" +
            "spring.datasource.username=sa\n" +
            "spring.datasource.password=password\n" +
            "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect";

    static class DataSource {
        final String url;
        final String driverClassName;
        final String username;

        DataSource(String properties) {
            String u = null, d = null, user = null;
            for (String line : properties.split("\n")) {
                if (line.startsWith("spring.datasource.url=")) u = line.substring("spring.datasource.url=".length());
                if (line.startsWith("spring.datasource.driverClassName=")) d = line.substring("spring.datasource.driverClassName=".length());
                if (line.startsWith("spring.datasource.username=")) user = line.substring("spring.datasource.username=".length());
            }
            this.url = u;
            this.driverClassName = d;
            this.username = user;
        }

        boolean connect() {
            System.out.println("Connecting to " + url + " with driver " + driverClassName + " as " + username);
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println("pom.xml:\n" + POM_XML);
        System.out.println("\napplication.properties:\n" + APPLICATION_PROPERTIES);

        DataSource dataSource = new DataSource(APPLICATION_PROPERTIES);
        boolean connected = dataSource.connect();
        System.out.println("\nEmployeeManagementSystem started, H2 connection established: " + connected);
    }
}
