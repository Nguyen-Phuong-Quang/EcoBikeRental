package org.hust.entity.invoice;

import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hust.entity.db.Database;
import org.hust.entity.payment.PaymentTransaction;

import java.util.List;

/**
 * This class represent an invoice and hold the information of an invoice.
 */
@Getter
@NoArgsConstructor
public class Invoice {
    private PaymentTransaction transaction;
    private int deposit;
    private int refund;
    private int fee;
    private int totalCharge;
    private List<String> bikeIds;

    /**
     * Initialize an Invoice object.
     *
     * @param transaction the transaction made in the invoice
     * @param deposit the deposit made in the invoice
     * @param refund the amount of money refunded in the invoice
     * @param fee the fee of the invoice
     * @param totalCharge the total charge of the invoice
     * @param bikeIds list of bike used in the invoice
     */
    public Invoice(PaymentTransaction transaction, int deposit, int refund, int fee, int totalCharge, List<String> bikeIds) {
        this.transaction = transaction;
        this.deposit = deposit;
        this.refund = refund;
        this.fee = fee;
        this.totalCharge = totalCharge;
        this.bikeIds = bikeIds;
    }

    /**
     * Save the information of the invoice into the database.
     */
    public void save() {
        MongoDatabase db = Database.getConnection();
        Document invoiceDoc = new Document("_id", new ObjectId());
        invoiceDoc.append("deposit", this.deposit);
        invoiceDoc.append("refund", this.refund);
        invoiceDoc.append("fee", this.fee);
        invoiceDoc.append("totalCharge", this.totalCharge);
        invoiceDoc.append("bikes", this.bikeIds);
        db.getCollection("invoices").insertOne(invoiceDoc);
    }

    /**
     * Return a detail String representation of the invoice.
     *
     * @return detail String representation of the invoice
     */
    public String toDetailedString() {
        return String.format("Deposit: +%s\nUsage fees: -%s\n", deposit, fee) + (refund == 0 ? "Total charge: -" + totalCharge : "Refund: +" + refund);
    }

    @Override
    public String toString() {
        return String.format("Deposit: +%s\nUsage fees: -%s\n", deposit, fee) + (refund == 0 ? "Total charge: -" + totalCharge : "Refund: +" + refund) + "\nBike: " + bikeIds.get(0);
    }

    /**
     * Set the list of the bike in the invoice.
     *
     * @param bikes list of bike to be set
     */
    public void setBikeIds(List<String> bikes) {
        this.bikeIds = bikes;
    }
}
