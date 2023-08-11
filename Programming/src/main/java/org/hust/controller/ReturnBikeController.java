package org.hust.controller;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hust.common.exception.HaveNotRentBikeException;
import org.hust.entity.bike.Bike;
import org.hust.entity.invoice.Invoice;
import org.hust.utils.Configs;
import org.hust.views.BaseScreenHandler;
import org.hust.views.invoice.InvoiceScreenHandler;
import org.hust.views.payment.PaymentScreenHandler;
import org.hust.views.returnbike.ReturnBikeConfirmation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the return bike use case.
 */
public class ReturnBikeController extends BaseController {

    private BaseScreenHandler currentScreen;
    private static Invoice invoice;

    /**
     * Constructor for creating a ReturnBikeController instance.
     *
     * @param screenThatCallReturnBike the screen that need to initiate return bike use case
     */
    public ReturnBikeController(BaseScreenHandler screenThatCallReturnBike) {
        this.currentScreen = screenThatCallReturnBike;
    }

    /**
     * Request to return bike, which will call the return bike confirm screen to show the bike's info and wait for user
     * confirmation.
     *
     * @param bike Bike that need to be returned
     * @throws HaveNotRentBikeException if the user currently doesn't rent any bike
     */
    public void requestToReturnBike(Bike bike) throws HaveNotRentBikeException {
        if (bike.isAvailable())
            throw new HaveNotRentBikeException("The bike is not rented yet!");

        try {
            Stage secondStage = new Stage();
            secondStage.initModality(Modality.APPLICATION_MODAL);
            ReturnBikeConfirmation returnBikeConfirmation = new ReturnBikeConfirmation(secondStage, Configs.CONFIRM_PATH);
            returnBikeConfirmation.setBController(this);
            returnBikeConfirmation.renderContent(bike);
            returnBikeConfirmation.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process to make payment transaction to return bikes, then show the user invoice made by the
     * return bike process.
     *
     * @param bikes list of bike to be return
     */
    public void returnBike(Bike... bikes) {
        String transactionContents = "Fee for return bike";
        List<String> bikeIds = new ArrayList<>();
        int transactionAmount = 0, deposit = 0, fee = 0, refund = 0, totalCharge = 0;
        for (Bike bike : bikes) {
            bike.calculateFee();
            transactionAmount += bike.getTotalCharge();
            deposit += bike.getDeposit();
            fee += bike.getFee();
            bikeIds.add(bike.get_id().toString());
        }

        if (transactionAmount < 0) refund = -(transactionAmount);
        else totalCharge = transactionAmount;

        try {
            PaymentScreenHandler paymentScreen = new PaymentScreenHandler(currentScreen.getStage(), Configs.PAYMENT_PATH);
            paymentScreen.setHomeScreenHandler(currentScreen.getHomeScreenHandler());
            paymentScreen.setPreviousScreen(currentScreen);
            paymentScreen.setBController(new TransactionController());
            if (paymentScreen.requestToMakeTransaction(transactionAmount, transactionContents)) {
                invoice = new Invoice(TransactionController.getTransaction(), deposit, refund, fee, totalCharge, bikeIds);
                InvoiceScreenHandler invoiceScreenHandler = new InvoiceScreenHandler(currentScreen.getStage(), Configs.INVOICE_PATH);
                invoiceScreenHandler.setHomeScreenHandler(currentScreen.getHomeScreenHandler());
                invoiceScreenHandler.setPreviousScreen(currentScreen);
                invoiceScreenHandler.setBController(this);
                invoiceScreenHandler.show();
                for (Bike bike : bikes) {
                    bike.lock();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the invoice made by the return bike process.
     *
     * @return {@link org.hust.entity.invoice.Invoice Invoice} of the process
     */
    public Invoice getInvoice() {
        return invoice;
    }
}
