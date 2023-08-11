package org.hust.views.payment;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hust.common.exception.InvalidFormatException;
import org.hust.controller.TransactionController;
import org.hust.entity.payment.CreditCard;
import org.hust.entity.payment.PaymentTransaction;
import org.hust.utils.Configs;
import org.hust.views.BaseScreenHandler;
import org.hust.views.popup.PopupScreen;
import org.hust.views.rentbike.BarcodeScreen;

import java.io.IOException;

/**
 * This class handle the GUI for Payment Screen.
 */
public class PaymentScreenHandler extends BaseScreenHandler {

    private final Object key = new Object();
    private boolean isSuccess = true;

    @FXML
    private TextField firstTextField;

    @FXML
    private TextField secondTextField;

    @FXML
    private TextField thirdTextField;

    @FXML
    private TextField fifthTextField;

    @FXML
    private CheckBox methodCheckBox;

    /**
     * Initialize PaymentScreenHandler.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public PaymentScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        stage.setOnCloseRequest(event -> {
            isSuccess = false;
            finishPayment();
        });
        primaryButton.setOnAction(event -> {
            submitTransactionInfo();
        });
        secondaryButton.setOnAction(event -> {
            isSuccess = false;
            finishPayment();
            getPreviousScreen().show();
        });
        methodCheckBox.setSelected(true);
        methodCheckBox.setDisable(true);
        scanButton.setOnMouseClicked(event -> {
            requestToScanBarcode();
        });
        nowButton.setOnMouseClicked(event -> {
            getHomeScreenHandler().setViewCurrentBikeInUse();
            getHomeScreenHandler().show();
        });
        firstTextField.setText("ict_group7_2021");
        secondTextField.setText("Group 7");
        thirdTextField.setText("1125");
        fifthTextField.setText("279");
    }

    /**
     * Request to make a payment transaction.
     *
     * @param amount   amount of money in the transaction
     * @param contents the transaction contents
     * @return true   - if the transaction success <p>
     * false  - if the transaction fail
     */
    public boolean requestToMakeTransaction(int amount, String contents) {
        TransactionController controller = (TransactionController) getBController();
        controller.setTransactionAmount(amount);
        controller.setTransactionContents(contents);
        show();
        if (TransactionController.getCard() != null) {
            CreditCard card = TransactionController.getCard();
            firstTextField.setText(card.getCardCode());
            secondTextField.setText(card.getOwner());
            fifthTextField.setText(card.getCvvCode());
            thirdTextField.setText(card.getDateExpired());
            submitTransactionInfo();
        }
        Platform.enterNestedEventLoop(key);
        if (isSuccess) {
            return true;
        } else {
            isSuccess = true;
            return false;
        }
    }

    private void submitTransactionInfo() {
        try {
            TransactionController controller = (TransactionController) getBController();
            PaymentTransaction transaction = controller.makeTransaction(firstTextField.getText(),
                    secondTextField.getText(),
                    fifthTextField.getText(),
                    thirdTextField.getText());
            transaction.save();
            isSuccess = true;
            PaymentResultScreenHandler resultScreen = new PaymentResultScreenHandler(stage, Configs.PAYMENT_RESULT_PATH);
            resultScreen.setHomeScreenHandler(getHomeScreenHandler());
            resultScreen.setPreviousScreen(this);
            resultScreen.show(transaction);
        } catch (InvalidFormatException e) {
            try {
                PopupScreen.error(e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            try {
                e.printStackTrace();
                isSuccess = false;
                finishPayment();
                PaymentResultScreenHandler resultScreen =
                        new PaymentResultScreenHandler(stage, Configs.PAYMENT_RESULT_PATH);
                resultScreen.setHomeScreenHandler(homeScreenHandler);
                resultScreen.showError();
                PopupScreen.error(e.getMessage());
                isSuccess = false;
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Break the nested loop that is currently running so that requestToMakeTransaction
     * finally return the result of the payment transaction.
     */
    public void finishPayment() {
        stage.setOnCloseRequest(null);
        if (Platform.isNestedLoopRunning()) {
            Platform.exitNestedEventLoop(key, null);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    private void requestToScanBarcode() {
        try {
            isSuccess = false;
            finishPayment();
            BarcodeScreen barcodeScreen = new BarcodeScreen(this.stage, Configs.BARCODE_PATH);
            barcodeScreen.setHomeScreenHandler(homeScreenHandler);
            barcodeScreen.setPreviousScreen(this);
            barcodeScreen.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
