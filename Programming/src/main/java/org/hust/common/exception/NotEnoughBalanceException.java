package org.hust.common.exception;


/**
 * This exception is throws when receive response from interbank with the error
 * code correspond to not enough balance.
 */
public class NotEnoughBalanceException extends PaymentException {

    /**
     * Constructor for NotEnoughBalanceException.
     */
    public NotEnoughBalanceException() {
        super("ERROR: Not enough balance in card!");
    }

}
