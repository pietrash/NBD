package Test;

import managers.RentManager;
import model.Bicycle;
import model.Car;
import model.ClientAddress;
import model.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import storage.ClientCassandraRepository;
import storage.RentCassandraRepository;
import storage.VehicleCassandraRepository;

public class CassandraTest {
    // Client
    @Test
    public void addClientTest() {
        ClientAddress client1 = new ClientAddress(1, "Mariusz", "Pudzianowski", 0, "Pudzianowksa", 1, "Pudzianów", 12345);
        ClientCassandraRepository clientCassandraRepository = new ClientCassandraRepository();

        clientCassandraRepository.addClient(client1);
        Assertions.assertEquals(client1.getPersonalId(), clientCassandraRepository.getClient(1).getPersonalId());

        clientCassandraRepository.deleteClient(1);
    }

    @Test
    public void removeClientTest() {
        ClientAddress client1 = new ClientAddress(1, "Mariusz", "Pudzianowski", 0, "Pudzianowksa", 1, "Pudzianów", 12345);
        ClientCassandraRepository clientCassandraRepository = new ClientCassandraRepository();

        clientCassandraRepository.addClient(client1);
        Assertions.assertEquals(client1.getPersonalId(), clientCassandraRepository.getClient(1).getPersonalId());

        clientCassandraRepository.deleteClient(1);

        Assertions.assertNull(clientCassandraRepository.getClient(1));
    }

    @Test
    public void updateClientTest() {
        ClientAddress client1 = new ClientAddress(1, "Mariusz", "Pudzianowski", 0, "Pudzianowksa", 1, "Pudzianów", 12345);
        ClientCassandraRepository clientCassandraRepository = new ClientCassandraRepository();

        clientCassandraRepository.addClient(client1);
        Assertions.assertEquals(client1.getPersonalId(), clientCassandraRepository.getClient(1).getPersonalId());

        ClientAddress client2 = new ClientAddress(1, "Marian", "Pudzianowski", 0, "Pudzianowksa", 1, "Pudzianów", 12345);
        clientCassandraRepository.updateClient(client2);

        Assertions.assertEquals(clientCassandraRepository.getClient(1).getFirstName(), client2.getFirstName());

        clientCassandraRepository.deleteClient(1);
    }

    // Vehicle
    @Test
    public void addVehicleTest() {
        Vehicle car1 = new Car("1", 300, "black", 20000, 0, 5, "car");
        VehicleCassandraRepository vehicleCassandraRepository = new VehicleCassandraRepository();

        vehicleCassandraRepository.addVehicle(car1);
        Assertions.assertEquals(car1.getId(), vehicleCassandraRepository.getVehicle("1").getId());

        vehicleCassandraRepository.deleteVehicle("1");
    }

    @Test
    public void removeVehicleTest() {
        Vehicle car1 = new Car("1", 300, "black", 20000, 0, 5, "car");
        VehicleCassandraRepository vehicleCassandraRepository = new VehicleCassandraRepository();

        vehicleCassandraRepository.addVehicle(car1);
        Assertions.assertEquals(car1.getId(), vehicleCassandraRepository.getVehicle("1").getId());

        vehicleCassandraRepository.deleteVehicle("1");
        Assertions.assertThrows(NullPointerException.class, () -> {
            vehicleCassandraRepository.getVehicle("1");
        });
    }

    @Test
    public void updateVehicleTest() {
        Vehicle car1 = new Car("1", 300, "black", 20000, 0, 5, "car");
        VehicleCassandraRepository vehicleCassandraRepository = new VehicleCassandraRepository();

        vehicleCassandraRepository.addVehicle(car1);
        Assertions.assertEquals(car1.getId(), vehicleCassandraRepository.getVehicle("1").getId());

        Vehicle car2 = new Car("1", 300, "green", 20000, 0, 5, "car");
        vehicleCassandraRepository.updateVehicle(car2);

        Assertions.assertEquals(vehicleCassandraRepository.getVehicle("1").getColor(), car2.getColor());

        vehicleCassandraRepository.deleteVehicle("1");
    }

    // Rent
    @Test
    public void addRentTest() {
        ClientCassandraRepository clientCassandraRepository = new ClientCassandraRepository();
        VehicleCassandraRepository vehicleCassandraRepository = new VehicleCassandraRepository();
        RentCassandraRepository rentCassandraRepository = new RentCassandraRepository();

        RentManager rentManager = new RentManager(rentCassandraRepository, clientCassandraRepository, vehicleCassandraRepository);

        ClientAddress client = new ClientAddress(1, "Mariusz", "Pudzianowski", 0, "Pudzianowksa", 1, "Pudzianów", 12345);
        Car car = new Car("1", 800, "black", 10, 0, 5, "car");

        clientCassandraRepository.addClient(client);
        Assertions.assertNotNull(clientCassandraRepository.getClient(1));

        vehicleCassandraRepository.addVehicle(car);
        Assertions.assertNotNull(vehicleCassandraRepository.getVehicle("1"));

        rentManager.addRent(1, client, car);

        Assertions.assertNotNull(rentCassandraRepository.getRent(1));
        Assertions.assertEquals(1, rentManager.getRent(1).getClient().getNoRents());
        Assertions.assertEquals(1, rentManager.getRent(1).getVehicle().isRented());

        clientCassandraRepository.deleteClient(1);
        vehicleCassandraRepository.deleteVehicle("1");
        rentCassandraRepository.deleteRent(1);
    }

    @Test
    public void removeRentTest() {
        ClientCassandraRepository clientCassandraRepository = new ClientCassandraRepository();
        VehicleCassandraRepository vehicleCassandraRepository = new VehicleCassandraRepository();
        RentCassandraRepository rentCassandraRepository = new RentCassandraRepository();

        RentManager rentManager = new RentManager(rentCassandraRepository, clientCassandraRepository, vehicleCassandraRepository);

        ClientAddress client = new ClientAddress(1, "Mariusz", "Pudzianowski", 0, "Pudzianowksa", 1, "Pudzianów", 12345);
        Bicycle bicycle = new Bicycle("1", 800, "black", 10, true, 5, "bicycle");

        clientCassandraRepository.addClient(client);
        Assertions.assertNotNull(clientCassandraRepository.getClient(1));

        vehicleCassandraRepository.addVehicle(bicycle);
        Assertions.assertNotNull(vehicleCassandraRepository.getVehicle("1"));

        rentManager.addRent(1, client, bicycle);

        Assertions.assertNotNull(rentCassandraRepository.getRent(1));

        rentCassandraRepository.deleteRent(1);

        Assertions.assertNull(rentCassandraRepository.getRent(1));

        clientCassandraRepository.deleteClient(1);
        vehicleCassandraRepository.deleteVehicle("1");
    }

    @Test
    public void updateRentTest() {
        ClientCassandraRepository clientCassandraRepository = new ClientCassandraRepository();
        VehicleCassandraRepository vehicleCassandraRepository = new VehicleCassandraRepository();
        RentCassandraRepository rentCassandraRepository = new RentCassandraRepository();

        RentManager rentManager = new RentManager(rentCassandraRepository, clientCassandraRepository, vehicleCassandraRepository);

        ClientAddress client = new ClientAddress(1, "Mariusz", "Pudzianowski", 0, "Pudzianowksa", 1, "Pudzianów", 12345);
        Car car = new Car("1", 800, "black", 10, 0, 5, "car");

        clientCassandraRepository.addClient(client);
        Assertions.assertNotNull(clientCassandraRepository.getClient(1));

        vehicleCassandraRepository.addVehicle(car);
        Assertions.assertNotNull(vehicleCassandraRepository.getVehicle("1"));

        rentManager.addRent(1, client, car);

        Assertions.assertNotNull(rentCassandraRepository.getRent(1));
        Assertions.assertEquals(1, rentManager.getRent(1).getClient().getNoRents());
        Assertions.assertEquals(1, rentManager.getRent(1).getVehicle().isRented());

        rentManager.endRent(rentCassandraRepository.getRent(1));

        Assertions.assertNotNull(rentCassandraRepository.getRent(1));
        Assertions.assertTrue(rentCassandraRepository.getRent(1).isArchive());
        Assertions.assertEquals(0, rentManager.getRent(1).getClient().getNoRents());
        Assertions.assertEquals(0, rentManager.getRent(1).getVehicle().isRented());

        clientCassandraRepository.deleteClient(1);
        vehicleCassandraRepository.deleteVehicle("1");
        rentCassandraRepository.deleteRent(1);
    }

}
