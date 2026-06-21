import java.util.Arrays;

/**
 * Manages employee records using a plain Java array (not a List).
 *
 * Arrays store elements in contiguous memory, which gives O(1) index-based
 * access but makes insertion/deletion expensive because elements must be
 * shifted, and growth requires allocating an entirely new, larger array.
 */
public class EmployeeArrayManager {

    private Employee[] employees;
    private int count; // number of slots currently in use

    public EmployeeArrayManager(int initialCapacity) {
        employees = new Employee[initialCapacity];
        count = 0;
    }

    /**
     * Adds an employee at the end of the array.
     * Time complexity: O(1) amortized; O(n) on the occasions a resize is needed,
     * since every existing element must be copied into the new, larger array.
     */
    public void add(Employee employee) {
        if (count == employees.length) {
            resize(employees.length * 2);
        }
        employees[count] = employee;
        count++;
    }

    private void resize(int newCapacity) {
        employees = Arrays.copyOf(employees, newCapacity);
    }

    /**
     * Searches for an employee by id.
     * Time complexity: O(n) — every slot may need to be checked.
     */
    public Employee search(String employeeId) {
        for (int i = 0; i < count; i++) {
            if (employees[i].getEmployeeId().equals(employeeId)) {
                return employees[i];
            }
        }
        return null;
    }

    /**
     * Visits every employee in order.
     * Time complexity: O(n) — must visit every element exactly once.
     */
    public void traverse() {
        for (int i = 0; i < count; i++) {
            System.out.println(employees[i]);
        }
    }

    /**
     * Deletes an employee by id, shifting subsequent elements left to close the gap.
     * Time complexity: O(n) — locating the element is O(n), and shifting the
     * remaining elements left is also O(n) in the worst case (deleting near the front).
     */
    public boolean delete(String employeeId) {
        for (int i = 0; i < count; i++) {
            if (employees[i].getEmployeeId().equals(employeeId)) {
                for (int j = i; j < count - 1; j++) {
                    employees[j] = employees[j + 1];
                }
                employees[count - 1] = null;
                count--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return count;
    }
}
