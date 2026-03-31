package it.polimi.ingsw.model.exceptions;

public class IllegalFoodArgumentException extends RuntimeException {
    public IllegalFoodArgumentException(String message) {
        super(message);
    }
}
