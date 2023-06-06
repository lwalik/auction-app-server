package pl.wit;

import utills.UniqueIdGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStorage {
//    private UniqueIdGenerator generator =  new UniqueIdGenerator();
    private Map<Integer, Product> products =  new HashMap();

    public ProductStorage() {
        this.products.put(1, new Product(1, "Product 1", 100.00, 500.00, "/images/pobrane.png"));
        this.products.put(2, new Product(2,"Product 2", 85.00, 170.00, "/images/apple-iphone-xs.jpg"));
        this.products.put(3, new Product(3,"Product 3", 15.00, 45.00,  "/images/apple-iphone-xs.jpg"));
    }


    public Map<Integer,Product> getAll() {
        return this.products;
    }

    public Map<Integer,Product> removeProduct(int id) {
        this.products.remove(id);
        return products;
    }
}
