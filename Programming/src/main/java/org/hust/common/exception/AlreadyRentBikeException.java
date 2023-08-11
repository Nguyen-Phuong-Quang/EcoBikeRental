package org.hust.common.exception;

/**
 * This exception is throws when user try to rent bike when they have already rent a bike.
 */
public class AlreadyRentBikeException extends Exception {

    /**
     * Constructor for AlreadyRentBikeException.
     */
    /**
     * Constructor for AlreadyRentBikeException.
     *
     * @param msg message to be throws
     */
    public AlreadyRentBikeException(String msg) {
        super("ERROR: " + msg + "!");
    }

}