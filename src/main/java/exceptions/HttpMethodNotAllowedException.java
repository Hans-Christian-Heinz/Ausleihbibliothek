package exceptions;

public class HttpMethodNotAllowedException extends Exception {
    public enum Methods {
        POST,
        DELETE,
        PATCH
    }

    public HttpMethodNotAllowedException(Methods method) {
        super("Die " + method + "-Methode ist nicht erlaubt.");
    }
}
