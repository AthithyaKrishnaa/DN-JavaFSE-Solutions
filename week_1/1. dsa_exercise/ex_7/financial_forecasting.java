class FinancialForecast {

    // Recursive function to calculate future value
    public static double forecast(double currentValue, double growthRate, int years) {

        System.out.println("Year: " + years + " | Current Value: " + currentValue);

        // Base case
        if (years == 0) {
            return currentValue;
        }

        // Recursive case
        return forecast(currentValue * (1 + growthRate), growthRate, years - 1);
    }

    public static void main(String[] args) {

        double initialValue = 10000;   // starting money
        double growthRate = 0.10;     // 10% growth per year
        int years = 5;

        System.out.println("=== FINANCIAL FORECAST USING RECURSION ===\n");

        double result = forecast(initialValue, growthRate, years);

        System.out.println("\nFinal Predicted Value after " + years + " years: " + result);

        // TIME COMPLEXITY
        System.out.println("\n=== TIME COMPLEXITY ANALYSIS ===");
        System.out.println("Each recursive call reduces 'years' by 1");
        System.out.println("Total calls = n");
        System.out.println("Time Complexity: O(n)");

        // SPACE COMPLEXITY
        System.out.println("Space Complexity (recursion stack): O(n)");

        // OPTIMIZATION
        System.out.println("\n=== OPTIMIZATION ===");
        System.out.println("- Recursion can cause repeated calculations in complex models");
        System.out.println("- Use Memoization (store results) to avoid recomputation");
        System.out.println("- Or convert recursion into iterative loop for better performance");

        System.out.println("\nOptimized approach idea:");
        System.out.println("Use loop instead of recursion:");
        System.out.println("value = value * (1 + growthRate) repeatedly");
    }
}