/*
 * Hands on 4 - Difference between JPA, Hibernate and Spring Data JPA
 */
import java.util.LinkedHashMap;
import java.util.Map;

public class JpaHibernateSpringDataJpaComparisonDemo {

    static class Employee {
        int id;
        String firstName;
        int salary;

        Employee(String firstName, int salary) {
            this.firstName = firstName;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "Employee [id=" + id + ", firstName=" + firstName + ", salary=" + salary + "]";
        }
    }

    // ----------------------------------------------------------------
    // Hibernate style: manual Session / Transaction handling
    // ----------------------------------------------------------------
    static class HibernateSessionFactory {
        final Map<Integer, Employee> store = new LinkedHashMap<>();
        int nextId = 1;
    }

    static class HibernateSession {
        private final HibernateSessionFactory factory;
        HibernateSession(HibernateSessionFactory factory) { this.factory = factory; }

        boolean beginTransaction() {
            System.out.println("   [Hibernate] session.beginTransaction()");
            return true;
        }

        Integer save(Employee employee) {
            employee.id = factory.nextId++;
            factory.store.put(employee.id, employee);
            System.out.println("   [Hibernate] session.save(employee) -> id=" + employee.id);
            return employee.id;
        }

        void commit() { System.out.println("   [Hibernate] tx.commit()"); }
        void rollback() { System.out.println("   [Hibernate] tx.rollback()"); }
        void close() { System.out.println("   [Hibernate] session.close()"); }
    }

    /** Mirrors the tutorial's `Integer addEmployee(Employee employee)` DAO method. */
    static Integer addEmployeeHibernateStyle(HibernateSessionFactory factory, Employee employee) {
        HibernateSession session = new HibernateSession(factory);
        Integer employeeID = null;
        try {
            session.beginTransaction();
            employeeID = session.save(employee);
            session.commit();
        } catch (RuntimeException e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
        return employeeID;
    }

    // ----------------------------------------------------------------
    // Spring Data JPA style: EmployeeRepository + @Transactional service
    // ----------------------------------------------------------------
    // public interface EmployeeRepository extends JpaRepository<Employee, Integer> {}
    static class EmployeeRepository {
        private final Map<Integer, Employee> store = new LinkedHashMap<>();
        private int nextId = 1;

        Employee save(Employee employee) {
            employee.id = nextId++;
            store.put(employee.id, employee);
            return employee;
        }
    }

    // @Autowired private EmployeeRepository employeeRepository;
    // @Transactional public void addEmployee(Employee employee) { employeeRepository.save(employee); }
    static class EmployeeService {
        private final EmployeeRepository employeeRepository;
        EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }

        void addEmployee(Employee employee) {
            System.out.println("   [SpringDataJPA] employeeRepository.save(employee)");
            employeeRepository.save(employee);
            System.out.println("   [SpringDataJPA] saved -> id=" + employee.id);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JPA vs Hibernate vs Spring Data JPA: addEmployee() comparison ===");

        System.out.println("\n-- Hibernate style (manual Session/Transaction) --");
        HibernateSessionFactory factory = new HibernateSessionFactory();
        Employee e1 = new Employee("Kiran", 4500);
        Integer id1 = addEmployeeHibernateStyle(factory, e1);
        System.out.println("Result: " + e1 + " (returned id=" + id1 + ")");

        System.out.println("\n-- Spring Data JPA style (repository + @Transactional service) --");
        EmployeeRepository repository = new EmployeeRepository();
        EmployeeService service = new EmployeeService(repository);
        Employee e2 = new Employee("Asha", 6200);
        service.addEmployee(e2);
        System.out.println("Result: " + e2);

        System.out.println("\n-- Summary --");
        System.out.println("JPA             : specification only (JSR 338), no implementation of its own");
        System.out.println("Hibernate       : concrete JPA implementation, needs explicit Session/Transaction handling");
        System.out.println("Spring Data JPA : abstraction over a JPA provider (e.g. Hibernate);");
        System.out.println("                  repository.save() + @Transactional removes the boilerplate seen above");
    }
}
