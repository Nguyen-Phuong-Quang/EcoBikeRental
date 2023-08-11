package org.hust.common.exception;


/**
 * This exception is throws when receive response from interbank with the error
 * code correspond to invalid card.
 */
public class InvalidCardException extends PaymentException {

    /**
     * Constructor for InvalidCardException.
     */
    public InvalidCardException() {
        super("ERROR: Invalid card!");
    }

}
