package org.hust.controller;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hust.common.exception.AlreadyRentBikeException;
import org.hust.common.exception.InvalidBarcodeException;
import org.hust.entity.bike.Bike;
import org.hust.utils.Configs;
import org.hust.views.BaseScreenHandler;
import org.hust.views.payment.PaymentScreenHandler;
import org.hust.views.popup.PopupScreen;
import org.hust.views.rentbike.RentBikeConfirmation;

import java.io.IOException;

/**
 * Controller for rent bike use case.
 */
public class RentBikeController extends BaseController {

    private static Bike currentlyRentedBike;
    private BaseScreenHandler screenThatCallRentBike;

    /**
     * For test only, do not use this.
     */
    public RentBikeController() {
    }

    /**
     * Constructor for creating a RentBikeController instance.
     *
     * @param screenThatCallRentBike the screen that need to initiate rent bike use case
     */
    public RentBikeController(BaseScreenHandler screenThatCallRentBike) {
        this.screenThatCallRentBike = screenThatCallRentBike;
    }

    /**
     * Return the currently rented bike.
     *
     * @return {@link org.hust.entity.bike.Bike Bike} - the currently rented bike.
     */
    public static Bike getCurrentlyRentedBike() {
        return currentlyRentedBike;
    }

    /**
     * Set the currently rented bike of the software.
     *
     * @param bike bike that have been rented
     */
    public static void setCurrentlyRentedBike(Bike bike) {
        currentlyRentedBike = bike;
    }

    /**
     * Request to rent bike, which will call the rent bike confirm screen to show the bike's info and wait for user
     * confirmation.
     *
     * @param barcode barcode of the bike that is requested to be rent
     * @throws InvalidBarcodeException if there are no bike with the corresponding barcode
     */
    public void requestToRentBike(String barcode) throws InvalidBarcodeException {
        if (!validateBarcode(barcode)) {
            throw new InvalidBarcodeException();
        }
        Bike bike = Bike.getBike(barcode);
        try {
            Stage secondStage = new Stage();
            secondStage.initModality(Modality.APPLICATION_MODAL);
            RentBikeConfirmation bikeScreen = new RentBikeConfirmation(secondStage, Configs.CONFIRM_PATH);
            bikeScreen.setBController(this);
            bikeScreen.renderContent(bike);
            bikeScreen.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Request to rent bike, which will call the rent bike confirm screen to show the bike's info and wait for user
     * confirmation.
     *
     * @param bike the bike that is requested to be rent
     * @throws AlreadyRentBikeException if the bike is already rent
     */
    public void requestToRentBike(Bike bike) throws AlreadyRentBikeException {
        if (currentlyRentedBike != null) {
            throw new AlreadyRentBikeException("You are already renting a bike");
        }
        if (!bike.isAvailable()) {
            throw new AlreadyRentBikeException("Bike not available");
        }
        try {
            Stage secondStage = new Stage();
            secondStage.initModality(Modality.APPLICATION_MODAL);
            RentBikeConfirmation bikeScreen = new RentBikeConfirmation(secondStage, Configs.CONFIRM_PATH);
            bikeScreen.setBController(this);
            bikeScreen.renderContent(bike);
            bikeScreen.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process to make payment transaction to rent bike, then unlock that bike.
     *
     * @param bike bike to be rent
     * @throws AlreadyRentBikeException if the bike is already rent
     */
    public void rentBike(Bike bike) throws AlreadyRentBikeException {
        if (currentlyRentedBike != null) {
            throw new AlreadyRentBikeException("You are already renting a bike");
        }
        String transactionContents = "Fee for rent bike " + bike.getModel();
        int transactionAmount = bike.getDeposit();
        try {
            PaymentScreenHandler paymentScreen = new PaymentScreenHandler(screenThatCallRentBike.getStage(), Configs.PAYMENT_PATH);
            paymentScreen.setHomeScreenHandler(screenThatCallRentBike.getHomeScreenHandler());
            paymentScreen.setPreviousScreen(screenThatCallRentBike);
            paymentScreen.setBController(new TransactionController());
            if (paymentScreen.requestToMakeTransaction(transactionAmount, transactionContents)) {
                bike.unlock();
                screenThatCallRentBike.getHomeScreenHandler().setViewStationList();
                screenThatCallRentBike.getHomeScreenHandler().show();
                PopupScreen.success("Rent Successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate the format of user's input barcode.
     *
     * @param barcode user's input barcode
     * @return true  - if the barcode is in correct format <p>
     * false - if otherwise
     */
    public boolean validateBarcode(String barcode) {
        if (barcode == null) {
            return false;
        }
        barcode = barcode.trim();
        if (barcode.length() != 8) {
            return false;
        }
        try {
            Integer.parseInt(barcode);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}