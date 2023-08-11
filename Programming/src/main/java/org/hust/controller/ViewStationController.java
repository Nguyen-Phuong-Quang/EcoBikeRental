package org.hust.controller;

import lombok.SneakyThrows;
import org.hust.common.exception.InvalidLocationFormatException;
import org.hust.entity.station.Station;
import org.hust.utils.Validator;
import org.hust.views.popup.PopupScreen;

import java.util.List;

/**
 * @author hoang.lh194766
 * <p>
 * The controller for view station use case.
 */
public class ViewStationController extends BaseController {

    private static ViewStationController instance;

    /**
     * Return the instance of the ViewBikeController.
     *
     * @return {@link org.hust.controller.ViewStationController ViewStationController} that control the ViewStation process
     */
    public static ViewStationController getInstance() {
        if (instance == null) {
            instance = new ViewStationController();
        }
        return instance;
    }

    /**
     * Search the station by using location.
     *
     * @param location the location of the station
     * @return List of {@link org.hust.entity.station.Station Station} that is located at that location <p>
     * or null if there are none
     */
    @SneakyThrows
    public List<Station> searchStationByLocation(String location) {

        if (validateLocation(location)) {
            return Station.getStationByLocation(location);
        } else {
            PopupScreen.error(new InvalidLocationFormatException().getMessage());
            return null;
        }
    }

    /**
     * Return the information of all stations.
     *
     * @return List of all {@link org.hust.entity.station.Station Station}
     */
    public List<Station> listStation() {
        return Station.listAllStation();
    }

    /**
     * Validate the format of the station id input by user.
     *
     * @param id the id to be validated
     * @return true - if it is in correct format <p>
     * false - otherwise
     */
    public boolean validateId(String id) {
        return Validator.validateNoSpecialCharacterString(id);
    }

    /**
     * Validate the format of the station's location input by user.
     *
     * @param location the location to be validated
     * @return true - if it is in correct format <p>
     * false - otherwise
     */
    public boolean validateLocation(String location) {
        return Validator.validateSomeSpecialCharacterString(location, ',', '.', ' ');
    }
}
