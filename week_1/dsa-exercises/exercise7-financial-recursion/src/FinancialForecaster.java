import java.util.HashMap;
import java.util.Map;

/**
 * Predicts a future value by recursively applying a per-period growth rate to
 * a present value: futureValue(n) = presentValue * (1 + rate)^n.
 *
 * Recursion fits this problem naturally because the value after n periods is
 * defined in terms of the value after n-1 periods, mirroring the compounding
 * relationship directly instead of computing the power with a loop.
 */
public class FinancialForecaster {

    /**
     * Naive recursive future-value calculation.
     *
     * Time complexity: O(n) — one recursive call per remaining period, with no
     * repeated work, since each call corresponds to a unique period count.
     * (Note: although this particular recursion has no overlapping
     * subproblems, repeatedly *calling* forecast for many different,
     * unrelated period counts in the same run still re-does shared work
     * across calls — which is what the memoized version below avoids.)
     */
    public static double forecastFutureValue(double presentValue, double growthRate, int periods) {
        if (periods < 0) {
            throw new IllegalArgumentException("periods cannot be negative");
        }
        if (periods == 0) {
            return presentValue; // base case: no growth periods left
        }
        return forecastFutureValue(presentValue, growthRate, periods - 1) * (1 + growthRate);
    }

    /**
     * Memoized version: caches results keyed by period count so that
     * repeated forecasts (e.g., asking for periods = 5, then later periods = 5
     * again, or periods = 6 which reuses the result for periods = 5) avoid
     * recomputing work already done.
     *
     * Time complexity: O(n) the first time any given period count is computed,
     * O(1) for every subsequent lookup of an already-cached period count.
     */
    public static double forecastFutureValueMemoized(double presentValue, double growthRate,
                                                       int periods, Map<Integer, Double> cache) {
        if (periods < 0) {
            throw new IllegalArgumentException("periods cannot be negative");
        }
        if (periods == 0) {
            return presentValue;
        }
        if (cache.containsKey(periods)) {
            return cache.get(periods);
        }
        double result = forecastFutureValueMemoized(presentValue, growthRate, periods - 1, cache) * (1 + growthRate);
        cache.put(periods, result);
        return result;
    }

    public static Map<Integer, Double> newCache() {
        return new HashMap<>();
    }
}
