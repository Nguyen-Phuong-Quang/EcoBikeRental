package org.hust.entity.payment;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hust.entity.db.Database;
import org.hust.utils.Utils;

import java.lang.reflect.Field;

/**
 * This class represent a payment transaction.
 */
public class PaymentTransaction {

    private final CreditCard card;
    private final String command;
    private final String transactionContent;
    private final int amount;
    private final String createdAt;
    private String transactionId;

    /**
     * Initialize a PaymentTransaction object using credit card, content, amount.
     *
     * @param card    the credit card used in the payment transaction
     * @param content the transaction content
     * @param amount  the transaction amount
     */
    public PaymentTransaction(CreditCard card, String content, int amount) {
        this.card = card;
        this.transactionContent = content;
        if (amount < 0) {
            this.command = "refund";
            this.amount = -amount;
        } else {
            this.command = "pay";
            this.amount = amount;
        }
        this.createdAt = Utils.getToday();
    }

    /**
     * Initialize a PaymentTransaction object using credit card,
     * id, content, command, amount, created time.
     *
     * @param card               the credit card used in the payment transaction
     * @param transactionId      the trasaction id
     * @param transactionContent the transaction content
     * @param command            the transaction command
     * @param amount             the transaction amount
     * @param createdAt          the transaction created time
     */
    public PaymentTransaction(CreditCard card, String transactionId, String transactionContent,
                              String command, int amount, String createdAt) {
        this.card = card;
        this.transactionId = transactionId;
        this.transactionContent = transactionContent;
        this.command = command;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    /**
     * Save the information of the payment transaction into the database.
     */
    public void save() {
        MongoDatabase db = Database.getConnection();
        Document creditCard = new Document("_id", new ObjectId());
        Document dbCreditCard = new Document();
        boolean isExist = false;
        for (Field field : card.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(card);
                if (field.getName() == "cardCode") {
                    dbCreditCard = db.getCollection("credit_cards").find(new Document("cardCode", value.toString())).first();
                    if (dbCreditCard != null) {
                        isExist = true;
                        break;
                    }
                }
                creditCard.append(field.getName(), value.toString());
                field.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Document paymentTransaction = new Document("_id", new ObjectId());
        paymentTransaction.append("createAt", this.createdAt);
        paymentTransaction.append("amount", this.amount);
        if (!isExist) {
            db.getCollection("credit_cards").insertOne(creditCard);
            paymentTransaction.append("creditCard", creditCard);
        } else {
            paymentTransaction.append("creditCard", dbCreditCard);
        }
        paymentTransaction.append("method", this.command);
        paymentTransaction.append("content", this.transactionContent);
        db.getCollection("payment_transactions").insertOne(paymentTransaction);
    }

    /**
     * Convert part of transaction info into a JSON format in order to create request's hash code.
     *
     * @return String represent the payment transaction info in JSON format
     */
    public String toDataForHashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"transaction\":{");
        try {
            sb.append("\"command\":\"" + command + "\",");
            for (Field field : card.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(card);
                sb.append('"' + field.getName() + '"' + ':' + '"' + value.toString() + '"' + ',');
                field.setAccessible(false);
            }
            sb.append("\"transactionContent\":\"" + transactionContent + "\",");
            sb.append("\"amount\":\"" + amount + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert all transaction info into a JSON format in order to create request's hash code.
     *
     * @return String represent the payment transaction info in JSON format
     */
    public String toData() {
        StringBuilder sb = new StringBuilder();
        sb.append(toDataForHashCode());
        sb.deleteCharAt(sb.length() - 1);
        sb.append(",\"createdAt\":\"" + createdAt + "\"}");
        return sb.toString();
    }

    /**
     * Return the transaction id.
     *
     * @return id of the transaction
     */
    public String getId() {
        return transactionId;
    }

    /**
     * Return the transaction amount.
     *
     * @return transaction amount
     */
    public int getAmount() { return amount; }

    /**
     * Return the owner of the credit card used in the payment transaction.
     *
     * @return owner of the credit card
     */
    public String getOwner() { return card.getOwner(); }

    /**
     * Return the time that this payment transaction was made.
     *
     * @return the created time of the transaction
     */
    public String getTime() {
        return createdAt;
    }

    /**
     * Return the contents of the transaction.
     *
     * @return the transaction contents
     */
    public String getContent() {
        return transactionContent;
    }

    /**
     * Return the command of the transaction.
     *
     * @return the transaction command
     */
    public String getCommand() { return command; }

    @Override
    public String toString() {
        return String.format("Transaction ID: %s\nCard Holder: %s\nAmount: %s\nDescription: %s\nTransaction Time: %s",
                transactionId,
                card.getOwner(),
                amount,
                transactionContent,
                createdAt);
    }
}
