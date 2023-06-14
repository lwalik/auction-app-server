package pl.wit;

public enum StatusCode {
    OK(200, "OK"),
    UPDATED(205, "Updated");

    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
}
