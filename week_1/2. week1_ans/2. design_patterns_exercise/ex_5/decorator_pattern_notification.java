// Exercise 5: Implementing the Decorator Pattern
// Scenario: A notification system where notifications can be sent via
// multiple channels (Email, SMS, Slack) using the Decorator Pattern.

interface Notifier {
    void send(String message);
}

// Concrete component
class EmailNotifier implements Notifier {
    public void send(String message) {
        System.out.println("Sending EMAIL notification: " + message);
    }
}

// Abstract decorator
abstract class NotifierDecorator implements Notifier {
    protected Notifier wrappedNotifier;

    public NotifierDecorator(Notifier notifier) {
        this.wrappedNotifier = notifier;
    }

    public void send(String message) {
        wrappedNotifier.send(message);
    }
}

// Concrete decorator: adds SMS notification
class SMSNotifierDecorator extends NotifierDecorator {
    public SMSNotifierDecorator(Notifier notifier) {
        super(notifier);
    }

    public void send(String message) {
        super.send(message);
        System.out.println("Sending SMS notification: " + message);
    }
}

// Concrete decorator: adds Slack notification
class SlackNotifierDecorator extends NotifierDecorator {
    public SlackNotifierDecorator(Notifier notifier) {
        super(notifier);
    }

    public void send(String message) {
        super.send(message);
        System.out.println("Sending SLACK notification: " + message);
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== DECORATOR PATTERN: NOTIFICATIONS ===");

        System.out.println("\nSending via Email only:");
        Notifier emailOnly = new EmailNotifier();
        emailOnly.send("Order #1001 has shipped.");

        System.out.println("\nSending via Email + SMS:");
        Notifier emailAndSms = new SMSNotifierDecorator(new EmailNotifier());
        emailAndSms.send("Order #1002 has shipped.");

        System.out.println("\nSending via Email + SMS + Slack:");
        Notifier allChannels = new SlackNotifierDecorator(
                new SMSNotifierDecorator(new EmailNotifier()));
        allChannels.send("Order #1003 has shipped.");

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("Decorators dynamically add new notification channels");
        System.out.println("without modifying the original EmailNotifier class.");
    }
}
