package pl.wit;

import javax.swing.*;
import java.io.Serializable;
import java.net.URL;

public class Product implements Serializable {
    private final String name;
    private final double buyNowPrice;
    private final double currPrice;
    private String currBuyer;
    private final int id;
    private static final long serialVersionUID = -1L;
    private final String imagePath;

    public Product(int id, String name, double currPrice, double buyNowPrice, String imagePath) {
        this.id = id;
        this.name = name;
        this.currPrice = currPrice;
        this.buyNowPrice = buyNowPrice;
        this.currBuyer = "";
        this.imagePath = imagePath;
    }

    public double getCurrPrice() {
        return this.currPrice;
    }

    public int getId() {
        return this.id;
    }

    public double getBuyNowPrice() {
        return this.buyNowPrice;
    }

    public void setCurrBuyer(String currBuyer) {
        this.currBuyer = currBuyer;
    }
}
