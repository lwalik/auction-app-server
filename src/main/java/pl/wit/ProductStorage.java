package pl.wit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class ProductStorage {
    private Map<Integer, Product> products = new HashMap();
    private final HashSet<Integer> ids = new HashSet<>();

    public ProductStorage() {
        this.products.put(1, new Product(generateUniqueId(), "Product 1", 100.00, 500.00, "/images/pobrane.png"));
        this.products.put(2, new Product(generateUniqueId(), "Product 2", 85.00, 170.00, "/images/apple-iphone-xs.jpg"));
        this.products.put(3, new Product(generateUniqueId(), "Product 3", 15.00, 45.00, "/images/apple-iphone-xs.jpg"));
    }


    public Map<Integer, Product> getAll() {
        return this.products;
    }

    public Map<Integer, Product> removeProduct(int id) {
        this.products.remove(id);
        return products;
    }

    public void updateData(Product givenProduct) {
        Map<Integer, Product> newProducts = new HashMap<>();
        for (Map.Entry<Integer, Product> entry : products.entrySet()) {
            Integer key = entry.getKey();
            Product product = entry.getValue();

            System.out.println("1. " + "name: " + product.getName() + " id: " + product.getId() + " currPrice: " + product.getCurrPrice() + " buyNowPrice: " + product.getBuyNowPrice());
            System.out.println("2. " + "name: " + givenProduct.getName() + " id: " + givenProduct.getId() + " currPrice: " + givenProduct.getCurrPrice() + " buyNowPrice: " + givenProduct.getBuyNowPrice());

            if (product.getId() == givenProduct.getId() && givenProduct.getCurrPrice() <= product.getBuyNowPrice()) {
                System.out.println("Nowy wjeżdża");
                newProducts.put(key, givenProduct);
            } else {
                newProducts.put(key, product);
                System.out.println("Został stary: " + product.getName());
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
