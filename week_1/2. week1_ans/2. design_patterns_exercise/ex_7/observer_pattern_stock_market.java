// Exercise 7: Implementing the Observer Pattern
// Scenario: A stock market monitoring application where multiple clients
// need to be notified whenever stock prices change.

import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String stockSymbol, double price);
}

interface Stock {
    void registerObserver(Observer observer);
    void deregisterObserver(Observer observer);
    void notifyObservers();
}

class StockMarket implements Stock {
    private List<Observer> observers = new ArrayList<>();
    private String stockSymbol;
    private double price;

    public StockMarket(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
        System.out.println(observer.getClass().getSimpleName() + " registered for " + stockSymbol + " updates.");
    }

    public void deregisterObserver(Observer observer) {
        observers.remove(observer);
        System.out.println(observer.getClass().getSimpleName() + " deregistered from " + stockSymbol + " updates.");
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(stockSymbol, price);
        }
    }

    public void setPrice(double price) {
        this.price = price;
        System.out.println("\n" + stockSymbol + " price changed to $" + price);
        notifyObservers();
    }
}

class MobileApp implements Observer {
    public void update(String stockSymbol, double price) {
        System.out.println("MobileApp Notification: " + stockSymbol + " is now $" + price);
    }
}

class WebApp implements Observer {
    public void update(String stockSymbol, double price) {
        System.out.println("WebApp Notification: " + stockSymbol + " is now $" + price);
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== OBSERVER PATTERN: STOCK MARKET ===");

        StockMarket appleStock = new StockMarket("AAPL");

        Observer mobileApp = new MobileApp();
        Observer webApp = new WebApp();

        appleStock.registerObserver(mobileApp);
        appleStock.registerObserver(webApp);

        appleStock.setPrice(189.50);
        appleStock.setPrice(192.10);

        System.out.println();
        appleStock.deregisterObserver(webApp);

        appleStock.setPrice(195.75);

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("All registered observers are automatically notified");
        System.out.println("whenever the stock price changes.");
    }
}
