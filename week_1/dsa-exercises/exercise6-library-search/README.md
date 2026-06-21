# Exercise 6: Library Management System (Search by Title/Author)

## Scenario
A library management system lets users search for books by title, and the
search needs to scale as the catalog grows.

## Search algorithms
### Linear Search
Checks each book in the catalog one at a time, in whatever order they happen
to be stored, until a match is found or the catalog is exhausted.
- No ordering requirement on the data.
- Time complexity: O(n) worst/average case, O(1) best case.

### Binary Search
Requires the catalog to be sorted by the search key (title, in this case)
ahead of time. Repeatedly checks the middle element and discards the half of
the remaining range that can't contain the target.
- Requires sorted data.
- Time complexity: O(log n) worst/average case, O(1) best case.

## Files
- `Book.java` — data model (`bookId`, `title`, `author`)
- `LibrarySearch.java` — `linearSearchByTitle`, `binarySearchByTitle`, and a `sortedByTitle` helper
- `Main.java` — demo driver comparing both approaches on the same catalog

## Complexity comparison

| Algorithm     | Best Case | Average/Worst Case | Requires Sorted Data? |
|---------------|-----------|---------------------|------------------------|
| Linear Search | O(1)      | O(n)                 | No                     |
| Binary Search | O(1)      | O(log n)             | Yes                    |

## When to use each, based on data set size and order
- **Small catalogs, or catalogs that change constantly** (new arrivals,
  withdrawals): linear search is often good enough, and avoids the overhead
  of keeping the data sorted after every change.
- **Large, relatively stable catalogs** (e.g., a university library with tens
  of thousands of titles that doesn't reorganize every few minutes): sort
  once, then binary search repeatedly — the O(log n) payoff compounds with
  every search performed against the same sorted catalog.
- If searches need to happen by *both* title and author, a real system would
  typically maintain two separate sorted indexes (or use a proper database
  with indexes on both columns) rather than re-sorting the same array
  depending on which field is being searched.

## How to run
```bash
cd src
javac *.java
java Main
```
