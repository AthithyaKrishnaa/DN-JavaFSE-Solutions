# Algorithms & Data Structures — Exercises

Solutions to 7 applied exercises covering core data structures and
algorithms, each framed around a realistic software scenario (inventory
systems, e-commerce search, sorting, employee records, task management,
library catalogs, and financial forecasting).

Each exercise is a small, self-contained Java project with its own source
files, a demo `Main` class, and a `README.md` explaining the concepts,
design choices, and complexity analysis.

## Structure

```
.
├── exercise1-inventory/          HashMap-based inventory management
├── exercise2-ecommerce-search/   Linear vs. binary search
├── exercise3-sorting-orders/     Bubble Sort vs. Quick Sort
├── exercise4-employee-array/     Array-based record management
├── exercise5-task-linkedlist/    Singly linked list
├── exercise6-library-search/     Linear vs. binary search (catalog lookup)
└── exercise7-financial-recursion/  Recursive (and memoized) forecasting
```

| # | Exercise | Core Concept |
|---|----------|---------------|
| 1 | [Inventory Management System](exercise1-inventory/) | HashMap for O(1) CRUD by key |
| 2 | [E-commerce Search Function](exercise2-ecommerce-search/) | Big O, linear vs. binary search |
| 3 | [Sorting Customer Orders](exercise3-sorting-orders/) | Bubble Sort vs. Quick Sort |
| 4 | [Employee Management System](exercise4-employee-array/) | Array representation, manual resizing |
| 5 | [Task Management System](exercise5-task-linkedlist/) | Singly linked list |
| 6 | [Library Management System](exercise6-library-search/) | Linear vs. binary search (by title) |
| 7 | [Financial Forecasting](exercise7-financial-recursion/) | Recursion and memoization |

## Requirements
- JDK 11 or later (developed and tested against OpenJDK 21)

## Running any exercise
Each exercise folder is independent:

```bash
cd exercise1-inventory/src
javac *.java
java Main
```

Repeat with the relevant folder name for any other exercise (e.g.
`exercise5-task-linkedlist/src`).

## Notes
- Every exercise was compiled and run to confirm correct output before being
  committed — see each exercise's `README.md` for the specific
  complexity analysis and design rationale.
- These are intentionally written as plain Java (no external dependencies,
  no build tool) so each exercise can be read, compiled, and run in
  isolation without any project setup.
