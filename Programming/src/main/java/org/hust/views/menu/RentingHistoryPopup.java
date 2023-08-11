package org.hust.views.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hust.controller.HomeController;
import org.hust.entity.invoice.Invoice;
import org.hust.views.BaseScreenHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class handle the GUI for Renting History Popup.
 */
public class RentingHistoryPopup extends BaseScreenHandler implements Initializable {
    @FXML
    private VBox mainVBox;

    /**
     * Initialize RentingHistoryPopup.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public RentingHistoryPopup(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBController(new HomeController());
        titleLabel.setText("Your Renting History");
        primaryButton.setOnMouseClicked(mouseEvent -> stage.close());

        ArrayList<Invoice> invoices = getBController().getInvoices();
        ObservableList<Invoice> invoiceObservableList = FXCollections.observableList(invoices);
        ListView<Invoice> invoiceListView = new ListView<>(invoiceObservableList);

        mainVBox.getChildren().set(1, invoiceListView);
    }
}
