/**
 * Demonstrates add, search, traverse, and delete operations on a singly
 * linked list of tasks.
 */
public class Main {

    public static void main(String[] args) {
        TaskLinkedList taskList = new TaskLinkedList();

        taskList.add(new Task("T001", "Design database schema", "In Progress"));
        taskList.add(new Task("T002", "Write unit tests", "Pending"));
        taskList.add(new Task("T003", "Deploy to staging", "Pending"));
        taskList.add(new Task("T004", "Code review", "Completed"));

        System.out.println("All tasks:");
        taskList.traverse();

        System.out.println("\nSearching for T003:");
        System.out.println(taskList.search("T003"));

        System.out.println("\nSearching for non-existent task T999:");
        System.out.println(taskList.search("T999"));

        taskList.delete("T001"); // delete the head
        taskList.delete("T003"); // delete a middle/tail node
        System.out.println("\nAfter deleting T001 and T003, remaining tasks:");
        taskList.traverse();

        System.out.println("\nTotal tasks remaining: " + taskList.size());
    }
}
