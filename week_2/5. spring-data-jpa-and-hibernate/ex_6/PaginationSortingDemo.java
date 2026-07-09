// Exercise 6 - Employee Management System - Implementing Pagination and Sorting
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PaginationSortingDemo {

    static class Employee {
        Long id;
        String name;
        double salary;

        Employee(Long id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "', salary=" + salary + "}";
        }
    }

    static class Sort {
        final String property;
        final boolean ascending;

        Sort(String property, boolean ascending) {
            this.property = property;
            this.ascending = ascending;
        }
    }

    static class Pageable {
        final int pageNumber;
        final int pageSize;
        final Sort sort;

        Pageable(int pageNumber, int pageSize, Sort sort) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.sort = sort;
        }
    }

    static class Page<T> {
        final List<T> content;
        final int pageNumber;
        final int totalElements;
        final int totalPages;

        Page(List<T> content, int pageNumber, int pageSize, int totalElements) {
            this.content = content;
            this.pageNumber = pageNumber;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        }

        @Override
        public String toString() {
            return "Page{content=" + content + ", pageNumber=" + pageNumber + ", totalElements=" + totalElements + ", totalPages=" + totalPages + "}";
        }
    }

    static class EmployeeRepository {
        private final List<Employee> table = new ArrayList<>();

        void save(Employee employee) {
            table.add(employee);
        }

        Page<Employee> findAll(Pageable pageable) {
            List<Employee> sorted = new ArrayList<>(table);
            Comparator<Employee> comparator = pageable.sort.property.equals("salary")
                    ? Comparator.comparingDouble(e -> e.salary)
                    : Comparator.comparing(e -> e.name);
            if (!pageable.sort.ascending) comparator = comparator.reversed();
            sorted.sort(comparator);

            int fromIndex = Math.min(pageable.pageNumber * pageable.pageSize, sorted.size());
            int toIndex = Math.min(fromIndex + pageable.pageSize, sorted.size());
            List<Employee> content = sorted.subList(fromIndex, toIndex);
            return new Page<>(content, pageable.pageNumber, pageable.pageSize, sorted.size());
        }
    }

    public static void main(String[] args) {
        EmployeeRepository repository = new EmployeeRepository();
        repository.save(new Employee(1L, "Alice", 95000));
        repository.save(new Employee(2L, "Bob", 65000));
        repository.save(new Employee(3L, "Carol", 72000));
        repository.save(new Employee(4L, "Dave", 88000));
        repository.save(new Employee(5L, "Erin", 60000));

        Pageable pageable = new Pageable(0, 2, new Sort("salary", false));
        Page<Employee> page = repository.findAll(pageable);
        System.out.println("Page 0, size 2, sorted by salary desc -> " + page);

        Pageable nextPage = new Pageable(1, 2, new Sort("salary", false));
        System.out.println("Page 1, size 2, sorted by salary desc -> " + repository.findAll(nextPage));

        Pageable byName = new Pageable(0, 5, new Sort("name", true));
        System.out.println("Page 0, size 5, sorted by name asc -> " + repository.findAll(byName));
    }
}
