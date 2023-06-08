package pl.wit;

import java.io.Serializable;
import java.util.Map;

public class Response implements Serializable {
    private static final long serialVersionUID = -1L;
    private final int statusCode;
    private final Map<Integer, Product> products;

    public Response(int statusCode, Map<Integer, Product> products) {
        this.statusCode = statusCode;
        this.products = products;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }
}
