package org.hust.views.menu;

import javafx.stage.Stage;
import org.hust.views.BaseScreenHandler;

import java.io.IOException;

/**
 * This class handle the GUI for the Pricing Policy Popup.
 */
public class PricingPolicyPopup extends BaseScreenHandler {

    /**
     * Initialize PricingPolicyPopup.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public PricingPolicyPopup(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        primaryButton.setOnMouseClicked(mouseEvent -> stage.close());
    }
}
