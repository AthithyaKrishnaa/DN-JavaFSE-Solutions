class Task {
    int taskId;
    String taskName;
    String status;

    Task(int taskId, String taskName, String status) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
    }

    public String toString() {
        return "TaskID: " + taskId +
                ", Name: " + taskName +
                ", Status: " + status;
    }
}

// Node class for Linked List
class Node {
    Task task;
    Node next;

    Node(Task task) {
        this.task = task;
        this.next = null;
    }
}

class TaskLinkedList {

    Node head;

    // ADD (at end)
    public void addTask(Task task) {
        Node newNode = new Node(task);

        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }

        System.out.println("Task Added: " + task.taskId);
    }

    // SEARCH
    public void searchTask(int id) {
        System.out.println("\nSearching Task ID: " + id);

        Node temp = head;

        while (temp != null) {
            if (temp.task.taskId == id) {
                System.out.println("Task Found: " + temp.task);
                return;
            }
            temp = temp.next;
        }

        System.out.println("Task Not Found");
    }

    // TRAVERSE
    public void displayTasks() {
        System.out.println("\n=== Task List ===");

        Node temp = head;

        while (temp != null) {
            System.out.println(temp.task);
            temp = temp.next;
        }
    }

    // DELETE
    public void deleteTask(int id) {
        System.out.println("\nDeleting Task ID: " + id);

        if (head == null) {
            System.out.println("List is Empty");
            return;
        }

        if (head.task.taskId == id) {
            head = head.next;
            System.out.println("Task Deleted");
            return;
        }

        Node temp = head;

        while (temp.next != null && temp.next.task.taskId != id) {
            temp = temp.next;
        }

        if (temp.next == null) {
            System.out.println("Task Not Found");
        } else {
            temp.next = temp.next.next;
            System.out.println("Task Deleted");
        }
    }
}

public class Main {

    public static void main(String[] args) {

        TaskLinkedList list = new TaskLinkedList();

        // ADD TASKS
        System.out.println("=== ADD TASKS ===");

        list.addTask(new Task(1, "Design UI", "Pending"));
        list.addTask(new Task(2, "Build Backend", "In Progress"));
        list.addTask(new Task(3, "Testing", "Pending"));

        // DISPLAY
        list.displayTasks();

        // SEARCH
        list.searchTask(2);

        // DELETE
        list.deleteTask(2);

        // DISPLAY AFTER DELETE
        list.displayTasks();

        // ANALYSIS
        System.out.println("\n=== TIME COMPLEXITY ANALYSIS ===");

        System.out.println("Add Task (end)     : O(n)");
        System.out.println("Search Task        : O(n)");
        System.out.println("Traverse Tasks     : O(n)");
        System.out.println("Delete Task        : O(n)");

        System.out.println("\n=== ADVANTAGES OVER ARRAYS ===");
        System.out.println("- Dynamic size (no fixed limit)");
        System.out.println("- Easy insertion and deletion");
        System.out.println("- No shifting required like arrays");

        System.out.println("\n=== LINKED LIST TYPES ===");
        System.out.println("1. Singly Linked List → one direction");
        System.out.println("2. Doubly Linked List → both directions");
        System.out.println("3. Circular Linked List → last connects to first");
    }
}