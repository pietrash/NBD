package model;

public class Rent {
    private final ClientAddress client;
    private final Vehicle vehicle;
    private final int id;
    private boolean archive;

    public Rent(int id, ClientAddress client, Vehicle vehicle) {
        this.id = id;
        this.archive = false;
        this.client = client;
        this.vehicle = vehicle;
    }

    public Rent(ClientAddress client, Vehicle vehicle, int id, boolean archive) {
        this.client = client;
        this.vehicle = vehicle;
        this.id = id;
        this.archive = archive;
    }

    public int getId() {
        return id;
    }

    public ClientAddress getClient() {
        return client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }
}
