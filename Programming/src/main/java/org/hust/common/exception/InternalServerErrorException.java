package org.hust.common.exception;


/**
 * This exception is throws when receive response from interbank with the error
 * code correspond to internal server error.
 */
public class InternalServerErrorException extends PaymentException {

    /**
     * Constructor for InternalServerErrorException.
     */
    public InternalServerErrorException() {
        super("ERROR: Internal Server Error!");
    }

}
