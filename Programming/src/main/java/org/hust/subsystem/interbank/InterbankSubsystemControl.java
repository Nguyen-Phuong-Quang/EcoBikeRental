package org.hust.subsystem.interbank;

import org.hust.common.exception.PaymentException;
import org.hust.common.exception.UnrecognizedException;
import org.hust.entity.payment.CreditCard;
import org.hust.entity.payment.PaymentTransaction;

/**
 * This class is used to control the data flow in interbank subsystem.
 */
public class InterbankSubsystemControl {

    private static final InterbankBoundary interbankBoundary = new InterbankBoundary();

    /**
     * Make payment transaction.
     *
     * @param card     the credit card used for payment transaction
     * @param amount   the transaction amount
     * @param contents the transaction contents
     * @return {@link org.hust.entity.payment.PaymentTransaction PaymentTransaction} if the payment is successful
     * @throws PaymentException      if payment fail with known reasons
     * @throws UnrecognizedException if payment fail with unknown reasons
     */
    public PaymentTransaction makeTransaction(CreditCard card, int amount, String contents)
            throws UnrecognizedException, PaymentException {
        Request request = new Request(card, contents, amount);

        Response response = interbankBoundary.requestToMakeTransaction(request);
        return response.getPaymentTransaction();
    }

}
