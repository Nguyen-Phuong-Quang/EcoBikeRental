package org.hust.entity.bike;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.hust.utils.Utils;

/**
 * EBike class specifies electronic bike, extended from Bike abstract class
 *
 * @author hoang.lh194766
 */
@Getter
@NoArgsConstructor
public class EBike extends Bike {
    private int battery;

    /**
     * Constructor for EBike class.
     *
     * @param model model of the electronic bike
     * @param status status of the electronic bike
     * @param speed maximum speed of the electronic bike
     * @param color color of the electronic bike
     * @param weight weight of the electronic bike
     * @param description description of the electronic bike
     * @param value value of the electronic bike
     * @param barcode barcode of the electronic bike
     * @param imgUrl the image URL of the electronic bike
     * @param battery battery of the electronic bike
     */
    public EBike(String model, boolean status, double speed, String color,
                 double weight, String description, int value, String barcode, String imgUrl,
                 int battery) {
        super(model, status, speed, color, weight, description, value, barcode, imgUrl);
        this.battery = battery;
        this.bikeType = EBike.class;
    }

    /**
     * check Bike.documentToBike for more information
     *
     * @param document the document needed to be cast
     * @return EBike
     */
    @Override
    public EBike documentToBike(Document document) {
        return Utils.documentToObject(document, EBike.class);
    }

    @Override
    public double getPriceCoefficient() {
        return 1.5f;
    }

    @Override
    public String getBikeType() {
        return "Electronic Bike";
    }
}
