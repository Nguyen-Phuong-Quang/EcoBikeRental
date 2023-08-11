package org.hust.common.exception;

/**
 * This exception is throws when invalid id is used in code.
 */
public class InvalidIdException extends Exception {
    @Override
    public String getMessage() {
        return ("ID not found");
    }
}
