package org.hust.subsystem;

import org.hust.common.exception.PaymentException;
import org.hust.common.exception.UnrecognizedException;
import org.hust.entity.payment.CreditCard;
import org.hust.entity.payment.PaymentTransaction;
import org.hust.subsystem.interbank.InterbankSubsystemControl;

/**
 * This class is used to communicate with the
 * interbank to make transaction.
 */
public class InterbankSubsystem implements InterbankInterface {

    private final InterbankSubsystemControl ctrl;

    /**
     * Constructor for InterbankSubsystem.
     */
    public InterbankSubsystem() {
        this.ctrl = new InterbankSubsystemControl();
    }

    @Override
    public PaymentTransaction makeTransaction(CreditCard card, int amount, String contents)
            throws PaymentException, UnrecognizedException {
        return ctrl.makeTransaction(card, amount, contents);
    }
}
