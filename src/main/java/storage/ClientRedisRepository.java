package storage;

import model.ClientAddress;


public class ClientRedisRepository extends AbstractRedisRepository {

    private final ClientMongoRepository clientMongoRepository;

    public ClientRedisRepository(ClientMongoRepository clientMongoRepository) {
        this.clientMongoRepository = clientMongoRepository;
        initDbConnection();
    }

    public ClientAddress getClient(int personalId) {
        try {
            checkConnection();
        } catch (Exception e) {
            return clientMongoRepository.getClient(personalId);
        }
        String temp = getPool().get("client:" + personalId);
        if (temp == null) return null;
        return getJsonb().fromJson(temp, ClientAddress.class);
    }

    public void addClient(ClientAddress client) {
        String temp = getJsonb().toJson(client);
        getPool().set("client:" + client.getPersonalId(), temp);
        getPool().expire("client:" + client.getPersonalId(), 5);
    }

    public void removeClient(int personalId) {
        getPool().del("client:" + personalId);
    }
}
