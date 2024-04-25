package storage;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;
import model.Vehicle;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class VehicleRepository extends AbstractMongoRepository {

    public VehicleRepository() {
        initDbConnection();
        if (!getMongoDatabase().listCollectionNames().into(new ArrayList()).contains("vehicles")) initCollection();
    }

    public void initCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema:{
                                "bsonType": "object",
                                "properties": {
                                    "rented": {
                                        "bsonType": "int",
                                        "minimum" : 0,
                                        "maximum" : 1
                                        "description": "must be 1 for rented and 0 for available"
                                    }
                                }
                            }
                        }
                        """));
        CreateCollectionOptions createCollectionOptions =
                new CreateCollectionOptions().validationOptions(validationOptions);
        getMongoDatabase().createCollection("vehicles", createCollectionOptions);
    }

    public Vehicle getVehicle(String id) {
        MongoCollection<Vehicle> vehicleCollection = getMongoDatabase().getCollection("vehicles", Vehicle.class);
        Bson filter = Filters.and(
                Filters.eq("_id", id)
        );
        ArrayList<Vehicle> vehicleArrayList = vehicleCollection.find(filter).into(new ArrayList<>());
        if (vehicleArrayList.isEmpty()) return null;
        return vehicleArrayList.get(0);
    }

    public void addVehicle(Vehicle vehicle) {
        MongoCollection<Vehicle> vehicleCollection =
                getMongoDatabase().getCollection("vehicles", Vehicle.class);
        vehicleCollection.insertOne(vehicle);
    }

    public boolean updateVehicle(String id, String fieldToUpdate, String value) {
        MongoCollection<Vehicle> vehicleCollection = getMongoDatabase().getCollection("vehicles", Vehicle.class);
        if (vehicleCollection.countDocuments() == 0) return false;

        Bson filter = Filters.eq("_id", id);
        Bson setUpdate = Updates.set(fieldToUpdate, value);
        vehicleCollection.updateOne(filter, setUpdate);

        return true;
    }

    public void removeVehicle(String id) {
        MongoCollection<Vehicle> vehicleCollection = getMongoDatabase().getCollection("vehicles", Vehicle.class);

        Bson filter = Filters.eq("_id", id);
        vehicleCollection.findOneAndDelete(filter);
    }

    public void setRented(String vehicleId, boolean status) {

    }

    @Override
    public void close() throws Exception {

    }
}