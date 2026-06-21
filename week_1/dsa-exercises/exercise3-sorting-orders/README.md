# Exercise 3: Sorting Customer Orders

## Scenario
Customer orders on an e-commerce platform need to be sorted by total price so
high-value orders can be prioritized.

## Sorting algorithms, briefly
- **Bubble Sort**: repeatedly steps through the array, swapping adjacent
  elements that are out of order. Simple but slow, since each pass only
  guarantees one element (the largest unsorted one) reaches its final position.
- **Insertion Sort**: builds the sorted array one element at a time, inserting
  each new element into its correct position among the already-sorted prefix.
  Efficient for small or nearly-sorted datasets.
- **Quick Sort**: picks a pivot, partitions the array into elements less than
  and greater than the pivot, then recursively sorts each partition.
  Efficient in practice due to good cache behavior and few comparisons.
- **Merge Sort**: recursively splits the array in half, sorts each half, then
  merges the sorted halves back together. Guarantees O(n log n) even in the
  worst case, at the cost of needing extra memory for the merge step.

## Algorithms implemented
This exercise implements **Bubble Sort** and **Quick Sort** to order `Order`
records by `totalPrice`.

## Files
- `Order.java` — data model (`orderId`, `customerName`, `totalPrice`)
- `SortingAlgorithms.java` — `bubbleSort` and `quickSort`, both sorting in place
- `Main.java` — demo driver sorting identical copies of sample data with each algorithm

## Complexity comparison

| Algorithm  | Best Case | Average Case | Worst Case | Space     | Stable? |
|------------|-----------|---------------|------------|-----------|---------|
| Bubble Sort| O(n)      | O(n²)         | O(n²)      | O(1)      | Yes     |
| Quick Sort | O(n log n)| O(n log n)    | O(n²)      | O(log n)* | No      |

\* Space here is for the recursion call stack; Quick Sort sorts in place
otherwise.

Bubble Sort's best case (O(n)) only applies because this implementation
includes an early-exit check: if a full pass makes no swaps, the array is
already sorted, and the algorithm stops immediately rather than continuing
through pointless passes.

## Why Quick Sort is generally preferred
Bubble Sort's O(n²) behavior comes from comparing/swapping adjacent pairs over
and over, redoing a lot of work each pass. Quick Sort instead discards roughly
half the remaining elements from consideration with every partition step,
which is why it averages O(n log n) — the same class as Merge Sort, but
typically faster in practice due to better cache locality and lower constant
overhead. Quick Sort's worst case (O(n²)) shows up only with a poor pivot
choice on adversarial or already-sorted input, and is commonly mitigated with
randomized pivot selection.

For a customer-order list that could realistically grow into the thousands or
millions of rows, Quick Sort's average-case advantage makes it the practical
choice; Bubble Sort is included here mainly as a baseline for comparison.

## How to run
```bash
cd src
javac *.java
java Main
```
