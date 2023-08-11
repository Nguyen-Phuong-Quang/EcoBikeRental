package org.hust.subsystem.interbank;

import org.hust.entity.payment.CreditCard;
import org.hust.entity.payment.PaymentTransaction;
import org.hust.utils.Utils;

/**
 * This class represent a payment transaction request
 * to be sent to the interbank.
 */
public class Request {

    private final String appCode = "Bwl7uRdfqRk=";
    private final String secretKey = "Bu4+3vWsffl=";
    private final String hashCode;
    private final String path = "/api/card/processTransaction";
    private final String version = "1.0.1";
    private final PaymentTransaction transaction;

    /**
     * Initialize Request object.
     *
     * @param card     the credit card to be used in the transaction
     * @param contents transaction contents
     * @param amount   transaction amount
     */
    public Request(CreditCard card, String contents, int amount) {
        transaction = new PaymentTransaction(card, contents, amount);
        StringBuilder sb = new StringBuilder();
        sb.append("{\"secretKey\":");
        sb.append("\"" + secretKey + "\",");
        sb.append(transaction.toDataForHashCode());
        sb.append("}");
        hashCode = Utils.md5(sb.toString());
    }

    /**
     * Convert the request into JSON format
     * which is the format needed to be sent to interbank.
     *
     * @return String represent the Request in JSON format
     */
    public String toData() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"version\"" + ":\"" + version + "\",");
        sb.append(transaction.toData());
        sb.append(",");
        sb.append("\"appCode\"" + ":\"" + appCode + "\",");
        sb.append("\"hashCode\"" + ":\"" + hashCode + "\"}");
        return sb.toString();
    }

    /**
     * Get the path to send the request.
     *
     * @return path to send the request
     */
    public String getPath() {
        return this.path;
    }
}
