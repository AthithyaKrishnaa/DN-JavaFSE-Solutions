// Exercise 11: Implementing Dependency Injection
// Scenario: A customer management application where the service class
// depends on a repository class. Use constructor-based Dependency
// Injection to manage this dependency.

interface CustomerRepository {
    String findCustomerById(int id);
}

class CustomerRepositoryImpl implements CustomerRepository {
    public String findCustomerById(int id) {
        // Simulates fetching a customer record from a data source
        return "Customer{id=" + id + ", name=Priya Menon, balance=12500.0}";
    }
}

class CustomerService {
    private final CustomerRepository customerRepository;

    // Constructor injection: the dependency is provided externally
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void printCustomerDetails(int id) {
        String customer = customerRepository.findCustomerById(id);
        System.out.println("Fetched record: " + customer);
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== DEPENDENCY INJECTION: CUSTOMER MANAGEMENT ===");

        // The concrete repository is injected into the service via the constructor
        CustomerRepository repository = new CustomerRepositoryImpl();
        CustomerService customerService = new CustomerService(repository);

        System.out.println("\nLooking up customer with ID 101:");
        customerService.printCustomerDetails(101);

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("CustomerService depends only on the CustomerRepository");
        System.out.println("interface. The actual implementation is injected at");
        System.out.println("construction time, making the service easy to test and");
        System.out.println("swap implementations without changing CustomerService.");
    }
}
