package org.hust.common.exception;

/**
 * This exception is throws when user input station's location in a wrong format.
 */
public class InvalidLocationFormatException extends Exception {
    @Override
    public String getMessage() {
        return "String include invalid special characters";
    }
}
