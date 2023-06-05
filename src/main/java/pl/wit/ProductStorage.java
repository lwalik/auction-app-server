package pl.wit;

import utills.UniqueIdGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductStorage {
    private UniqueIdGenerator generator =  new UniqueIdGenerator();
    private Map<Integer, Product> products =  new HashMap();

    public ProductStorage() {
        this.products.put(generator.generateId(), new Product(generator.generateId(), "Product 1", 100.00, 500.00, "/images/pobrane.png"));
        this.products.put( generator.generateId(), new Product(generator.generateId(),"Product 2", 85.00, 170.00, "/images/apple-iphone-xs.jpg"));
        this.products.put(generator.generateId(), new Product(generator.generateId(),"Product 3", 15.00, 45.00,  "/images/apple-iphone-xs.jpg"));
    }


    public Map<Integer,Product> getAll() {
        return this.products;
    }

    public Map<Integer,Product> removeProduct(int id) {
        this.products.remove(id);
        return products;
    }
}
