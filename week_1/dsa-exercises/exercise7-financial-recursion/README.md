# Exercise 7: Financial Forecasting (Recursion)

## Scenario
A financial forecasting tool predicts future values based on past growth
rates, using a recursive approach.

## Recursion, briefly
Recursion solves a problem by expressing it in terms of a smaller instance of
the same problem, plus a **base case** that stops the recursion. Here, the
value after `n` periods of compounding growth is naturally defined in terms
of the value after `n - 1` periods:

```
futureValue(n) = futureValue(n - 1) * (1 + growthRate)
futureValue(0) = presentValue   <- base case
```

This mirrors how compounding actually works (each period's value builds on
the last), which is why recursion is a natural fit — the recursive
definition *is* the financial definition, rather than something forced into
a recursive shape.

## Files
- `FinancialForecaster.java` — `forecastFutureValue` (plain recursion) and `forecastFutureValueMemoized` (cached recursion)
- `Main.java` — demo driver forecasting 1–5 periods ahead with both versions

## Complexity

| Version           | Time Complexity | Space Complexity | Notes |
|--------------------|------------------|--------------------|-------|
| Plain recursion    | O(n)             | O(n) call stack    | One call per period, no repeated subproblems within a single call |
| Memoized recursion | O(n) first call per period count, O(1) for repeats | O(n) cache + O(n) call stack | Avoids redoing work across *separate* forecast requests |

Within a single call, `forecastFutureValue(p, r, n)` doesn't recompute
anything — there's exactly one call per period from `n` down to `0`. The
memoization pays off across *multiple* calls: if the tool is later asked to
forecast periods 1 through 5 (as the demo does), each call's result is cached,
so asking for period 6 next reuses all of that prior work instead of
recursing all the way back down to 0 again.

## Optimizing the recursive solution
- **Memoization** (implemented here): cache results by period count in a
  `Map<Integer, Double>` so repeated or incremental forecast requests reuse
  previous work instead of recomputing it.
- **Closed-form formula**: since this is simple compound growth, the entire
  recursion can be replaced with `presentValue * Math.pow(1 + growthRate, periods)`,
  which runs in O(log n) (due to how `Math.pow` computes powers) and avoids
  recursion/call-stack growth entirely. The recursive version is kept here
  because the exercise specifically asks for a recursive approach, but a real
  production system would likely just use the closed-form formula for this
  particular calculation.
- **Tail-call style iteration**: the recursion could be rewritten as a simple
  loop accumulating the result, trading the elegance of recursion for O(1)
  stack space — useful if `periods` could be very large and a `StackOverflowError`
  becomes a concern.

## How to run
```bash
cd src
javac *.java
java Main
```
