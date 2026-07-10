// Exercise 10 - Employee Management System - Hibernate-Specific Features
import java.util.ArrayList;
import java.util.List;

public class HibernateFeaturesDemo {

    static final String HIBERNATE_PROPERTIES =
            "hibernate.dialect=org.hibernate.dialect.H2Dialect\n" +
            "hibernate.jdbc.batch_size=25\n" +
            "hibernate.order_inserts=true\n" +
            "hibernate.order_updates=true";

    static class Employee {
        Long id;
        String name;

        Employee(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "'}";
        }
    }

    static class BatchSession {
        private final int batchSize;
        private final List<Employee> pending = new ArrayList<>();
        private int flushCount = 0;

        BatchSession(int batchSize) {
            this.batchSize = batchSize;
        }

        void save(Employee employee) {
            pending.add(employee);
            System.out.println("save() -> " + employee);
            if (pending.size() % batchSize == 0) {
                flush();
            }
        }

        void flush() {
            flushCount++;
            System.out.println("[Hibernate] flushing batch #" + flushCount + " (" + pending.size() + " pending statement(s) since last flush accounted)");
        }

        void closeAndFlush() {
            if (!pending.isEmpty()) {
                flush();
            }
            System.out.println("Session closed, total flush operations: " + flushCount);
        }
    }

    public static void main(String[] args) {
        System.out.println("hibernate properties:\n" + HIBERNATE_PROPERTIES);

        System.out.println("\n@DynamicUpdate class Employee { ... } -> only changed columns included in UPDATE statements");

        BatchSession session = new BatchSession(2);
        for (int i = 1; i <= 5; i++) {
            session.save(new Employee((long) i, "Employee" + i));
        }
        session.closeAndFlush();
    }
}
