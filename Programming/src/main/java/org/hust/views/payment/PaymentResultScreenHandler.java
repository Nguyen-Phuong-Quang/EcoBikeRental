package org.hust.views.payment;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.hust.entity.payment.PaymentTransaction;
import org.hust.utils.Utils;
import org.hust.views.BaseScreenHandler;

import java.io.IOException;

/**
 * This class handle the GUI for Payment Result Screen.
 */
public class PaymentResultScreenHandler extends BaseScreenHandler {
    @FXML
    private Label transactionIdLabel;

    @FXML
    private Label cardHolderLabel;

    @FXML
    private Label transactionAmountLabel;

    @FXML
    private Label transactionContentsLabel;

    @FXML
    private Label transactionCommandLabel;

    @FXML
    private Label transactionTimeLabel;

    /**
     * Initialize PaymentResultScreenHandler.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public PaymentResultScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        primaryButton.setOnAction(event -> {
            PaymentScreenHandler prev = (PaymentScreenHandler) getPreviousScreen();
            prev.finishPayment();
        });
        secondaryButton.setVisible(false);
    }

    @Override
    public void show() {
        super.show();
    }

    /**
     * Show the result of a successful payment transaction with info.
     *
     * @param transaction transaction to be show
     */
    public void show(PaymentTransaction transaction) {
        subtitleLabel.setText("Transaction success!");
        transactionIdLabel.setText(transaction.getId());
        cardHolderLabel.setText(transaction.getOwner());
        transactionAmountLabel.setText(Utils.getCurrencyFormat(transaction.getAmount()));
        transactionContentsLabel.setText(transaction.getContent());
        transactionCommandLabel.setText(transaction.getCommand());
        transactionTimeLabel.setText(transaction.getTime());
        show();
    }

    /**
     * Show the result of a fail payment transaction.
     */
    public void showError() {
        subtitleLabel.setText("Transaction fail!");
        primaryButton.setOnAction(event -> {
            getHomeScreenHandler().setViewStationList();
            getHomeScreenHandler().show();
        });
        show();
    }

}
