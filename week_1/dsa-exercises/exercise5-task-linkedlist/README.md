# Exercise 5: Task Management System (Linked List)

## Scenario
A task management system needs tasks to be added, deleted, and traversed
efficiently, with frequent changes to the task list.

## Types of linked lists
- **Singly Linked List**: each node holds data plus a single reference to the
  *next* node. Traversal is one-directional (head to tail).
- **Doubly Linked List**: each node holds references to both the *next* and
  *previous* nodes. This allows traversal in either direction and makes
  deletion of a known node O(1) (no need to track/find the predecessor), at
  the cost of extra memory per node for the second reference.

This exercise implements a **Singly Linked List**, which is sufficient since
tasks are only ever processed head-to-tail.

## Files
- `Task.java` — data model (`taskId`, `taskName`, `status`)
- `Node.java` — internal node holding a `Task` and a reference to the next node
- `TaskLinkedList.java` — the singly linked list itself, with `add`, `search`, `traverse`, `delete`
- `Main.java` — demo driver, including deletion of both the head and a non-head node

## Complexity summary

| Operation | Time Complexity | Why |
|-----------|------------------|-----|
| Add (to tail) | O(1) | A `tail` reference is maintained, so appending never requires walking the list |
| Search    | O(n) | No random access — must follow `next` pointers from the head until found |
| Traverse  | O(n) | Must visit every node exactly once |
| Delete    | O(n) | Finding the node (and its predecessor) takes O(n); unlinking it is O(1) once found |

## Advantages of linked lists over arrays for dynamic data
- **No fixed capacity**: nodes are allocated individually as needed, so there's
  no need to "resize" or pre-guess capacity, and no large copy operation when
  growing.
- **O(1) insertion at the head or tail**: no shifting of existing elements is
  required, unlike an array where inserting at the front means shifting
  everything else over by one.
- **Memory doesn't need to be contiguous**: nodes can live anywhere in memory,
  which avoids the "not enough contiguous space" failure mode arrays can hit.

The trade-off is that linked lists lose O(1) random access (no `list[i]`
shortcut — you must walk from the head) and use more memory per element
(each node carries an extra pointer). For a task list where tasks are
constantly being added and removed, and access is sequential rather than
by-index, those trade-offs favor the linked list.

## How to run
```bash
cd src
javac *.java
java Main
```
