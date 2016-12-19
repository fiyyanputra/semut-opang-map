import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import config.Config;
import db.DBConnection;
import model.Driver;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fiyyanp on 10/19/2016.
 */

@Path("map/session/")
public class MapResource {
    private Config configuration;
    final Locale id = new Locale("in", "ID");

    public MapResource(final Config configuration) {
        this.configuration = configuration;
    }

    @GET
    @Path("/test")
    public String Test(){
        System.out.println("test");
        String response = "{\"status\": true,\"message\":\"success\"}";
        return response;
    }

    @GET
    @Path("/find")
    public Response findById(@QueryParam("id_user") String idUser){
        //delete
        DBConnection db = new DBConnection(configuration.getMongoDBhost(), configuration.getMongoDBisAuth(), configuration.getMongoDBuser(), configuration.getMongoDBpassword(), configuration.getMongoDBname());
        db.connect();
        db.getCollection("session_map");
        Document searchQuery = new Document();
        searchQuery.append("id_user", idUser);

        String response = "";
        FindIterable<Document> result = db.collection.find(searchQuery);
        if(result == null){
            response = "{\"status\": false,\"message\":\"data tidak ditemukan\"}";
        }else{
            response = "{\"status\": true,\"message\":\"data ditemukan\"}";
        }

        db.disconnect();
        //end

        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/set")
    public Response Set(@FormParam("id_user") String idUser,
                      @FormParam("nama_user") String name,
                      @FormParam("lat_user") String latitude,
                      @FormParam("long_user") String longitude,
                      @FormParam("queue_name") String queueName){
        //store to db
        DBConnection db = new DBConnection(configuration.getMongoDBhost(), configuration.getMongoDBisAuth(), configuration.getMongoDBuser(), configuration.getMongoDBpassword(), configuration.getMongoDBname());
        db.connect();
        db.getCollection("session_map");

        List<Double> loc = new ArrayList<>();
        loc.add(Double.parseDouble(longitude));
        loc.add(Double.parseDouble(latitude));

        Document doc = new Document();
        doc.append("id_user", idUser)
                .append("nama_user", name)
                .append("queue_name", queueName)
                .append("loc", loc)
                .append("last_login", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", id).format(new Date()))
                .append("last_update", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", id).format(new Date()));

        db.collection.insertOne(doc);
        db.disconnect();
        //end

        String response = "{\"status\": true,\"message\":\"success\"}";
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/update")
    public Response Update(@FormParam("id_user") String idUser,
                       @FormParam("lat_user") String latitude,
                       @FormParam("long_user") String longitude){

        //update in db
        DBConnection db = new DBConnection(configuration.getMongoDBhost(), configuration.getMongoDBisAuth(), configuration.getMongoDBuser(), configuration.getMongoDBpassword(), configuration.getMongoDBname());
        db.connect();
        db.getCollection("session_map");
        Document searchQuery = new Document();
        searchQuery.append("id_user", idUser);

        List<Double> loc = new ArrayList<>();
        loc.add(Double.parseDouble(longitude));
        loc.add(Double.parseDouble(latitude));

        Document updateFields = new Document();
        updateFields.append("loc", loc);
        updateFields.append("last_update", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", id).format(new Date()));
        Document setQuery = new Document();
        setQuery.append("$set", updateFields);
        db.collection.updateMany(searchQuery, setQuery);
        db.disconnect();
        //end

        String response = "{\"status\": true,\"message\":\"success\"}";
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/unset")
    public Response Unset(@FormParam("id_user") String idUser){
        //delete
        DBConnection db = new DBConnection(configuration.getMongoDBhost(), configuration.getMongoDBisAuth(), configuration.getMongoDBuser(), configuration.getMongoDBpassword(), configuration.getMongoDBname());
        db.connect();
        db.getCollection("session_map");
        Document searchQuery = new Document();
        searchQuery.append("id_user", idUser);

        db.collection.deleteOne(searchQuery);
        db.disconnect();
        //end

        String response = "{\"status\": true,\"message\":\"success\"}";
        return Response.ok(response, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/drivers")
    public Response DriverByRadius(@FormParam("latitude") String latitude,
                                 @FormParam("longitude") String longitude,
                                 @FormParam("radius_km") String radius){

        List<Driver> drivers = new ArrayList<>();
        DBConnection db = new DBConnection(configuration.getMongoDBhost(), configuration.getMongoDBisAuth(), configuration.getMongoDBuser(), configuration.getMongoDBpassword(), configuration.getMongoDBname());
        db.connect();
        db.getCollection("session_map");

        List<Double> loc = new ArrayList<>();
        loc.add(Double.parseDouble(longitude));
        loc.add(Double.parseDouble(latitude));

        List<Object> param = new ArrayList<>();
        param.add(loc);
        param.add(Double.parseDouble(radius)/6371);

        Document center = new Document();
        center.append("$centerSphere", param);

        Document geo = new Document();
        geo.append("$geoWithin", center);

        Document searchQuery = new Document();
        searchQuery.append("loc", geo);

        FindIterable<Document> result = db.collection.find(searchQuery);

        if(result == null){
            String response = "{\"status\": false,\"message\":\"data tidak ditemukan\"}";
            db.disconnect();
            return Response.ok(response, MediaType.APPLICATION_JSON).build();

        }

        for (Document current : result) {
            Driver d = new Driver();
            d.setIdUser(current.getString("id_user"));
            d.setNamaUser(current.getString("nama_user"));
            d.setLongitude(Double.parseDouble(((ArrayList) current.get("loc")).get(0).toString()));
            d.setLatitude(Double.parseDouble(((ArrayList) current.get("loc")).get(1).toString()));
            d.setQueueName(current.getString("queue_name"));
            drivers.add(d);
        }

        db.disconnect();
        return Response.ok(drivers, MediaType.APPLICATION_JSON).build();
    }

}
