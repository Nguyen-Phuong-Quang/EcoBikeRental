package org.hust.common.exception;

/**
 * This exception is throws when receive response from interbank with the error
 * code correspond to suspicious transaction.
 */
public class SuspiciousTransactionException extends PaymentException {

    /**
     * Constructor for SuspiciousTransactionException.
     */
    public SuspiciousTransactionException() {
        super("ERROR: Suspicious Transaction Report!");
    }

}
