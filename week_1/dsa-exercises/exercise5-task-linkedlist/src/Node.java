/**
 * A single node in the singly linked list, holding one Task and a reference
 * to the next node in the chain.
 */
public class Node {

    Task data;
    Node next;

    public Node(Task data) {
        this.data = data;
        this.next = null;
    }
}
