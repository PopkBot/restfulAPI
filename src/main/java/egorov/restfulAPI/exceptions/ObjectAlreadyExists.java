package egorov.restfulAPI.exceptions;

public class ObjectAlreadyExists extends RuntimeException {
    public ObjectAlreadyExists(String message) {
        super(message);
    }
}
