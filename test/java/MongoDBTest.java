import com.mongodb.MongoWriteException;
import model.Car;
import model.ClientAddress;
import model.Rent;
import model.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import storage.ClientRepository;
import storage.RentRepository;
import storage.VehicleRepository;

public class MongoDBTest {

    @Test
    public void addRemoveClientTest() {
        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);

        ClientRepository clientRepository = new ClientRepository();
        clientRepository.addClient(clientAddress);
        Assertions.assertNotNull(clientRepository.getClient(1));

        clientRepository.removeClient(1);

        Assertions.assertNull(clientRepository.getClient(1));

        clientRepository.getMongoDatabase().drop();
    }

    @Test
    public void modifyClientTest() {
        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);

        ClientRepository clientRepository = new ClientRepository();
        clientRepository.addClient(clientAddress);
        Assertions.assertEquals(clientRepository.getClient(1).getFirstName(), "Pablo");

        clientRepository.updateClient(1, "firstName", "olbaP");

        Assertions.assertEquals(clientRepository.getClient(1).getFirstName(), "olbaP");

        clientRepository.getMongoDatabase().drop();
    }

    @Test
    public void addRemoveVehicleTest() {
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);

        VehicleRepository vehicleRepository = new VehicleRepository();
        vehicleRepository.addVehicle(vehicle);
        Assertions.assertNotNull(vehicleRepository.getVehicle("1"));

        vehicleRepository.removeVehicle("1");

        Assertions.assertNull(vehicleRepository.getVehicle("1"));

        vehicleRepository.getMongoDatabase().drop();
    }

    @Test
    public void modifyVehicleTest() {
        Vehicle car = new Car("1", 100, "black", 10000.50, 0, 5);

        VehicleRepository vehicleRepository = new VehicleRepository();
        vehicleRepository.addVehicle(car);
        Assertions.assertEquals(vehicleRepository.getVehicle("1").getColor(), "black");

        vehicleRepository.updateVehicle("1", "color", "red");

        Assertions.assertEquals(vehicleRepository.getVehicle("1").getColor(), "red");
        vehicleRepository.getMongoDatabase().drop();
    }

    @Test
    public void vehicleSchemaTest() {
        Vehicle car1 = new Car("1", 100, "black", 10000.50, 0, 5);
        Vehicle car2 = new Car("2", 100, "black", 10000.50, 1, 5);
        Vehicle car3 = new Car("3", 100, "black", 10000.50, 2, 5);
        VehicleRepository vehicleRepository = new VehicleRepository();

        Assertions.assertDoesNotThrow(() -> {
            vehicleRepository.addVehicle(car1);
            vehicleRepository.addVehicle(car2);
        });

        Assertions.assertThrows(MongoWriteException.class, () -> {
            vehicleRepository.addVehicle(car3);
        });

        vehicleRepository.getMongoDatabase().drop();
    }

    @Test
    public void addRemoveRentTest() {
        ClientRepository clientRepository = new ClientRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();
        RentRepository rentRepository = new RentRepository(clientRepository, vehicleRepository);

        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);
        Rent rent = new Rent(3, clientAddress, vehicle);

        clientRepository.addClient(clientAddress);
        vehicleRepository.addVehicle(vehicle);
        rentRepository.addRent(rent);

        Rent retrivedRent = rentRepository.getRent(3);

        Assertions.assertEquals(rent.getId(), retrivedRent.getId());
        Assertions.assertEquals(retrivedRent.getClient().getFirstName(), clientAddress.getFirstName());
        Assertions.assertEquals(retrivedRent.getVehicle().getId(), vehicle.getId());

        clientRepository.getMongoDatabase().drop();
        vehicleRepository.getMongoDatabase().drop();
        rentRepository.getMongoDatabase().drop();
    }

    @Test
    public void modifyRentTest() {
        ClientRepository clientRepository = new ClientRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();
        RentRepository rentRepository = new RentRepository(clientRepository, vehicleRepository);

        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);
        Rent rent = new Rent(1, clientAddress, vehicle);

        clientRepository.addClient(clientAddress);
        vehicleRepository.addVehicle(vehicle);
        rentRepository.addRent(rent);

        Rent retrivedRent = rentRepository.getRent(1);

        Assertions.assertEquals(rent.getId(), retrivedRent.getId());
        Assertions.assertEquals(retrivedRent.getClient().getFirstName(), clientAddress.getFirstName());
        Assertions.assertEquals(retrivedRent.getVehicle().getId(), vehicle.getId());

        rentRepository.endRent(1);

        Rent modifiedRetrivedRent = rentRepository.getRent(1);
        Assertions.assertTrue(modifiedRetrivedRent.isArchive());

        clientRepository.getMongoDatabase().drop();
        vehicleRepository.getMongoDatabase().drop();
        rentRepository.getMongoDatabase().drop();
    }

    @Test
    public void addRemoveEndRentTest() {
        ClientRepository clientRepository = new ClientRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();
        RentRepository rentRepository = new RentRepository(clientRepository, vehicleRepository);

        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);
        Rent rent1 = new Rent(1, clientAddress, vehicle);

        clientRepository.addClient(clientAddress);
        vehicleRepository.addVehicle(vehicle);
        rentRepository.addRent(rent1);

        Assertions.assertNotNull(rentRepository.getRent(1));
        Assertions.assertFalse(rentRepository.getRent(1).isArchive());
        Assertions.assertEquals(1, vehicleRepository.getVehicle("1").isRented());
        Assertions.assertEquals(1, clientRepository.getClient(1).getNoRents());


        // End rent first
        rentRepository.endRent(1);

        Assertions.assertNotNull(rentRepository.getRent(1));
        Assertions.assertTrue(rentRepository.getRent(1).isArchive());
        Assertions.assertEquals(0, vehicleRepository.getVehicle("1").isRented());
        Assertions.assertEquals(0, clientRepository.getClient(1).getNoRents());

        rentRepository.removeRent(1);

        Assertions.assertNull(rentRepository.getRent(1));

        // Remove rent first
        rentRepository.addRent(rent1);

        Assertions.assertNotNull(rentRepository.getRent(1));
        Assertions.assertFalse(rentRepository.getRent(1).isArchive());
        Assertions.assertEquals(1, vehicleRepository.getVehicle("1").isRented());
        Assertions.assertEquals(1, clientRepository.getClient(1).getNoRents());

        rentRepository.removeRent(1);

        Assertions.assertNull(rentRepository.getRent(1));
        Assertions.assertEquals(0, vehicleRepository.getVehicle("1").isRented());
        Assertions.assertEquals(0, clientRepository.getClient(1).getNoRents());

        clientRepository.getMongoDatabase().drop();
        vehicleRepository.getMongoDatabase().drop();
        rentRepository.getMongoDatabase().drop();
    }

    @Test
    public void rentVehicleTwiceTest() {
        ClientRepository clientRepository = new ClientRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();
        RentRepository rentRepository = new RentRepository(clientRepository, vehicleRepository);

        ClientAddress clientAddress1 = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        ClientAddress clientAddress2 = new ClientAddress(2, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);

        Rent rent1 = new Rent(1, clientAddress1, vehicle);
        Rent rent2 = new Rent(2, clientAddress2, vehicle);

        clientRepository.addClient(clientAddress2);
        vehicleRepository.addVehicle(vehicle);
        rentRepository.addRent(rent1);
        rentRepository.addRent(rent2);

        Assertions.assertNull(rentRepository.getRent(2));

        clientRepository.getMongoDatabase().drop();
        vehicleRepository.getMongoDatabase().drop();
        rentRepository.getMongoDatabase().drop();
    }


    @Test
    public void maxNoOfVehicleRentTest() {
        ClientRepository clientRepository = new ClientRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();
        RentRepository rentRepository = new RentRepository(clientRepository, vehicleRepository);

        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);
        Vehicle vehicle2 = new Car("2", 100, "Pink", 0.0000001, 0, 15);
        Vehicle vehicle3 = new Car("3", 100, "Pink", 0.0000001, 0, 15);

        Rent rent1 = new Rent(1, clientAddress, vehicle);
        Rent rent2 = new Rent(2, clientAddress, vehicle2);
        Rent rent3 = new Rent(3, clientAddress, vehicle3);

        clientRepository.addClient(clientAddress);
        vehicleRepository.addVehicle(vehicle);
        vehicleRepository.addVehicle(vehicle2);
        vehicleRepository.addVehicle(vehicle3);

        rentRepository.addRent(rent1);
        rentRepository.addRent(rent2);
        rentRepository.addRent(rent3);

        Assertions.assertNotNull(rentRepository.getRent(1));
        Assertions.assertNotNull(rentRepository.getRent(2));
        Assertions.assertNull(rentRepository.getRent(3));

        clientRepository.getMongoDatabase().drop();
        vehicleRepository.getMongoDatabase().drop();
        rentRepository.getMongoDatabase().drop();
    }
}
