/**
 * A singly linked list implementation for managing tasks.
 *
 * Unlike an array, a linked list does not need contiguous memory or upfront
 * capacity: each node points to the next, so growing the list never requires
 * copying existing elements. This makes insertion at the head/tail O(1),
 * at the cost of O(n) random access (no direct indexing) and O(n) search.
 */
public class TaskLinkedList {

    private Node head;
    private Node tail;
    private int size;

    public TaskLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds a task to the end of the list.
     * Time complexity: O(1), since we keep a tail reference and never need to
     * walk the list to find the insertion point.
     */
    public void add(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Searches for a task by id.
     * Time complexity: O(n) — there is no random access, so every node from
     * the head must be visited in the worst case.
     */
    public Task search(String taskId) {
        Node current = head;
        while (current != null) {
            if (current.data.getTaskId().equals(taskId)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Visits every task from head to tail.
     * Time complexity: O(n) — must visit every node exactly once.
     */
    public void traverse() {
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    /**
     * Deletes the task with the given id by re-linking around it.
     * Time complexity: O(n) — finding the node (and its predecessor) requires
     * walking the list, but the removal itself, once found, is O(1).
     */
    public boolean delete(String taskId) {
        if (head == null) {
            return false;
        }

        // Special case: removing the head.
        if (head.data.getTaskId().equals(taskId)) {
            head = head.next;
            if (head == null) {
                tail = null; // list is now empty
            }
            size--;
            return true;
        }

        Node previous = head;
        Node current = head.next;
        while (current != null) {
            if (current.data.getTaskId().equals(taskId)) {
                previous.next = current.next;
                if (current == tail) {
                    tail = previous; // removed the last node; update tail
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
