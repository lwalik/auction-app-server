package pl.wit;

import java.io.Serializable;
import java.util.Map;

public class Response implements Serializable {
    private static final long serialVersionUID = -1L;
    private final StatusCode statusCode;
    private final Map<Integer, Product> products;
    private String userName = "";

    public Response(StatusCode statusCode, Map<Integer, Product> products) {
        this.statusCode = statusCode;
        this.products = products;
    }

    public void setUserName(String name) {
        this.userName = name;
    }
}
