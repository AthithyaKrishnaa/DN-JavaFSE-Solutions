import java.util.Map;

/**
 * Demonstrates recursive future-value forecasting, with and without memoization.
 */
public class Main {

    public static void main(String[] args) {
        double presentValue = 10000.0; // starting investment
        double growthRate = 0.08;      // 8% growth per period (e.g., per year)

        System.out.println("Present value: " + presentValue + ", growth rate: " + (growthRate * 100) + "% per period");

        for (int periods = 1; periods <= 5; periods++) {
            double futureValue = FinancialForecaster.forecastFutureValue(presentValue, growthRate, periods);
            System.out.printf("Forecast after %d period(s): %.2f%n", periods, futureValue);
        }

        // Demonstrate the memoized version reusing cached subresults.
        System.out.println("\nUsing memoized forecasting (shared cache across calls):");
        Map<Integer, Double> cache = FinancialForecaster.newCache();
        for (int periods = 1; periods <= 5; periods++) {
            double futureValue = FinancialForecaster.forecastFutureValueMemoized(presentValue, growthRate, periods, cache);
            System.out.printf("Forecast after %d period(s): %.2f%n", periods, futureValue);
        }
        System.out.println("Cache now holds entries for periods: " + cache.keySet());
    }
}
