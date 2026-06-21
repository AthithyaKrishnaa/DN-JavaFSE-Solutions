# Exercise 2: E-commerce Platform Search Function

## Scenario
An e-commerce platform's product search needs to be fast even as the catalog
grows.

## Big O notation
Big O notation describes how an algorithm's running time (or memory use)
grows as the input size `n` grows, ignoring constant factors and lower-order
terms. It answers "if the catalog doubles, roughly how much slower does this
get?" rather than giving an exact runtime, which makes it a fair way to
compare algorithms independent of hardware.

- **Best case**: the fewest steps an algorithm could take (e.g., the target is
  the very first element checked).
- **Average case**: the expected number of steps over typical/random input.
- **Worst case**: the most steps an algorithm could ever take — the usual
  focus of Big O, since it's the guarantee you can rely on.

## Algorithms implemented

### Linear Search
Checks each product one at a time until a match is found or the array is
exhausted. Works on data in any order.
- Best case: O(1) — target is the first element.
- Average/worst case: O(n) — target is near the end, or absent entirely.

### Binary Search
Repeatedly halves the search range by comparing the middle element. Requires
the array to be sorted first.
- Best case: O(1) — target is the middle element.
- Average/worst case: O(log n) — each comparison eliminates half the
  remaining candidates.

## Files
- `Product.java` — data model (`productId`, `productName`, `category`)
- `SearchAlgorithms.java` — `linearSearch`, `binarySearch`, and a `sortedByName` helper
- `Main.java` — demo driver comparing both on the same dataset

## Complexity comparison

| Algorithm     | Best Case | Average/Worst Case | Requires Sorted Data? |
|---------------|-----------|---------------------|------------------------|
| Linear Search | O(1)      | O(n)                 | No                     |
| Binary Search | O(1)      | O(log n)             | Yes                    |

## Which is more suitable, and why
Binary search wins decisively as the catalog grows — searching a million
products takes roughly 20 comparisons instead of up to a million. The
trade-off is the sorting prerequisite: if products are added/removed
frequently and the array must be re-sorted every time, that O(n log n) sort
cost can eat into the savings. In practice, e-commerce platforms sidestep
this entirely by using a proper search index (e.g., Elasticsearch) rather
than re-sorting a raw array, but for a fixed or infrequently-changing catalog,
sorting once and using binary search is the right call here.

## How to run
```bash
cd src
javac *.java
java Main
```
