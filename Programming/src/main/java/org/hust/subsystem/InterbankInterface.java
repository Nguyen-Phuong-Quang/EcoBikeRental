package org.hust.subsystem;

import org.hust.common.exception.PaymentException;
import org.hust.common.exception.UnrecognizedException;
import org.hust.entity.payment.CreditCard;
import org.hust.entity.payment.PaymentTransaction;

/**
 * This class is used to communicate with the interbank
 * in order to make payment transaction.
 */
public interface InterbankInterface {

    /**
     * Make payment transaction.
     *
     * @param card     the credit card used for payment transaction
     * @param amount   the transaction amount
     * @param contents the transaction contents
     * @return {@link org.hust.entity.payment.PaymentTransaction PaymentTransaction} if the payment is successful
     * @throws PaymentException      - if payment fail with known reasons
     * @throws UnrecognizedException - if payment fail with unknown reasons
     */
    PaymentTransaction makeTransaction(CreditCard card, int amount, String contents)
            throws PaymentException, UnrecognizedException;
}
