package org.hust.entity.bike;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.hust.utils.Utils;

/**
 * TwinBike class specifies twin bike type which is built for two cyclists, extended from Bike abstract class
 *
 * @author hoang.lh194766
 */
@Getter
@NoArgsConstructor
public class TwinBike extends Bike {
    /**
     * Constructor for TwinBike class.
     *
     * @param model model of the twin bike
     * @param status status of the twin bike
     * @param speed maximum speed of the twin bike
     * @param color color of the twin bike
     * @param weight weight of the twin bike
     * @param description description of the twin bike
     * @param value value of the twin bike
     * @param barcode barcode of the twin bike
     * @param imgUrl the image URL of the twin bike
     */
    public TwinBike(String model, boolean status, double speed, String color,
                    double weight, String description, int value, String barcode, String imgUrl) {
        super(model, status, speed, color, weight, description, value, barcode, imgUrl);
        this.bikeType = TwinBike.class;
    }

    /**
     * check Bike.documentToBike for more information
     *
     * @param document the document needed to be cast
     * @return TwinBike
     */
    @Override
    public TwinBike documentToBike(Document document) {
        return Utils.documentToObject(document, TwinBike.class);
    }

    @Override
    public double getPriceCoefficient() {
        return 1.5f;
    }

    @Override
    public String getBikeType() {
        return "Twin Bike";
    }
}
