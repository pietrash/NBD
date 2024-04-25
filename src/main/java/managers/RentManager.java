package managers;

import model.ClientAddress;
import model.Rent;
import model.RentDB;
import model.Vehicle;
import storage.ClientCassandraRepository;
import storage.RentCassandraRepository;
import storage.VehicleCassandraRepository;

public class RentManager {
    private final RentCassandraRepository rentCassandraRepository;
    private final ClientCassandraRepository clientCassandraRepository;
    private final VehicleCassandraRepository vehicleCassandraRepository;

    public RentManager(RentCassandraRepository rentCassandraRepository,
                       ClientCassandraRepository clientCassandraRepository,
                       VehicleCassandraRepository vehicleCassandraRepository) {
        this.rentCassandraRepository = rentCassandraRepository;
        this.clientCassandraRepository = clientCassandraRepository;
        this.vehicleCassandraRepository = vehicleCassandraRepository;
    }

    public void addRent(int id, ClientAddress client, Vehicle vehicle) {
        if (client.getNoRents() > 1) return;
        if (vehicle.isRented() == 1) return;

        client.setNoRents(client.getNoRents() + 1);
        vehicle.setRented(1);

        Rent rent = new Rent(id, client, vehicle);
        rentCassandraRepository.addRent(rent);
    }

    public void endRent(RentDB rentDB) {
        Rent rent = getRent(rentDB.getId());
        rent.getClient().setNoRents(rent.getClient().getNoRents() - 1);
        rent.getVehicle().setRented(0);
        rent.setArchive(true);
        rentCassandraRepository.updateRent(rent);
    }

    public Rent getRent(int id) {
        RentDB rentDB = rentCassandraRepository.getRent(id);
        Rent rent = new Rent();
        rent.setId(rentDB.getId());
        rent.setArchive(rentDB.isArchive());
        rent.setClient(clientCassandraRepository.getClient(rentDB.getClientId()));
        rent.setVehicle(vehicleCassandraRepository.getVehicle(rentDB.getVehicleId()));

        return rent;
    }

}
