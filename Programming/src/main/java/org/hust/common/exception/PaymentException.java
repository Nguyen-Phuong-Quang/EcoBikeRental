package org.hust.common.exception;


/**
 * This exception is throws when receive error when making payment transaction
 * with known reasons.
 */
public class PaymentException extends Exception {

    /**
     * Constructor of the PaymentException.
     *
     * @param message message to be throws
     */
    public PaymentException(String message) {
        super(message);
    }

}
