package org.hust.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.hust.entity.bike.Bike;
import org.hust.entity.bike.EBike;
import org.hust.entity.bike.NormalBike;
import org.hust.entity.bike.TwinBike;
import org.hust.entity.db.Database;
import org.hust.entity.station.Station;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * DO NOT RUN THIS CLASS ARBITRARILY
 * this class is use to inject data into the database for testing purpose
 *
 * @author hoang.lh
 */
public class DataInjector {
    private static final String[] bikeUrl = new String[3];

    static {
        //bike
        bikeUrl[0] = "https://cdn.shopify.com/s/files/1/2081/1519/products/1600x1067_US_B_Mint_PROFILE-X2.jpg?v=1590502980";
        //ebike
        bikeUrl[1] = "https://cdn.shopify.com/s/files/1/1009/9108/products/HyperScrambler-Alum-RtSide_600x400_crop_center.jpg?v=1599144366";
        //twinbike
        bikeUrl[2] = "https://coynecycles.ie/wp-content/uploads/2020/02/Dawes-Discovery-Twin-web.jpg";
    }

    /**
     * inject Bike to bikes collection
     */
    public static void injectBike() {
        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> bikeCollection = db.getCollection("bikes");

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int ran = random.nextInt(10);
            Bike bike;
            if (ran < 3) {
                bike = new EBike("model" + i, true, random.nextDouble(),
                        "color" + i, random.nextDouble(), "" + (char) (random.nextInt(26) + 'a'),
                        random.nextInt(20) * 1000000, "" + (char) (random.nextInt(26) + 'a'),
                        bikeUrl[1], random.nextInt(100));
            } else if (ran < 5) {
                bike = new TwinBike("model" + i, true, random.nextDouble(),
                        "color" + i, random.nextDouble(), "" + (char) (random.nextInt(26) + 'a'),
                        random.nextInt(20) * 1000000, "" + (char) (random.nextInt(26) + 'a'),
                        bikeUrl[2]);
            } else {
                bike = new NormalBike("model" + i, true, random.nextDouble(),
                        "color" + i, random.nextDouble(), "" + (char) (random.nextInt(26) + 'a'),
                        random.nextInt(20) * 1000000, "" + (char) (random.nextInt(26) + 'a'),
                        bikeUrl[2]);
            }
            Document bikeDocument = Utils.objectToDocument(bike);
            bikeCollection.insertOne(bikeDocument);
        }
    }

    /**
     * Inject a random list of station to the database.
     *
     * @throws ClassNotFoundException if class not found
     * @throws NoSuchMethodException if there are no method suitable
     * @throws InvocationTargetException if the target is invoked
     * @throws InstantiationException if try to create new instance of a class that can not be instantiated
     * @throws IllegalAccessException if try to access illegal field or method
     */
    public static void injectSation() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        MongoDatabase db = Database.getConnection();
        MongoCollection<Document> bikeCollection = db.getCollection("stations");
        Station[] stations = new Station[4];
        List<String> bikes1 = new LinkedList();
        List<String> bikes2 = new LinkedList();
        List<String> bikes3 = new LinkedList();
        List<String> bikes0 = new LinkedList();

        Random random = new Random();
        Reflections reflections = new Reflections(Bike.class);
        Set<Class<? extends Bike>> list = reflections.getSubTypesOf(Bike.class);
        for (Document bike : db.getCollection("bikes").find()) {
            Bike bike1;
            Class bikeType = Class.forName(bike.getString("bikeType"));

            for (Class<? extends Bike> c : list) {
                if (bikeType.equals(c)) {
                    bike1 = (c.getDeclaredConstructor().newInstance()).documentToBike(bike);
                    switch (random.nextInt(4)) {
                        case 0:
                            bikes0.add(bike1.get_id().toHexString());
                            break;
                        case 1:
                            bikes1.add(bike1.get_id().toHexString());
                            break;
                        case 2:
                            bikes2.add(bike1.get_id().toHexString());
                            break;
                        default:
                            bikes3.add(bike1.get_id().toHexString());
                            break;
                    }
                }
            }


        }
        stations[0] = new Station("location" + 0, bikes0);
        stations[1] = new Station("location" + 1, bikes1);
        stations[2] = new Station("location" + 2, bikes2);
        stations[3] = new Station("location" + 3, bikes3);
        Document[] documents = new Document[4];
        for (int i = 0; i < 4; i++) {
            documents[i] = Utils.objectToDocument(stations[i]);
            bikeCollection.insertOne(documents[i]);
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        DataInjector.injectBike();
        DataInjector.injectSation();
    }
}
