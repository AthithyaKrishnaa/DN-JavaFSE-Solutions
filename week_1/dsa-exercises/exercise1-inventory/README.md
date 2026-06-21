# Exercise 1: Inventory Management System

## Scenario
A warehouse inventory management system needs efficient storage and retrieval
of product records.

## Why data structures/algorithms matter here
A warehouse can hold thousands of distinct products, and operations like
"look up this product," "update its stock count," or "remove it" happen
constantly. The wrong data structure turns every one of those into a slow
linear scan; the right one turns most of them into constant-time operations.
Picking a structure up front, instead of bolting one on later, is what keeps
the system responsive as inventory grows.

## Data structure choice: HashMap over ArrayList
- **ArrayList**: finding a product by ID means scanning the list — O(n).
- **HashMap (keyed by `productId`)**: lookup, insertion, and deletion are all
  O(1) on average, because the key hashes directly to a bucket instead of
  being searched for.

Since every operation in this exercise (add, update, delete, lookup) is keyed
by `productId`, a HashMap is the clear choice.

## Files
- `Product.java` — the data model (`productId`, `productName`, `quantity`, `price`)
- `InventoryManager.java` — wraps a `HashMap<String, Product>` with add/update/delete/get
- `Main.java` — demo driver exercising all operations

## Complexity summary

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| Add       | O(1) average    | Direct hash insertion |
| Update    | O(1) average    | Direct hash lookup, then field mutation |
| Delete    | O(1) average    | Direct hash removal |
| Get/Lookup| O(1) average    | Direct hash lookup |

Worst case for all HashMap operations is O(n) if many keys collide into the
same bucket, but Java's `HashMap` resizes and rehashes to keep this rare in
practice.

## Optimization notes
- Pre-sizing the `HashMap` (via the constructor's initial-capacity parameter)
  avoids repeated rehashing if the approximate inventory size is known ahead
  of time.
- If range queries (e.g., "all products under $20") become common, a
  secondary index — such as a `TreeMap` sorted by price — could be added
  alongside the HashMap, trading some memory for faster range lookups.

## How to run
```bash
cd src
javac *.java
java Main
```
