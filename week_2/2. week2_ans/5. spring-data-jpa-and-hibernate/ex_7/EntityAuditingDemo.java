// Exercise 7 - Employee Management System - Enabling Entity Auditing
import java.time.LocalDateTime;

public class EntityAuditingDemo {

    static class Employee {
        Long id;
        String name;
        String createdBy;
        String lastModifiedBy;
        LocalDateTime createdDate;
        LocalDateTime lastModifiedDate;

        Employee(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', createdBy='" + createdBy
                    + "', lastModifiedBy='" + lastModifiedBy + "', createdDate=" + createdDate
                    + ", lastModifiedDate=" + lastModifiedDate + "}";
        }
    }

    static class AuditorAware {
        String currentAuditor() {
            return "system";
        }
    }

    static class AuditingEntityListener {
        private final AuditorAware auditorAware;

        AuditingEntityListener(AuditorAware auditorAware) {
            this.auditorAware = auditorAware;
        }

        void prePersist(Employee employee) {
            LocalDateTime now = LocalDateTime.now();
            employee.createdDate = now;
            employee.lastModifiedDate = now;
            employee.createdBy = auditorAware.currentAuditor();
            employee.lastModifiedBy = auditorAware.currentAuditor();
            System.out.println("@PrePersist -> stamped createdDate/createdBy on " + employee.name);
        }

        void preUpdate(Employee employee) {
            employee.lastModifiedDate = LocalDateTime.now();
            employee.lastModifiedBy = auditorAware.currentAuditor();
            System.out.println("@PreUpdate -> stamped lastModifiedDate/lastModifiedBy on " + employee.name);
        }
    }

    public static void main(String[] args) {
        System.out.println("@EnableJpaAuditing configured, AuditorAware<String> bean registered");

        AuditingEntityListener listener = new AuditingEntityListener(new AuditorAware());
        Employee employee = new Employee(1L, "Alice");

        listener.prePersist(employee);
        System.out.println(employee);

        employee.name = "Alice Smith";
        listener.preUpdate(employee);
        System.out.println(employee);
    }
}
