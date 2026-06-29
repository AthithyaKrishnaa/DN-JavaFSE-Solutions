// Exercise 8: Implementing the Strategy Pattern
// Scenario: A payment system where different payment methods (Credit Card,
// PayPal) can be selected at runtime.

interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card ending in " +
                cardNumber.substring(cardNumber.length() - 4));
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal account: " + email);
    }
}

class PaymentContext {
    private PaymentStrategy strategy;

    public PaymentContext(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executePayment(double amount) {
        strategy.pay(amount);
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== STRATEGY PATTERN: PAYMENT METHODS ===");

        PaymentContext context = new PaymentContext(new CreditCardPayment("4111111111111234"));
        System.out.println("\nCheckout #1 (Credit Card):");
        context.executePayment(250.00);

        context.setStrategy(new PayPalPayment("buyer@example.com"));
        System.out.println("\nCheckout #2 (PayPal, strategy switched at runtime):");
        context.executePayment(75.50);

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("The PaymentContext delegates payment processing to whichever");
        System.out.println("PaymentStrategy is selected, without changing its own code.");
    }
}
