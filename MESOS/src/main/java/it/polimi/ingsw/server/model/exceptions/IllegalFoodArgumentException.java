package it.polimi.ingsw.server.model.exceptions;

public class IllegalFoodArgumentException extends RuntimeException {
    public IllegalFoodArgumentException(String message) {
        super(message);
    }
}
