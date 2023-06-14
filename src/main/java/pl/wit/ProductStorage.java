package pl.wit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class ProductStorage {
    private Map<Integer, Product> products = new HashMap<>();
    private final HashSet<Integer> ids = new HashSet<>();

    public ProductStorage() {
        this.products.put(1, new Product(generateUniqueId(), "Logitech MX Keys Mini", 0, 560, "sadasda"));
        this.products.put(2, new Product(generateUniqueId(), "Logitech Master 3s", 0, 400, "/images/mx-master-3s.jpg"));
        this.products.put(3, new Product(generateUniqueId(), "Steelseries Arctis 9", 0, 620, "/images/steelseries-arctis-9.png"));
        this.products.put(4, new Product(generateUniqueId(), "Steelseries QCK", 0, 135, "/images/steelseries-qck.png"));
    }


    public Map<Integer, Product> getAll() {
        return this.products;
    }

    public void updateData(Product givenProduct) {
        Map<Integer, Product> newProducts = new HashMap<>();
        for (Map.Entry<Integer, Product> entry : products.entrySet()) {
            Integer key = entry.getKey();
            Product product = entry.getValue();

            if (product.getId() == givenProduct.getId() && givenProduct.getCurrPrice() <= product.getBuyNowPrice()) {
                newProducts.put(key, givenProduct);
            } else {
                newProducts.put(key, product);
            }

        }

        this.products = newProducts;
    }

    private Integer generateUniqueId() {
        Random random = new Random();
        Integer id;
        do {
            id = random.nextInt(900) + 100;
        } while (this.ids.contains(id));
        ids.add(id);

        return id;
    }
}
