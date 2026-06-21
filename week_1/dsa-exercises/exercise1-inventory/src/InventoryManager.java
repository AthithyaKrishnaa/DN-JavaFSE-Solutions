import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages warehouse inventory using a HashMap keyed by productId.
 *
 * A HashMap is chosen over an ArrayList because lookups, insertions, and
 * deletions by productId run in O(1) average time, versus O(n) for a list
 * that must be scanned linearly to find a matching id.
 */
public class InventoryManager {

    private final Map<String, Product> inventory;

    public InventoryManager() {
        this.inventory = new HashMap<>();
    }

    /** Adds a product to the inventory. O(1) average case. */
    public void addProduct(Product product) {
        if (inventory.containsKey(product.getProductId())) {
            throw new IllegalArgumentException(
                    "Product with id " + product.getProductId() + " already exists.");
        }
        inventory.put(product.getProductId(), product);
    }

    /** Updates quantity and price for an existing product. O(1) average case. */
    public void updateProduct(String productId, int newQuantity, double newPrice) {
        Product product = inventory.get(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + productId + " not found.");
        }
        product.setQuantity(newQuantity);
        product.setPrice(newPrice);
    }

    /** Removes a product from the inventory. O(1) average case. */
    public boolean deleteProduct(String productId) {
        return inventory.remove(productId) != null;
    }

    /** Retrieves a product by id. O(1) average case. */
    public Product getProduct(String productId) {
        return inventory.get(productId);
    }

    /** Returns all products currently stored. */
    public Collection<Product> getAllProducts() {
        return inventory.values();
    }

    public int size() {
        return inventory.size();
    }
}
