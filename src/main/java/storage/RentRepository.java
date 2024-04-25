package storage;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.ClientAddress;
import model.Rent;
import model.Vehicle;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class RentRepository extends AbstractMongoRepository {

    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;

    public RentRepository(ClientRepository clientRepository, VehicleRepository vehicleRepository) {
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;

        initDbConnection();
        if (!getMongoDatabase().listCollectionNames().into(new ArrayList()).contains("rents")) {
            initCollection();
        }
    }

    public void initCollection() {
        getMongoDatabase().createCollection("rents");
    }

    public Rent getRent(int id) {
        MongoCollection<Document> rentCollection = getMongoDatabase().getCollection("rents");
        Bson filter = Filters.and(
                Filters.eq("_id", id)
        );
        ArrayList<Document> rentArrayList = rentCollection.find(filter).into(new ArrayList<>());
        if (rentArrayList.isEmpty()) return null;
        return fromMongoDocument(rentArrayList.get(0));
    }

    public void addRent(Rent rent) {
        MongoCollection<Document> rentCollection = getMongoDatabase().getCollection("rents");
        ClientSession clientSession = getMongoClient().startSession();
        try {
            clientSession.startTransaction();

            // Vehicle
            MongoCollection<Vehicle> vehiclesCollection =
                    getMongoDatabase().getCollection("vehicles", Vehicle.class);
            Bson vehicleFilter = Filters.eq("_id", rent.getVehicle().getId());
            Bson vehicleUpdate = Updates.inc("rented", 1);
            vehiclesCollection.updateOne(vehicleFilter, vehicleUpdate);

            // Client
            MongoCollection<ClientAddress> clientCollection =
                    getMongoDatabase().getCollection("clients", ClientAddress.class);
            Bson clientFilter = Filters.eq("_id", rent.getClient().getPersonalId());
            Bson clientUpdate = Updates.inc("noRents", 1);
            clientCollection.updateOne(clientFilter, clientUpdate);

            rentCollection.insertOne(toMongoDocument(rent));

            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
        } finally {
            clientSession.close();
        }
    }

    public void endRent(int id) {
        MongoCollection<Document> rentCollection = getMongoDatabase().getCollection("rents");
        if (rentCollection.countDocuments() == 0) return;

        Bson filter = Filters.eq("_id", id);
        Bson setUpdate = Updates.set("archive", true);
        rentCollection.updateOne(filter, setUpdate);

        // Vehicle
        MongoCollection<Vehicle> vehiclesCollection =
                getMongoDatabase().getCollection("vehicles", Vehicle.class);
        Bson vehicleFilter = Filters.eq("_id", getRent(id).getVehicle().getId());
        Bson vehicleUpdate = Updates.set("rented", 0);
        vehiclesCollection.updateOne(vehicleFilter, vehicleUpdate);

        // Client
        MongoCollection<ClientAddress> clientCollection =
                getMongoDatabase().getCollection("clients", ClientAddress.class);
        Bson clientFilter = Filters.eq("_id", getRent(id).getClient().getPersonalId());
        Bson clientUpdate = Updates.inc("noRents", -1);
        clientCollection.updateOne(clientFilter, clientUpdate);
    }

    public void removeRent(int id) {
        if (!getRent(id).isArchive()) endRent(id);

        MongoCollection<Document> rentCollection = getMongoDatabase().getCollection("rents");

        Bson filter = Filters.eq("_id", id);
        rentCollection.findOneAndDelete(filter);
    }

    private Document toMongoDocument(Rent rent) {
        return new Document()
                .append("_id", rent.getId())
                .append("price", rent.getVehicle().getPrice())
                .append("archive", rent.isArchive())
                .append("clientid", rent.getClient().getPersonalId())
                .append("vehicleid", rent.getVehicle().getId());
    }

    private Rent fromMongoDocument(Document document) {
        ClientAddress client = clientRepository.getClient((int) document.get("clientid"));
        Vehicle vehicle = vehicleRepository.getVehicle(document.get("vehicleid").toString());

        return new Rent(client, vehicle, (int) document.get("_id"), (boolean) document.get("archive"));
    }

    @Override
    public void close() throws Exception {

    }
}