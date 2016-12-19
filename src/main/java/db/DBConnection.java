package db;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiyyanp on 10/4/2016.
 */
public class DBConnection {

    private final String url;
    private final String user;
    private final String pass;
    private final String dbName;
    private final Boolean isAuth;
    private  String collectionName;
    private MongoClient mongoClient;
    private MongoDatabase db;
    public MongoCollection<Document> collection;


    public DBConnection(final String mongoDBUrl, Boolean auth, final String user, final String pass, final String dbName) {
        //mongoClient = new MongoClient(mongoDBUrl, 27017);
        //db = mongoClient.getDatabase(dbName);

        this.url = mongoDBUrl;
        this.isAuth = auth;
        this.user = user;
        this.pass = pass;
        this.dbName = dbName;
    }

    public void connect(){
        ServerAddress serverAddress = new ServerAddress(url, 27017);
        if(isAuth){
            MongoCredential credential = MongoCredential.createPlainCredential(user, dbName, pass.toCharArray());
            List<MongoCredential> auths = new ArrayList<MongoCredential>();
            auths.add(credential);
            mongoClient = new MongoClient(serverAddress, auths);
        }else{
            mongoClient = new MongoClient(serverAddress);
        }

        db = mongoClient.getDatabase(dbName);


    }

    public void disconnect(){
        mongoClient.close();
    }

    public void getCollection(String colName){
        collection = db.getCollection(colName);
    }
}

