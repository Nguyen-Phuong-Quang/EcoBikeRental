package org.hust.entity.station;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hust.common.exception.InvalidIdException;
import org.hust.common.exception.InvalidLocationException;
import org.hust.entity.bike.Bike;
import org.hust.entity.db.Database;
import org.hust.utils.Utils;
import org.hust.views.popup.PopupScreen;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Station class represents station entity
 *
 * @author hoang.lh194766
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Station {
    /**
     * The station capacity
     */
    public static int CAPACITY = 30;
    /**
     * Unique ID of the station
     */
    public ObjectId _id = new ObjectId();
    private String location;
    private List<String> bikeIds;

    /**
     * Constructor for Station class.
     *
     * @param location location of the station
     * @param bikeIds id list of bikes in the station
     */
    public Station(String location, List<String> bikeIds) {
        this.location = location;
        this.bikeIds = bikeIds;
    }

    /**
     * Connect to the database to retrieve the information of the station with that id.
     *
     * @param id id of the station to be retrieved
     * @return {@link org.hust.entity.station.Station Station} with that id
     */
    @SneakyThrows
    public static Station getStationById(String id) {
        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> stationCollection = db.getCollection("stations");
        Document station = stationCollection.find(new Document("_id", new ObjectId(id))).first();
        if (station == null) {
            PopupScreen.error(new InvalidIdException().getMessage());
            return null;
        }
        return Utils.documentToObject(station, Station.class);
    }

    /**
     * Connect to the database to search for the station that is currently contain the bike.
     *
     * @param bikeId id of bike that need to find
     * @return {@link org.hust.entity.station.Station Station} that contain the bike
     */
    @SneakyThrows
    public static Station getStationContainBike(String bikeId) {
        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> stationCollection = db.getCollection("stations");
        Document station = stationCollection.find(new Document("bikeIds", bikeId)).first();
        if (station == null) {
            PopupScreen.error(new InvalidIdException().getMessage());
            return null;
        }
        return Utils.documentToObject(station, Station.class);
    }

    /**
     * Connect to the database to retrieve the information of all the station that is in the location.
     *
     * @param location location of the stations that need to be retrieved
     * @return List of {@link org.hust.entity.station.Station Station} that is in that location
     */
    @SneakyThrows
    public static List<Station> getStationByLocation(String location) {
        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> stationCollection = db.getCollection("stations");
        MongoCursor<Document> stations = stationCollection.find(new Document("location", location)).iterator();
        if (!stations.hasNext()) {
            PopupScreen.error(new InvalidLocationException().getMessage());
            return null;
        }
        List<Station> result = new LinkedList<>();
        while (stations.hasNext()) {
            result.add(Utils.documentToObject(stations.next(), Station.class));
        }
        return result;
    }

    /**
     * Connect to the database to retrieve the information of every station.
     *
     * @return List of all {@link org.hust.entity.station.Station Station}
     */
    public static List<Station> listAllStation() {
        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> stationCollection = db.getCollection("stations");
        MongoCursor<Document> stations = stationCollection.find().iterator();

        List<Station> result = new ArrayList<>();
        while (stations.hasNext()) {
            result.add(Utils.documentToObject(stations.next(), Station.class));
        }

        return result;
    }

    /**
     * Get list of all bikes that is in the station.
     *
     * @return List of {@link org.hust.entity.bike.Bike Bike} that is in the station
     */
    public List<Bike> getStationBikes() {
        List<Bike> result = new ArrayList<>();
        for (String id : bikeIds) {
            result.add(Bike.getBikeById(id));
        }

        return result;
    }

    /**
     * Check if the station is full or not.
     *
     * @return true - if the station is full <p>
     * false - otherwise
     */
    public boolean isFull() {
        return CAPACITY == bikeIds.size();
    }

    /**
     * Get the number of empty spots in the station.
     *
     * @return number of empty spots
     */
    public int getEmptyDocksCount() {
        return CAPACITY - bikeIds.size();
    }

    @Override
    public String toString() {
        return "Station at: " + this.location;
    }
}
