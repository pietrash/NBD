package storage;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.ClientDao;
import mapper.ClientMapper;
import mapper.ClientMapperBuilder;
import model.ClientAddress;

public class ClientCassandraRepository extends AbstractCassandraRepository {
    private final ClientDao clientDao;

    public ClientCassandraRepository() {
        initSession();
        createTable();
        ClientMapper clientMapper = new ClientMapperBuilder(getSession()).build();
        clientDao = clientMapper.clientDao(CqlIdentifier.fromCql("rent_a_vehicle"), "clients");
    }

    private void createTable() {
        SimpleStatement createAccounts =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("city"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("firstName"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("lastName"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("noRents"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("street"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("streetNumber"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("postcode"), DataTypes.INT).build();
        getSession().execute(createAccounts);
    }

    public void addClient(ClientAddress client) {
        clientDao.save(client);
    }

    public void deleteClient(int id) {
        clientDao.deleteById(id);
    }

    public void updateClient(ClientAddress client) {
        clientDao.save(client);
    }

    public ClientAddress getClient(int id) {
        return clientDao.findById(id);
    }

}