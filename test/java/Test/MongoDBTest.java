package Test;

import com.mongodb.MongoWriteException;
import model.Car;
import model.ClientAddress;
import model.Rent;
import model.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import storage.ClientMongoRepository;
import storage.RentMongoRepository;
import storage.VehicleMongoRepository;

public class MongoDBTest {

    @Test
    public void addRemoveClientTest() {
        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);

        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        clientMongoRepository.addClient(clientAddress);
        Assertions.assertNotNull(clientMongoRepository.getClient(1));

        clientMongoRepository.removeClient(1);

        Assertions.assertNull(clientMongoRepository.getClient(1));

        clientMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void modifyClientTest() {
        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);

        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        clientMongoRepository.addClient(clientAddress);
        Assertions.assertEquals(clientMongoRepository.getClient(1).getFirstName(), "Pablo");

        clientMongoRepository.updateClient(1, "firstName", "olbaP");

        Assertions.assertEquals(clientMongoRepository.getClient(1).getFirstName(), "olbaP");

        clientMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void addRemoveVehicleTest() {
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);

        VehicleMongoRepository vehicleMongoRepository = new VehicleMongoRepository();
        vehicleMongoRepository.addVehicle(vehicle);
        Assertions.assertNotNull(vehicleMongoRepository.getVehicle("1"));

        vehicleMongoRepository.removeVehicle("1");

        Assertions.assertNull(vehicleMongoRepository.getVehicle("1"));

        vehicleMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void modifyVehicleTest() {
        Vehicle car = new Car("1", 100, "black", 10000.50, 0, 5);

        VehicleMongoRepository vehicleMongoRepository = new VehicleMongoRepository();
        vehicleMongoRepository.addVehicle(car);
        Assertions.assertEquals(vehicleMongoRepository.getVehicle("1").getColor(), "black");

        vehicleMongoRepository.updateVehicle("1", "color", "red");

        Assertions.assertEquals(vehicleMongoRepository.getVehicle("1").getColor(), "red");
        vehicleMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void vehicleSchemaTest() {
        Vehicle car1 = new Car("1", 100, "black", 10000.50, 0, 5);
        Vehicle car2 = new Car("2", 100, "black", 10000.50, 1, 5);
        Vehicle car3 = new Car("3", 100, "black", 10000.50, 2, 5);
        VehicleMongoRepository vehicleMongoRepository = new VehicleMongoRepository();

        Assertions.assertDoesNotThrow(() -> {
            vehicleMongoRepository.addVehicle(car1);
            vehicleMongoRepository.addVehicle(car2);
        });

        Assertions.assertThrows(MongoWriteException.class, () -> {
            vehicleMongoRepository.addVehicle(car3);
        });

        vehicleMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void addRemoveRentTest() {
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        VehicleMongoRepository vehicleMongoRepository = new VehicleMongoRepository();
        RentMongoRepository rentMongoRepository = new RentMongoRepository(clientMongoRepository, vehicleMongoRepository);

        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);
        Rent rent = new Rent(3, clientAddress, vehicle);

        clientMongoRepository.addClient(clientAddress);
        vehicleMongoRepository.addVehicle(vehicle);
        rentMongoRepository.addRent(rent);

        Rent retrivedRent = rentMongoRepository.getRent(3);

        Assertions.assertEquals(rent.getId(), retrivedRent.getId());
        Assertions.assertEquals(retrivedRent.getClient().getFirstName(), clientAddress.getFirstName());
        Assertions.assertEquals(retrivedRent.getVehicle().getId(), vehicle.getId());

        clientMongoRepository.getMongoDatabase().drop();
        vehicleMongoRepository.getMongoDatabase().drop();
        rentMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void modifyRentTest() {
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        VehicleMongoRepository vehicleMongoRepository = new VehicleMongoRepository();
        RentMongoRepository rentMongoRepository = new RentMongoRepository(clientMongoRepository, vehicleMongoRepository);

        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);
        Rent rent = new Rent(1, clientAddress, vehicle);

        clientMongoRepository.addClient(clientAddress);
        vehicleMongoRepository.addVehicle(vehicle);
        rentMongoRepository.addRent(rent);

        Rent retrivedRent = rentMongoRepository.getRent(1);

        Assertions.assertEquals(rent.getId(), retrivedRent.getId());
        Assertions.assertEquals(retrivedRent.getClient().getFirstName(), clientAddress.getFirstName());
        Assertions.assertEquals(retrivedRent.getVehicle().getId(), vehicle.getId());

        rentMongoRepository.endRent(1);

        Rent modifiedRetrivedRent = rentMongoRepository.getRent(1);
        Assertions.assertTrue(modifiedRetrivedRent.isArchive());

        clientMongoRepository.getMongoDatabase().drop();
        vehicleMongoRepository.getMongoDatabase().drop();
        rentMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void addRemoveEndRentTest() {
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        VehicleMongoRepository vehicleMongoRepository = new VehicleMongoRepository();
        RentMongoRepository rentMongoRepository = new RentMongoRepository(clientMongoRepository, vehicleMongoRepository);

        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);
        Rent rent1 = new Rent(1, clientAddress, vehicle);

        clientMongoRepository.addClient(clientAddress);
        vehicleMongoRepository.addVehicle(vehicle);
        rentMongoRepository.addRent(rent1);

        Assertions.assertNotNull(rentMongoRepository.getRent(1));
        Assertions.assertFalse(rentMongoRepository.getRent(1).isArchive());
        Assertions.assertEquals(1, vehicleMongoRepository.getVehicle("1").isRented());
        Assertions.assertEquals(1, clientMongoRepository.getClient(1).getNoRents());


        // End rent first
        rentMongoRepository.endRent(1);

        Assertions.assertNotNull(rentMongoRepository.getRent(1));
        Assertions.assertTrue(rentMongoRepository.getRent(1).isArchive());
        Assertions.assertEquals(0, vehicleMongoRepository.getVehicle("1").isRented());
        Assertions.assertEquals(0, clientMongoRepository.getClient(1).getNoRents());

        rentMongoRepository.removeRent(1);

        Assertions.assertNull(rentMongoRepository.getRent(1));

        // Remove rent first
        rentMongoRepository.addRent(rent1);

        Assertions.assertNotNull(rentMongoRepository.getRent(1));
        Assertions.assertFalse(rentMongoRepository.getRent(1).isArchive());
        Assertions.assertEquals(1, vehicleMongoRepository.getVehicle("1").isRented());
        Assertions.assertEquals(1, clientMongoRepository.getClient(1).getNoRents());

        rentMongoRepository.removeRent(1);

        Assertions.assertNull(rentMongoRepository.getRent(1));
        Assertions.assertEquals(0, vehicleMongoRepository.getVehicle("1").isRented());
        Assertions.assertEquals(0, clientMongoRepository.getClient(1).getNoRents());

        clientMongoRepository.getMongoDatabase().drop();
        vehicleMongoRepository.getMongoDatabase().drop();
        rentMongoRepository.getMongoDatabase().drop();
    }

    @Test
    public void rentVehicleTwiceTest() {
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        VehicleMongoRepository vehicleMongoRepository = new VehicleMongoRepository();
        RentMongoRepository rentMongoRepository = new RentMongoRepository(clientMongoRepository, vehicleMongoRepository);

        ClientAddress clientAddress1 = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        ClientAddress clientAddress2 = new ClientAddress(2, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);

        Rent rent1 = new Rent(1, clientAddress1, vehicle);
        Rent rent2 = new Rent(2, clientAddress2, vehicle);

        clientMongoRepository.addClient(clientAddress2);
        vehicleMongoRepository.addVehicle(vehicle);
        rentMongoRepository.addRent(rent1);
        rentMongoRepository.addRent(rent2);

        Assertions.assertNull(rentMongoRepository.getRent(2));

        clientMongoRepository.getMongoDatabase().drop();
        vehicleMongoRepository.getMongoDatabase().drop();
        rentMongoRepository.getMongoDatabase().drop();
    }


    @Test
    public void maxNoOfVehicleRentTest() {
        ClientMongoRepository clientMongoRepository = new ClientMongoRepository();
        VehicleMongoRepository vehicleMongoRepository = new VehicleMongoRepository();
        RentMongoRepository rentMongoRepository = new RentMongoRepository(clientMongoRepository, vehicleMongoRepository);

        ClientAddress clientAddress = new ClientAddress(1, "Pablo", "Escobar", "Uliczna", 1, "Mehiko", 121212);
        Vehicle vehicle = new Car("1", 100, "Pink", 0.0000001, 0, 15);
        Vehicle vehicle2 = new Car("2", 100, "Pink", 0.0000001, 0, 15);
        Vehicle vehicle3 = new Car("3", 100, "Pink", 0.0000001, 0, 15);

        Rent rent1 = new Rent(1, clientAddress, vehicle);
        Rent rent2 = new Rent(2, clientAddress, vehicle2);
        Rent rent3 = new Rent(3, clientAddress, vehicle3);

        clientMongoRepository.addClient(clientAddress);
        vehicleMongoRepository.addVehicle(vehicle);
        vehicleMongoRepository.addVehicle(vehicle2);
        vehicleMongoRepository.addVehicle(vehicle3);

        rentMongoRepository.addRent(rent1);
        rentMongoRepository.addRent(rent2);
        rentMongoRepository.addRent(rent3);

        Assertions.assertNotNull(rentMongoRepository.getRent(1));
        Assertions.assertNotNull(rentMongoRepository.getRent(2));
        Assertions.assertNull(rentMongoRepository.getRent(3));

        clientMongoRepository.getMongoDatabase().drop();
        vehicleMongoRepository.getMongoDatabase().drop();
        rentMongoRepository.getMongoDatabase().drop();
    }
}
