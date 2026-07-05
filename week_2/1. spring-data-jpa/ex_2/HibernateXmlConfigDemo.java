/*
 * Hands on 2 - Hibernate XML Config implementation walk through
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HibernateXmlConfigDemo {

    /*
     * Equivalent Employee.hbm.xml mapping:
     *
     * <hibernate-mapping>
     *   <class name="Employee" table="EMPLOYEE">
     *     <id name="id" column="id"><generator class="native"/></id>
     *     <property name="firstName" column="first_name"/>
     *     <property name="lastName"  column="last_name"/>
     *     <property name="salary"    column="salary"/>
     *   </class>
     * </hibernate-mapping>
     */
    static class Employee {
        int id;
        String firstName;
        String lastName;
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

    // hibernate.cfg.xml driver/url/username/password/dialect held here conceptually
    static class SessionFactory {
        private final Map<Integer, Employee> employeeTable = new LinkedHashMap<>();
        private int nextId = 1;

        Session openSession() { return new Session(this); }
    }

    static class Transaction {
        private boolean active = true;
        void commit() {
            if (!active) throw new IllegalStateException("Transaction already closed");
            active = false;
            System.out.println("[Transaction] commit()");
        }
        void rollback() {
            if (!active) return;
            active = false;
            System.out.println("[Transaction] rollback()");
        }
    }

    static class Session {
        private final SessionFactory factory;
        private boolean open = true;

        Session(SessionFactory factory) { this.factory = factory; }

        Transaction beginTransaction() {
            System.out.println("[Session] beginTransaction()");
            return new Transaction();
        }

        Integer save(Employee employee) {
            employee.id = factory.nextId++;
            factory.employeeTable.put(employee.id, employee);
            System.out.println("[Session] save() -> " + employee);
            return employee.id;
        }

        @SuppressWarnings("unchecked")
        List<Employee> createQueryListEmployees() {
            List<Employee> list = new ArrayList<>(factory.employeeTable.values());
            System.out.println("[Session] createQuery(\"FROM Employee\").list() -> " + list.size() + " row(s)");
            return list;
        }

        Employee get(int id) {
            Employee e = factory.employeeTable.get(id);
            System.out.println("[Session] get(Employee.class, " + id + ") -> " + e);
            return e;
        }

        void delete(Employee employee) {
            factory.employeeTable.remove(employee.id);
            System.out.println("[Session] delete() -> removed id " + employee.id);
        }

        void close() {
            open = false;
            System.out.println("[Session] close()");
        }

        boolean isOpen() { return open; }
    }

    /** Integer addEmployee(Employee employee) - mirrors the ManageEmployee DAO in the tutorial */
    static Integer addEmployee(SessionFactory factory, Employee employee) {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;
        try {
            tx = session.beginTransaction();
            employeeID = session.save(employee);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }

    static List<Employee> listEmployees(SessionFactory factory) {
        Session session = factory.openSession();
        List<Employee> employees = session.createQueryListEmployees();
        session.close();
        return employees;
    }

    static Employee getEmployee(SessionFactory factory, int id) {
        Session session = factory.openSession();
        Employee employee = session.get(id);
        session.close();
        return employee;
    }

    static void deleteEmployee(SessionFactory factory, int id) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Employee employee = session.get(id);
        session.delete(employee);
        tx.commit();
        session.close();
    }

    public static void main(String[] args) {
        System.out.println("=== Hibernate XML Config walk through (simulated) ===");
        SessionFactory factory = new SessionFactory();

        System.out.println("\n-- CREATE --");
        addEmployee(factory, new Employee("Zara", "Ali", 1000));
        addEmployee(factory, new Employee("Daisy", "Das", 5000));
        addEmployee(factory, new Employee("John", "Paul", 10000));

        System.out.println("\n-- READ (list) --");
        for (Employee e : listEmployees(factory)) {
            System.out.println("   " + e);
        }

        System.out.println("\n-- READ (get by id) --");
        getEmployee(factory, 2);

        System.out.println("\n-- DELETE --");
        deleteEmployee(factory, 1);

        System.out.println("\n-- READ (list after delete) --");
        for (Employee e : listEmployees(factory)) {
            System.out.println("   " + e);
        }
    }
}
