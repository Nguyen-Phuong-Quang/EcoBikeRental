package org.hust.views.returnbike;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.hust.common.exception.HaveNotRentBikeException;
import org.hust.controller.ReturnBikeController;
import org.hust.controller.ViewStationController;
import org.hust.entity.station.Station;
import org.hust.utils.Configs;
import org.hust.utils.Utils;
import org.hust.views.BaseScreenHandler;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class handle the GUI for Return Bike Screen.
 */
public class ReturnBikeScreenHandler extends BaseScreenHandler implements Initializable {
    List<Station> stationList;

    @FXML
    private ScrollPane infoScrollPane;
    @FXML
    private ImageView image;

    /**
     * Initialize ReturnBikeScreenHandler.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public ReturnBikeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public ReturnBikeController getBController() {
        return (ReturnBikeController) super.getBController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBController(new ReturnBikeController(this));
        titleLabel.setText("Return bike");
        subtitleLabel.setText("Empty stations around you:");
        stationList = ViewStationController.getInstance().listStation();
        ObservableList<Station> stationObservableList = FXCollections.observableList(stationList);
        ListView<Station> stationListView = new ListView<>(stationObservableList);
        stationListView.setStyle("-fx-font-size: 16");
        stationListView.setPrefWidth(infoScrollPane.getPrefWidth());
        stationListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        stationListView.getFocusModel().focus(0);
        stationListView.getSelectionModel().selectedItemProperty().addListener((observableValue, station, selected) -> selectedStation = selected);

        infoScrollPane.setContent(stationListView);

        image.setImage(Utils.getImageFromUrl(Objects.requireNonNull(getClass().getResource(Configs.IMAGE_PATH + "map.png")).toString()));

        primaryButton.setText("Return to this station");
        primaryButton.setVisible(true);
        primaryButton.setOnMouseClicked(mouseEvent -> {
            try {
                getBController().requestToReturnBike(selectedBike);
            } catch (HaveNotRentBikeException e) {
                e.printStackTrace();
            }
        });
        secondaryButton.setVisible(false);
    }

    @Override
    public void show() {
        super.show();
    }

}
