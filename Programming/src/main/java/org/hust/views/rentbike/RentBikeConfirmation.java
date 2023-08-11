package org.hust.views.rentbike;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hust.common.exception.AlreadyRentBikeException;
import org.hust.controller.RentBikeController;
import org.hust.entity.bike.Bike;
import org.hust.entity.bike.EBike;
import org.hust.utils.Utils;
import org.hust.views.BaseScreenHandler;
import org.hust.views.popup.PopupScreen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handle the GUI for Rent Bike Confirmation Popup.
 */
public class RentBikeConfirmation extends BaseScreenHandler implements Initializable {
    @FXML
    private Label confirmLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label smallTextLabel;

    @FXML
    private Button primaryButton;

    @FXML
    private Button secondaryButton;

    @FXML
    private ScrollPane infoScrollPane;

    @FXML
    private ImageView image;

    private Bike currentlyShowBike;

    /**
     * Initialize RentBikeConfirmation.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public RentBikeConfirmation(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        confirmLabel.setText("Renting Confirmation");
        primaryButton.setOnAction(event -> {
            this.stage.close();
            confirmToRentBike();
        });
        secondaryButton.setOnAction(event -> this.stage.close());
    }

    /**
     * Set up the screen to show the bike information.
     *
     * @param bike bike to be show
     */
    public void renderContent(Bike bike) {
        this.currentlyShowBike = bike;
        titleLabel.setText(bike.getModel());
        subtitleLabel.setText(bike.getLocation());
        smallTextLabel.setText(bike.getBikeType());
        image.setImage(new Image(bike.getImgUrl()));
        VBox vb = new VBox();
        String info = String.format("Speed: %.2f km/h\nColor: %s\nWeight: %.2f kg\nDescription: %s\nValue: %s\nDeposit: %s\n",
                bike.getSpeed() * 100,
                bike.getColor(),
                bike.getWeight() * 100,
                bike.getDescription(),
                Utils.getCurrencyFormat(bike.getValue()),
                Utils.getCurrencyFormat(bike.getDeposit()));
        if (bike instanceof EBike) {
            info += "Battery: " + ((EBike) bike).getBattery();
        }
        vb.getChildren().add(new Label(info));
        infoScrollPane.setContent(vb);
    }

    private void confirmToRentBike() {
        RentBikeController controller = (RentBikeController) getBController();
        try {
            controller.rentBike(currentlyShowBike);
        } catch (AlreadyRentBikeException e) {
            try {
                PopupScreen.error(e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
