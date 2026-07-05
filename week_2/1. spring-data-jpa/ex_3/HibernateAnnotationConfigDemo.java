/*
 * Hands on 3 - Hibernate Annotation Config implementation walk through
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.LinkedHashMap;
import java.util.Map;

public class HibernateAnnotationConfigDemo {

    // ---- local stand-ins for javax.persistence annotations ----
    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE)
    @interface Entity {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE)
    @interface Table { String name(); }

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    @interface Id {}

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    @interface GeneratedValue { String strategy() default "AUTO"; }

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    @interface Column { String name(); }

    @Entity
    @Table(name = "EMPLOYEE")
    static class Employee {
        @Id
        @GeneratedValue(strategy = "IDENTITY")
        @Column(name = "id")
        int id;

        @Column(name = "first_name")
        String firstName;

        @Column(name = "last_name")
        String lastName;

        @Column(name = "salary")
        int salary;

        Employee() {}
        Employee(String firstName, String lastName, int salary) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", firstName=" + firstName
                    + ", lastName=" + lastName + ", salary=" + salary + "]";
        }
    }

    /*
     * hibernate.cfg.xml (conceptual, since no Hibernate/DB runtime exists here)
     *
     * <hibernate-configuration>
     *   <session-factory>
     *     <property name="hibernate.dialect">org.hibernate.dialect.MySQL5Dialect</property>
     *     <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
     *     <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/ormlearn</property>
     *     <property name="hibernate.connection.username">root</property>
     *     <property name="hibernate.connection.password">root</property>
     *     <mapping class="Employee"/>
     *   </session-factory>
     * </hibernate-configuration>
     */
    static final class HibernateCfg {
        static final String DIALECT = "org.hibernate.dialect.MySQL5Dialect";
        static final String DRIVER = "com.mysql.cj.jdbc.Driver";
        static final String URL = "jdbc:mysql://localhost:3306/ormlearn";
        static final String USERNAME = "root";

        static void print() {
            System.out.println("hibernate.cfg.xml ->");
            System.out.println("  dialect  = " + DIALECT);
            System.out.println("  driver   = " + DRIVER);
            System.out.println("  url      = " + URL);
            System.out.println("  username = " + USERNAME);
            System.out.println("  password = ****");
        }
    }

    static class ManageEmployee {
        private final Map<Integer, Employee> table = new LinkedHashMap<>();
        private int nextId = 1;

        Integer addEmployee(Employee employee) {
            employee.id = nextId++;
            table.put(employee.id, employee);
            System.out.println("[session.save] " + employee);
            return employee.id;
        }

        void updateEmployee(int id, int salary) {
            Employee e = table.get(id);
            e.salary = salary;
            System.out.println("[session.update] " + e);
        }

        void listEmployees() {
            System.out.println("[session.createQuery(\"FROM Employee\").list()]");
            for (Employee e : table.values()) System.out.println("   " + e);
        }

        void deleteEmployee(int id) {
            Employee e = table.remove(id);
            System.out.println("[session.delete] removed " + e);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Hibernate Annotation Config walk through (simulated) ===");
        HibernateCfg.print();

        ManageEmployee me = new ManageEmployee();

        System.out.println("\n-- CREATE --");
        Integer empID1 = me.addEmployee(new Employee("Manoj", "Kumar", 4000));
        Integer empID2 = me.addEmployee(new Employee("Dilip", "Kumar", 3000));
        Integer empID3 = me.addEmployee(new Employee("Maria", "Pia", 10000));

        System.out.println("\n-- READ --");
        me.listEmployees();

        System.out.println("\n-- UPDATE (empID1 salary) --");
        me.updateEmployee(empID1, 5000);

        System.out.println("\n-- DELETE (empID2) --");
        me.deleteEmployee(empID2);

        System.out.println("\n-- READ (after update/delete) --");
        me.listEmployees();

        System.out.println("\nremaining employee ids: " + empID1 + ", " + empID3);
    }
}
