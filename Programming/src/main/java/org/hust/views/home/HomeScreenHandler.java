package org.hust.views.home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.hust.common.exception.AlreadyRentBikeException;
import org.hust.controller.HomeController;
import org.hust.controller.RentBikeController;
import org.hust.controller.ViewBikeController;
import org.hust.controller.ViewStationController;
import org.hust.entity.bike.Bike;
import org.hust.entity.bike.EBike;
import org.hust.entity.station.Station;
import org.hust.utils.Configs;
import org.hust.utils.Utils;
import org.hust.views.BaseScreenHandler;
import org.hust.views.popup.PopupScreen;
import org.hust.views.rentbike.BarcodeScreen;
import org.hust.views.returnbike.ReturnBikeScreenHandler;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class handle the GUI for Home Screen.
 */
public class HomeScreenHandler extends BaseScreenHandler implements Initializable {

    RentBikeController rentBikeController = new RentBikeController(this);

    @FXML
    private ScrollPane infoScrollPane;
    @FXML
    private Label smallTextLabel;
    @FXML
    private ImageView image;

    /**
     * Initialize HomeScreenHandler.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public HomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        setHomeScreenHandler(this);
        setPreviousScreen(this);
        setViewStationList();

        searchButton.setOnMouseClicked(mouseEvent -> {
            String location = searchTextField.getText();
            if (StringUtils.isEmpty(location)) {
                stationList = ViewStationController.getInstance().listStation();
            } else {
                stationList = ViewStationController.getInstance().searchStationByLocation(location);
            }
            setViewStationList();
        });

        nowButton.setOnMouseClicked(mouseEvent -> setViewCurrentBikeInUse());

        scanButton.setOnMouseClicked(event -> requestToScanBarcode());
    }

    @Override
    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBController(new HomeController());
    }

    /**
     * Change the home screen into viewing the current rented bike.
     */
    public void setViewCurrentBikeInUse() {
        selectedBike = ViewBikeController.getInstance().checkUserRentedBike();

        if (selectedBike == null) {
            return;
        }
        resetStyle();
        renderBikeInfo();
        subtitleLabel.setText("In Use");
        primaryButton.setVisible(true);
        primaryButton.setText("Return this bike");
        primaryButton.setOnAction(actionEvent -> {
            try {
                ReturnBikeScreenHandler returnBikeScreenHandler = new ReturnBikeScreenHandler(this.stage, Configs.RETURN_PATH);
                returnBikeScreenHandler.setHomeScreenHandler(this);
                returnBikeScreenHandler.setPreviousScreen(this);
                returnBikeScreenHandler.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        secondaryButton.setText("Back");
        secondaryButton.setVisible(true);
        secondaryButton.setOnMouseClicked(mouseEvent -> setViewStation());
    }

    /**
     * Set the home screen into viewing the information of the selected bike.
     */
    private void setViewBike() {
        resetStyle();
        renderBikeInfo();
        primaryButton.setVisible(true);
        primaryButton.setText("Rent this bike");
        primaryButton.setOnAction(event -> {
            try {
                rentBikeController.requestToRentBike(selectedBike);
            } catch (AlreadyRentBikeException e) {
                try {
                    PopupScreen.error(e.getMessage());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        secondaryButton.setText("Back");
        secondaryButton.setVisible(true);
        secondaryButton.setOnMouseClicked(mouseEvent -> setViewStation());
    }

    /**
     * Set the home screen into viewing the information of a station.
     */
    private void setViewStation() {
        if (selectedStation == null) {
            setViewStationList();
            return;
        }
        resetStyle();
        titleLabel.setText("Station");
        subtitleLabel.setText(selectedStation.getLocation());
        int bikeCount = selectedStation.getBikeIds().size();
        smallTextLabel.setText(bikeCount + " bikes, " + selectedStation.getEmptyDocksCount() + " empty docks");

        ObservableList<Bike> bikesObservableList = FXCollections.observableList(selectedStation.getStationBikes());
        ListView<Bike> bikesListView = new ListView<>(bikesObservableList);
        bikesListView.setStyle("-fx-font-size: 16");
        bikesListView.setPrefWidth(infoScrollPane.getPrefWidth() - 10);
        bikesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        bikesListView.getFocusModel().focus(0);
        bikesListView.setPrefHeight(bikesObservableList.size() * 35);
        bikesListView.getSelectionModel().selectedItemProperty().addListener((observableValue, station, selected) -> selectedBike = selected);

        infoScrollPane.setContent(bikesListView);

        image.setImage(Utils.getImageFromUrl(Objects.requireNonNull(getClass().getResource(Configs.IMAGE_PATH + "station.png")).toString()));

        primaryButton.setText("View Bike");
        primaryButton.setVisible(true);
        primaryButton.setOnAction(event -> setViewBike());
        secondaryButton.setText("Back");
        secondaryButton.setVisible(true);
        secondaryButton.setOnMouseClicked(mouseEvent -> {
            if (stationList.size() != 0) {
                setViewStationList();
            }
        });
    }

    /**
     * Set the home screen into viewing the list of all stations.
     */
    public void setViewStationList() {
        resetStyle();
        titleLabel.setText("Welcome back!");
        subtitleLabel.setText("Stations around you:");
        smallTextLabel.setText("");

        ObservableList<Station> stationObservableList = FXCollections.observableList(stationList);
        ListView<Station> stationListView = new ListView<>(stationObservableList);
        stationListView.setStyle("-fx-font-size: 16");
        stationListView.setPrefWidth(infoScrollPane.getPrefWidth() - 10);
        stationListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        stationListView.getFocusModel().focus(0);
        stationListView.setPrefHeight(stationList.size() * 35);
        stationListView.getSelectionModel().selectedItemProperty().addListener((observableValue, station, selected) -> selectedStation = selected);

        infoScrollPane.setContent(stationListView);

        image.setImage(Utils.getImageFromUrl(Objects.requireNonNull(getClass().getResource(Configs.IMAGE_PATH + "map.png")).toString()));

        primaryButton.setText("View Station");
        primaryButton.setVisible(true);
        primaryButton.setOnAction(event -> setViewStation());
        secondaryButton.setVisible(false);
    }

    private void requestToScanBarcode() {
        try {
            BarcodeScreen barcodeScreen = new BarcodeScreen(this.stage, Configs.BARCODE_PATH);
            barcodeScreen.setHomeScreenHandler(this);
            barcodeScreen.setPreviousScreen(this);
            barcodeScreen.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderBikeInfo() {
        titleLabel.setText(selectedBike.getModel().toUpperCase(Locale.ROOT));
        subtitleLabel.setText(selectedBike.isStatus() ? "Available" : "Unavailable");
        if (subtitleLabel.getText().equals("Available")) {
            subtitleLabel.setStyle("-fx-text-fill: green");
        }
        else {
            subtitleLabel.setStyle("-fx-text-fill: red");
        }
        smallTextLabel.setText(selectedBike.getBikeType());

        VBox vBox = new VBox();
        String info = selectedBike.toDetailedString();
        if (selectedBike instanceof EBike) {
            info += String.format("Battery: %s%%", ((EBike) selectedBike).getBattery());
        }
        Label bikeInfo = new Label(info);
        bikeInfo.setStyle("-fx-wrap-text: true; -fx-line-spacing: 10; -fx-font-size: 16");
        vBox.getChildren().add(bikeInfo);
        infoScrollPane.setContent(vBox);

        image.setImage(Utils.getImageFromUrl(selectedBike.getImgUrl()));
    }

    private void resetStyle() {
        subtitleLabel.setStyle("-fx-text-fill: black");
    }
}
