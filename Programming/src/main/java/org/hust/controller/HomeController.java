package org.hust.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.hust.entity.db.Database;
import org.hust.entity.invoice.Invoice;
import org.hust.utils.Utils;

import java.util.ArrayList;

/**
 * This class represent the controller for home screen.
 */
public class HomeController extends BaseController {

    /**
     * Connect to the database to get the information of all the invoices that has been made.
     * @return list of {@link org.hust.entity.invoice.Invoice Invoice} stored in the database
     */
    public ArrayList<Invoice> getInvoices() {
        ArrayList<Invoice> invoices = new ArrayList<>();
        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> invoiceCollection = db.getCollection("invoices");
        FindIterable<Document> iterDoc = invoiceCollection.find();
        for (Document doc: iterDoc) {
            Invoice invoice = Utils.documentToObject(doc, Invoice.class);
            invoice.setBikeIds(doc.get("bikes", new ArrayList<String>().getClass()));
            invoices.add(invoice);
        }
        return invoices;
    }

}
