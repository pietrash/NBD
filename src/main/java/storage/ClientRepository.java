package storage;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;
import model.ClientAddress;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class ClientRepository extends AbstractMongoRepository {

    public ClientRepository() {
        initDbConnection();
        if (!getMongoDatabase().listCollectionNames().into(new ArrayList()).contains("clients")) initCollection();
    }

    public void initCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema:{
                                "bsonType": "object",
                                "properties": {
                                    "noRents": {
                                        "bsonType": "int",
                                        "minimum" : 0,
                                        "maximum" : 2
                                    }
                                }
                            }
                        }
                        """));
        CreateCollectionOptions createCollectionOptions =
                new CreateCollectionOptions().validationOptions(validationOptions);
        getMongoDatabase().createCollection("clients", createCollectionOptions);
    }

    public ClientAddress getClient(int personalId) {
        MongoCollection<ClientAddress> clientsCollection = getMongoDatabase().getCollection("clients", ClientAddress.class);
        Bson filter = Filters.eq("_id", personalId);
        ArrayList<ClientAddress> clientAddressArrayList = clientsCollection.find(filter).into(new ArrayList<>());
        if (clientAddressArrayList.isEmpty()) return null;
        return clientAddressArrayList.get(0);
    }

    public void addClient(ClientAddress client) {
        MongoCollection<ClientAddress> clientsCollection =
                getMongoDatabase().getCollection("clients", ClientAddress.class);
        clientsCollection.insertOne(client);
    }

    public boolean updateClient(int personalId, String fieldToUpdate, String value) {
        MongoCollection<ClientAddress> clientsCollection = getMongoDatabase().getCollection("clients", ClientAddress.class);
        if (clientsCollection.countDocuments() == 0) return false;

        Bson filter = Filters.eq("_id", personalId);
        Bson setUpdate = Updates.set(fieldToUpdate, value);
        clientsCollection.updateOne(filter, setUpdate);

        return true;
    }

    public void removeClient(int personalId) {
        MongoCollection<ClientAddress> clientsCollection =
                getMongoDatabase().getCollection("clients", ClientAddress.class);

        Bson filter = Filters.eq("_id", personalId);
        clientsCollection.findOneAndDelete(filter);
    }

    @Override
    public void close() throws Exception {

    }
}
