package org.hust.entity.bike;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hust.common.exception.InvalidBarcodeException;
import org.hust.common.exception.InvalidIdException;
import org.hust.controller.RentBikeController;
import org.hust.entity.db.Database;
import org.hust.entity.station.Station;
import org.hust.utils.Timer;
import org.hust.utils.Utils;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Bike class is an abstract class that all types of bike need to extend from
 *
 * @author hoang.lh194766, tu.bm194870
 * <p> To operate properly, the child class need to have: </p>
 * <ul>
 *     <li><code>@Getter</code> annotation</li>
 *     <li><code>@NoArgsConstructor</code> annotation</li>
 *     <li><code>documentToBike()</code> implemented</li>
 *     <li><code>priceCoefficient()</code> implemented</li>
 * </ul>
 */
@Getter
@NoArgsConstructor
public abstract class Bike {
    /**
     * INITIAL_CHARGE is the starting price in the first 30 minutes
     */
    protected static final int INITIAL_CHARGE = 10000;
    /**
     * UNIT_FEE is the price for each additional 15-minute
     */
    protected static final int UNIT_FEE = 3000;

    private final ObjectId _id = new ObjectId();
    /**
     * bikeType represent the type of the bike
     */
    protected Class bikeType;
    /**
     * usageTime represent the time this bike has been used
     */
    protected int usageTime;
    /**
     * fee represent the fee to rent the bike
     */
    protected int fee;
    private String model;
    private boolean status;
    private double speed;
    private String color;
    private double weight;
    private String description;
    private int value;
    private String barcode;
    private String imgUrl;
    private Timer rentTimer = new Timer();

    /**
     * The constructor for the abstract class {@link org.hust.entity.bike.Bike Bike}.
     *
     * @param model bike's model
     * @param status bike's status
     * @param speed bike's max speed
     * @param color bike's color
     * @param weight bike's weight
     * @param description bike's description
     * @param value bike's value
     * @param barcode bike's barcode
     * @param imgUrl bike's image URL
     */
    protected Bike(String model, boolean status, double speed, String color,
                   double weight, String description, int value, String barcode, String imgUrl) {

        this.model = model;
        this.status = status;
        this.speed = speed;
        this.color = color;
        this.weight = weight;
        this.description = description;
        this.value = value;
        this.barcode = barcode;
        this.imgUrl = imgUrl;
    }

    /**
     * Connect to the database to retrieve the information of the bike with correspond barcode.
     *
     * @param barcode the barcode of the bike to retrieved
     * @return {@link org.hust.entity.bike.Bike Bike} with that barcode
     * @throws InvalidBarcodeException if fail to retrieve any bike with that barcode
     */
    public static Bike getBike(String barcode) throws InvalidBarcodeException {

        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> bikeCollection = db.getCollection("bikes");
        Document bike = bikeCollection.find(new Document("barcode", barcode)).first();
        if (bike == null) {
            throw new InvalidBarcodeException();
        }

        try {
            Class bikeType = Class.forName(bike.getString("bikeType"));
            return ((Bike) bikeType.getDeclaredConstructor().newInstance()).documentToBike(bike);
        } catch (Exception e) {
            return (new NormalBike()).documentToBike(bike);
        }
    }

    /**
     * Connect to the database to retrieve the information of the bike with correspond id.
     *
     * @param id the id of the bike to retrieved
     * @return {@link org.hust.entity.bike.Bike Bike} with that id
     */
    @SneakyThrows
    public static Bike getBikeById(String id) {
        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> bikeCollection = db.getCollection("bikes");
        Document bike = bikeCollection.find(new Document("_id", new ObjectId(id))).first();


        if (bike == null) {
            System.out.println(id);
            throw new InvalidIdException();
        }

        try {
            Class bikeType = Class.forName(bike.getString("bikeType"));
            Reflections reflections = new Reflections(Bike.class);
            for (Class<? extends Bike> c : reflections.getSubTypesOf(Bike.class)) {
                if (bikeType.equals(c)) {
                    return (c.getDeclaredConstructor().newInstance()).documentToBike(bike);
                }
            }
            throw new ClassNotFoundException();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return (new NormalBike()).documentToBike(bike);
        }
    }

    /**
     * Unlock the bike, then register the new status to the database.
     */
    public void unlock() {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", this._id);

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("status", false);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);

        Database.getConnection().getCollection("bikes").updateOne(query, updateObject);
        status = false;

        RentBikeController.setCurrentlyRentedBike(this);
        run();
    }

    /**
     * Lock the bike, then register the new status to the database.
     */
    public void lock() {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", this._id);

        BasicDBObject newDocument = new BasicDBObject();
        newDocument.put("status", true);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", newDocument);

        Database.getConnection().getCollection("bikes").updateOne(query, updateObject);
        status = true;

        RentBikeController.setCurrentlyRentedBike(null);
        stop();
        reset();
    }

    /**
     * The abstract function require the children class to generate a function
     * to set a document to that children class
     *
     * @param document the document needed to be cast
     * @return the child class object
     */
    public abstract Bike documentToBike(Document document);

    /**
     * Return a detailed String representation of the bike.
     *
     * @return detailed String of the bike
     */
    public String toDetailedString() {
        calculateFee();
        return String.format("Speed: %.2f km/h\nColor: %s\nWeight: %.2f kg\nDescription: %s\nValue: %s\nRent time: %s minutes\nCurrent fee: %s\n",
                speed * 100,
                color,
                weight * 100,
                description,
                Utils.getCurrencyFormat(value),
                getUsageTime(),
                Utils.getCurrencyFormat(this.fee));
    }

    @Override
    public String toString() {
        return model + " - " + getBikeType();
    }

    /**
     * Return the location of the bike.
     *
     * @return location of the bike
     */
    public String getLocation() {
        return Objects.requireNonNull(Station.getStationContainBike(this._id.toString())).getLocation();
    }

    /**
     * Check the availability of the bike
     *
     * @return true - if bike is available to rent <p>
     * false - otherwise
     */
    public boolean isAvailable() {
        return status;
    }


    /**
     * Price coefficient differs in each bike type
     *
     * @return price coefficient based on bike type
     */
    public abstract double getPriceCoefficient();

    /**
     * Deposit must be at least 40 percent of bike value
     *
     * @return deposit money
     */
    public int getDeposit() {
        return value * 40 / 100;
    }

    /**
     * Total charge is the total price to pay including deposit, fee, tax...
     * <p>
     * In case <code>total charge &lt; 0</code>, customer is refunded
     *
     * @return total charge
     */
    public int getTotalCharge() {
        return fee - getDeposit();
    }

    /**
     * Calculating the fee to return the bike.
     */
    public void calculateFee() {
        this.fee = (int) (getPriceCoefficient() * (getUsageTime() <= 30 ? INITIAL_CHARGE : INITIAL_CHARGE + UNIT_FEE * (getUsageTime() - 30)));
    }

    /**
     * Return the type of the bike.
     *
     * @return type of the bike
     */
    public abstract String getBikeType();

    /**
     * Run this bike.
     */
    public void run() {
        Thread t = new Thread(rentTimer);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Stop this bike.
     */
    public void stop() {
        rentTimer.stop();
    }

    /**
     * Reset the timer counting rent time in this bike.
     */
    public void reset() {
        rentTimer.reset();
    }

    /**
     * Return the time this bike has been rented.
     *
     * @return bike's rent time
     */
    public long getUsageTime() {
        usageTime = rentTimer.getCount();
        return usageTime;
    }

    /**
     * The class for Bike Deserializer.
     */
    public static class BikeDeserializer extends StdDeserializer<Bike> {
        /**
         * Constructor for BikeDeserializer.
         */
        public BikeDeserializer() {
            this(null);
        }

        protected BikeDeserializer(final Class<?> vc) {
            super(vc);
        }

        @SneakyThrows
        @Override
        public Bike deserialize(final JsonParser parser, final DeserializationContext context) {
            final JsonNode node = parser.getCodec().readTree(parser);
            final ObjectMapper mapper = (ObjectMapper) parser.getCodec();
            Class bikeType = Class.forName(node.get("bikeType").textValue());
            Reflections reflections = new Reflections(Bike.class);
            for (Class<? extends Bike> c : reflections.getSubTypesOf(Bike.class)) {
                if (bikeType.equals(c)) {
                    return mapper.treeToValue(node, c);
                }
            }
            return mapper.treeToValue(node, NormalBike.class);
        }
    }
}
