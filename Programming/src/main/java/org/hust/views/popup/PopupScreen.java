package org.hust.views.popup;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.hust.utils.Configs;
import org.hust.views.BaseScreenHandler;

import java.io.IOException;

/**
 * This class handle the GUI for Popup screen.
 */
public class PopupScreen extends BaseScreenHandler {
    @FXML
    ImageView image;

    @FXML
    Label message;

    /**
     * Initialize PopupScreen.
     *
     * @param stage      stage to show the GUI
     * @throws IOException if fail to construct the instance
     */
    public PopupScreen(Stage stage) throws IOException {
        super(stage, Configs.POPUP_PATH);
        primaryButton.setOnMouseClicked(mouseEvent -> close(0));
    }

    private static PopupScreen popup(String message, String imagepath, Boolean undecorated) throws IOException {
        PopupScreen popup = new PopupScreen(new Stage());
        if (undecorated) popup.stage.initStyle(StageStyle.UNDECORATED);
        popup.message.setText(message);
        popup.setImage(imagepath);
        return popup;
    }

    /**
     * Popup to show the message of a successful interaction.
     *
     * @param message message to be show
     * @throws IOException if fail to popup
     */
    public static void success(String message) throws IOException {
        popup(message, Configs.IMAGE_PATH + "check.png", true).show(true);
    }

    /**
     * Popup to show the message of an error interaction.
     *
     * @param message message to be show
     * @throws IOException if fail to popup
     */
    public static void error(String message) throws IOException {
        popup(message, Configs.IMAGE_PATH + "cancel.png", false).show(false);
    }

    /**
     * Popup to show the message of a loading interaction.
     *
     * @param message message to be show
     * @return {@link org.hust.views.popup.PopupScreen} that show the loading interaction
     * @throws IOException if fail to popup
     */
    public static PopupScreen loading(String message) throws IOException {
        return popup(message, Configs.IMAGE_PATH + "loading.gif", true);
    }

    /**
     * Set the image of the popup.
     *
     * @param path path to the image
     */
    public void setImage(String path) {
        super.setImage(image, path);
    }

    /**
     * Show the popup.
     *
     * @param autoclose if the popup will be automatically close
     */
    public void show(Boolean autoclose) {
        super.show();
        if (autoclose) close(1);
    }

    /**
     * Show the popup and automatically close it after a certain amount of time (in seconds).
     * @param time amount of time to wait before close the popup
     */
    public void show(double time) {
        super.show();
        close(time);
    }

    /**
     * Close the popup after a certain amount of time (in seconds).
     * @param time amount of time to wait before close the popup
     */
    public void close(double time) {
        PauseTransition delay = new PauseTransition(Duration.seconds(time));
        delay.setOnFinished(event -> stage.close());
        delay.play();
    }
}
