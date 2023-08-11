package org.hust.common.exception;

/**
 * This exception is throws when user try to view current rented or return bike when they are not renting any.
 */
public class HaveNotRentBikeException extends Exception {
    /**
     * Constructor for HaveNotRentBikeException.
     */
    public HaveNotRentBikeException() {
    }

    /**
     * Constructor for HaveNotRentBikeException.
     *
     * @param message message to be throws
     */
    public HaveNotRentBikeException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return ("You haven't rented any bike! Don't hesitate to rent one!");
    }
}
