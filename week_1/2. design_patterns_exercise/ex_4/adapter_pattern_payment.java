// Exercise 4: Implementing the Adapter Pattern
// Scenario: A payment processing system that needs to integrate with
// multiple third-party payment gateways with different interfaces.

interface PaymentProcessor {
    void processPayment(double amount);
}

// Adaptee 1: Third-party gateway with its own incompatible interface
class PayPalGateway {
    public void sendPayment(double amountInDollars) {
        System.out.println("PayPalGateway: Sent payment of $" + amountInDollars + " via PayPal.");
    }
}

// Adaptee 2: Another third-party gateway with a different method signature
class StripeGateway {
    public void makeTransaction(double amountInDollars, String currency) {
        System.out.println("StripeGateway: Made transaction of " + amountInDollars + " " + currency + " via Stripe.");
    }
}

// Adapter for PayPal
class PayPalAdapter implements PaymentProcessor {
    private PayPalGateway payPalGateway;

    public PayPalAdapter(PayPalGateway payPalGateway) {
        this.payPalGateway = payPalGateway;
    }

    public void processPayment(double amount) {
        payPalGateway.sendPayment(amount);
    }
}

// Adapter for Stripe
class StripeAdapter implements PaymentProcessor {
    private StripeGateway stripeGateway;

    public StripeAdapter(StripeGateway stripeGateway) {
        this.stripeGateway = stripeGateway;
    }

    public void processPayment(double amount) {
        stripeGateway.makeTransaction(amount, "USD");
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== ADAPTER PATTERN: PAYMENT GATEWAYS ===");

        System.out.println("\nProcessing payment via PayPal:");
        PaymentProcessor payPalProcessor = new PayPalAdapter(new PayPalGateway());
        payPalProcessor.processPayment(150.75);

        System.out.println("\nProcessing payment via Stripe:");
        PaymentProcessor stripeProcessor = new StripeAdapter(new StripeGateway());
        stripeProcessor.processPayment(299.99);

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("Both gateways are used through the same PaymentProcessor");
        System.out.println("interface, even though their underlying APIs are different.");
    }
}
