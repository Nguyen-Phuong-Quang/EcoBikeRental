package org.hust.common.exception;


/**
 * This exception is throws when receive response from interbank with the error
 * code correspond to invalid version.
 */
public class InvalidVersionException extends PaymentException {

    /**
     * Constructor for InvalidVersionException.
     */
    public InvalidVersionException() {
        super("ERROR: Invalid Version Information!");
    }

}
