package org.hust.common.exception;

/**
 * This exception is throws when user try to find a station at a location that have no station in there.
 */
public class InvalidLocationException extends Exception {
    @Override
    public String getMessage() {
        return "Nothing found in that location";
    }
}
