package model;

public class Rent extends RentDB {
    private ClientAddress client;
    private Vehicle vehicle;

    public Rent(int id, ClientAddress client, Vehicle vehicle) {
        this.id = id;
        this.client = client;
        this.vehicle = vehicle;
    }

    public Rent() {
    }

    public ClientAddress getClient() {
        return client;
    }

    public void setClient(ClientAddress client) {
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
