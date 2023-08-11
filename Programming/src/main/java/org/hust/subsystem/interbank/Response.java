package org.hust.subsystem.interbank;

import org.hust.common.exception.*;
import org.hust.entity.payment.CreditCard;
import org.hust.entity.payment.PaymentTransaction;

/**
 * This class represent a response received from the interbank.
 */
public class Response {

    private final String errorCode;
    private String cardCode;
    private String owner;
    private String cvvCode;
    private String command;
    private String dateExpired;
    private String transactionId;
    private String transactionContent;
    private int amount;
    private String createdAt;

    /**
     * Convert the response received from interbank
     * from String to Request.
     *
     * @param responseText - response from interbank in form of a String
     */
    public Response(String responseText) {
        errorCode = getValue("errorCode", responseText);
        if (errorCode.equals("00")) {
            cardCode = getValue("cardCode", responseText);
            owner = getValue("owner", responseText);
            cvvCode = getValue("cvvCode", responseText);
            command = getValue("command", responseText);
            dateExpired = getValue("dateExpired", responseText);
            transactionId = getValue("transactionId", responseText);
            transactionContent = getValue("transactionContent", responseText);
            try {
                amount = Integer.parseInt(getValue("amount", responseText));
            } catch (NumberFormatException e) {
                throw new UnrecognizedException();
            }
            createdAt = getValue("createdAt", responseText);
        }
    }

    private String getValue(String key, String responseText) {
        int index;
        if (!responseText.contains("\"" + key + "\"")) {
            throw new UnrecognizedException();
        }

        index = responseText.indexOf("\"" + key + "\"") + key.length() + 4;
        if (index >= responseText.length()) {
            throw new UnrecognizedException();
        }
        StringBuilder sb = new StringBuilder();
        char c;
        while ((c = responseText.charAt(index)) != '"') {
            if (index >= responseText.length()) {
                throw new UnrecognizedException();
            }
            sb.append(c);
            index++;
        }
        return sb.toString();
    }

    PaymentTransaction getPaymentTransaction() throws PaymentException, UnrecognizedException {
        switch (errorCode) {
            case "00":
                break;
            case "01":
                throw new InvalidCardException();
            case "02":
                throw new NotEnoughBalanceException();
            case "03":
                throw new InternalServerErrorException();
            case "04":
                throw new SuspiciousTransactionException();
            case "05":
                throw new NotEnoughTransactionInfoException();
            case "06":
                throw new InvalidVersionException();
            case "07":
                throw new InvalidTransactionAmountException();
            default:
                throw new UnrecognizedException();
        }

        CreditCard card = new CreditCard(cardCode, owner, cvvCode, dateExpired);
        return new PaymentTransaction(card, transactionId,
                transactionContent, command, amount, createdAt);
    }
}
