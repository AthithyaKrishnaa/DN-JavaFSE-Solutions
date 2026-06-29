// Exercise 6: Implementing the Proxy Pattern
// Scenario: An image viewer application that loads images from a remote
// server. Use the Proxy Pattern to add lazy initialization and caching.

interface Image {
    void display();
}

// Real subject: expensive to create (simulates loading from a remote server)
class RealImage implements Image {
    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromServer();
    }

    private void loadFromServer() {
        System.out.println("Loading \"" + fileName + "\" from remote server... (expensive operation)");
    }

    public void display() {
        System.out.println("Displaying \"" + fileName + "\"");
    }
}

// Proxy: adds lazy initialization and caching
class ProxyImage implements Image {
    private String fileName;
    private RealImage realImage;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    public void display() {
        if (realImage == null) {
            System.out.println("ProxyImage: No cached image found for \"" + fileName + "\".");
            realImage = new RealImage(fileName);
        } else {
            System.out.println("ProxyImage: Using cached image for \"" + fileName + "\".");
        }
        realImage.display();
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== PROXY PATTERN: IMAGE VIEWER ===");

        Image image = new ProxyImage("vacation_photo.png");

        System.out.println("\nFirst call to display():");
        image.display();

        System.out.println("\nSecond call to display() (should use cache):");
        image.display();

        System.out.println("\nThird call to display() (should use cache):");
        image.display();

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("The image is only loaded from the remote server once.");
        System.out.println("Subsequent calls reuse the cached RealImage instance.");
    }
}
