package org.hust.views.invoice;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hust.controller.ReturnBikeController;
import org.hust.entity.bike.EBike;
import org.hust.views.BaseScreenHandler;
import org.hust.views.popup.PopupScreen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handle the GUI for Invoice Screen.
 */
public class InvoiceScreenHandler extends BaseScreenHandler implements Initializable {
    @FXML
    private Label smallTextLabel;
    @FXML
    private ScrollPane infoScrollPane;
    @FXML
    private ImageView image;

    /**
     * Initialize InvoiceScreenHandler.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public InvoiceScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public ReturnBikeController getBController() {
        return (ReturnBikeController) super.getBController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBController(new ReturnBikeController(this));
        titleLabel.setText("Invoice");
        subtitleLabel.setText(selectedBike.getModel());
        smallTextLabel.setText(selectedBike.getBikeType());
        image.setImage(new Image(selectedBike.getImgUrl()));

        VBox vBox = new VBox();
        String info = getBController().getInvoice().toDetailedString();
        info += ("\n" + selectedBike.toString());
        if (selectedBike instanceof EBike) {
            info += String.format("Battery: %s%%", ((EBike) selectedBike).getBattery());
        }
        Label invoiceInfo = new Label(info);
        invoiceInfo.setStyle("-fx-wrap-text: true; -fx-line-spacing: 10; -fx-font-size: 16");
        vBox.getChildren().add(invoiceInfo);
        infoScrollPane.setContent(vBox);

        secondaryButton.setVisible(false);
        primaryButton.setText("Confirm");
        primaryButton.setOnMouseClicked(mouseEvent -> {
            try {
                getBController().getInvoice().save();
                getHomeScreenHandler().setViewStationList();
                getHomeScreenHandler().show();
                PopupScreen.success("Thank you for using ECOB service!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        primaryButton.setVisible(true);
    }
}
