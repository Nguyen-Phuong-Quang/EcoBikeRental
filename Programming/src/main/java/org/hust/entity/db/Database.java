package org.hust.entity.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * @author hoang.lh194766
 * <p>
 * Database of the project.
 */
public class Database {

    private static final MongoDatabase DATABASE;

    static {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://Group_7_User:ahaha123@cluster0.hpjzw.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        DATABASE = mongoClient.getDatabase("CapstoneProject");
    }

    /**
     * Get the connection to the database of EcoBikeRentalService software.
     *
     * @return the connection to the database
     */
    public static MongoDatabase getConnection() {
        return DATABASE;
    }

}
