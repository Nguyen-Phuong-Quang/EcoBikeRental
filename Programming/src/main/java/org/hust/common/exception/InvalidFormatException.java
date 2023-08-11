package org.hust.common.exception;


/**
 * This exception is throws when detect invalid format in user's input.
 */
public class InvalidFormatException extends Exception {

    /**
     * Constructor for InvalidFormatException.
     *
     * @param msg message to be throws
     */
    public InvalidFormatException(String msg) {
        super(msg);
    }

}
