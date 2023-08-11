package org.hust.views;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hust.controller.BaseController;
import org.hust.controller.ViewStationController;
import org.hust.entity.bike.Bike;
import org.hust.entity.station.Station;
import org.hust.utils.Configs;
import org.hust.views.home.HomeScreenHandler;
import org.hust.views.menu.PricingPolicyPopup;
import org.hust.views.menu.RentingHistoryPopup;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

/**
 * This class represent all the class that handle the screen in EcoBikeRentalService software.
 */
public class BaseScreenHandler extends FXMLScreenHandler {

    protected final Stage stage;
    protected HomeScreenHandler homeScreenHandler;
    protected Hashtable<String, String> messages;

    /**
     * Save the station List, either by initial or filtered with user search,
     * used as a cache memory for runtime utility purpose
     */
    protected List<Station> stationList;
    /**
     * Save the station selected by user from the list
     * used as a cache memory for runtime utility purpose
     */
    protected Station selectedStation;
    /**
     * Save the bike selected by user from the list
     * used as a cache memory for runtime utility purpose
     */
    protected static Bike selectedBike;

    @FXML
    protected TextField searchTextField;
    @FXML
    protected Button searchButton;
    @FXML
    protected VBox nowButton;
    @FXML
    protected VBox scanButton;
    @FXML
    protected VBox priceButton;
    @FXML
    protected VBox historyButton;
    @FXML
    protected Button primaryButton;
    @FXML
    protected Button secondaryButton;
    @FXML
    protected Label titleLabel;
    @FXML
    protected Label subtitleLabel;
    private Scene scene;
    private BaseScreenHandler prev;
    private BaseController bController;

    private BaseScreenHandler(String screenPath) throws IOException {
        super(screenPath);
        this.stage = new Stage();
    }

    /**
     * Initialize BaseScreenHandler.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public BaseScreenHandler(Stage stage, String screenPath) throws IOException {
        super(screenPath);
        this.stage = stage;
        stationList = ViewStationController.getInstance().listStation();
    }

    /**
     * Get the previous screen of the screen.
     *
     * @return the previous screen.
     */
    public BaseScreenHandler getPreviousScreen() {
        return this.prev;
    }

    /**
     * Set the previous screen of this screen.
     *
     * @param prev the previous screen of this screen
     */
    public void setPreviousScreen(BaseScreenHandler prev) {
        this.prev = prev;
        priceButton.setOnMouseClicked(mouseEvent -> {
            Stage newStage = new Stage();
            try {
                PricingPolicyPopup pricingPolicyPopup = new PricingPolicyPopup(newStage, Configs.PRICING_PATH);
                pricingPolicyPopup.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        historyButton.setOnMouseClicked(mouseEvent -> {
            Stage newStage = new Stage();
            try {
                RentingHistoryPopup rentingHistoryPopup = new RentingHistoryPopup(newStage, Configs.PRICING_PATH);
                rentingHistoryPopup.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Show the screen to the user.
     */
    public void show() {
        if (this.scene == null) {
            this.scene = new Scene(this.content);
        }
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    /**
     * Set the screen title of the screen.
     *
     * @param string title to be set
     */
    public void setScreenTitle(String string) {
        this.stage.setTitle(string);
    }

    /**
     * Get the controller of the screen.
     *
     * @return controller of the screen
     */
    public BaseController getBController() {
        return this.bController;
    }

    /**
     * Set the controller for the screen.
     *
     * @param bController controller to be set
     */
    public void setBController(BaseController bController) {
        this.bController = bController;
    }

    /**
     * Return the stage that is used to show this screen.
     *
     * @return the stage that is used
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Return the home screen of the screen.
     *
     * @return the home screen of the screen
     */
    public HomeScreenHandler getHomeScreenHandler() {
        return this.homeScreenHandler;
    }

    /**
     * Set the home screen for {@link org.hust.views.BaseScreenHandler BaseScreenHandler}.
     * @param HomeScreenHandler the screen to be set to home screen
     */
    public void setHomeScreenHandler(HomeScreenHandler HomeScreenHandler) {
        this.homeScreenHandler = HomeScreenHandler;
    }
}

