# Exercise 4: Employee Management System (Arrays)

## Scenario
An employee management system stores employee records and needs to support
adding, searching, traversing, and deleting them.

## How arrays are represented in memory
An array allocates a single contiguous block of memory, large enough to hold
every element back-to-back. Because every element is the same fixed size,
the address of element `i` can be computed directly as
`baseAddress + i * elementSize`, which is why indexed access (`array[i]`) is
O(1) — there's no searching involved, just arithmetic.

### Advantages
- O(1) random access by index.
- Excellent cache locality (elements are physically adjacent in memory),
  which makes traversal fast in practice.
- Simple, predictable memory layout.

### Disadvantages
- Fixed size at creation — growing the array means allocating a new, larger
  block and copying every existing element over (O(n)).
- Inserting or deleting in the middle requires shifting all subsequent
  elements (O(n)).
- Memory must be allocated contiguously, which can be wasteful if the array
  is over-provisioned, or insufficient if it's under-provisioned.

## Files
- `Employee.java` — data model (`employeeId`, `name`, `position`, `salary`)
- `EmployeeArrayManager.java` — raw-array-backed manager with manual resizing and shifting
- `Main.java` — demo driver, including a deliberately small starting capacity to show automatic resizing in action

## Complexity summary

| Operation | Time Complexity | Why |
|-----------|------------------|-----|
| Add       | O(1) amortized   | Appending to the end is O(1); occasional resizes cost O(n) but are infrequent (capacity doubles each time) |
| Search    | O(n)             | No way to know an element's position without checking each one |
| Traverse  | O(n)             | Must visit every element exactly once |
| Delete    | O(n)             | Finding the element is O(n); shifting remaining elements left to close the gap is also O(n) |

## Limitations of arrays, and when to use them
Arrays are the right choice when:
- The dataset size is known (or has a reasonable upper bound) ahead of time.
- Random access by index is the dominant access pattern.
- Insertions/deletions are rare compared to reads.

They become a poor fit when the dataset grows and shrinks frequently and
unpredictably, or when most operations involve inserting/removing from the
middle — that's exactly the gap a linked list (see Exercise 5) is designed to
fill.

## How to run
```bash
cd src
javac *.java
java Main
```
