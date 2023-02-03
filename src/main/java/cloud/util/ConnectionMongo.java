package cloud.util;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;



public class ConnectionMongo {
	
	    public static MongoDatabase getMongoConnection() throws Exception {
            MongoDatabase database = null;
            try {
    
                ConnectionString connectionString = new ConnectionString("mongodb+srv://ericonomena:ericonomena@enchere.qf9jcj9.mongodb.net/?retryWrites=true&w=majority");
                MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
                MongoClient mongoClient = MongoClients.create(settings);
                database = mongoClient.getDatabase("enchere");
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
            return database;
	    }

}