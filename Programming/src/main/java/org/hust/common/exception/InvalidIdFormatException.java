package org.hust.common.exception;

/**
 * This exception is throws when user input id in the wrong format.
 */
public class InvalidIdFormatException extends Exception {
    @Override
    public String getMessage() {
        return "ID contain special characters";
    }
}
