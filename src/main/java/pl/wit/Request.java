package pl.wit;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = -1L;

    private final String method;
    private final Product product;
    private final String message;

    public Request(String method, Product product, String message) {
        this.method = method;
        this.product = product;
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public Product getProduct() {
        return product;
    }

    public String getMessage() {
        return message;
    }
}
