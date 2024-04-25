package storage;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.RentDao;
import mapper.RentMapper;
import mapper.RentMapperBuilder;
import model.Rent;
import model.RentDB;


public class RentCassandraRepository extends AbstractCassandraRepository {

    private final RentDao rentDao;

    public RentCassandraRepository() {
        initSession();
        createTable();
        RentMapper rentMapper = new RentMapperBuilder(getSession()).build();
        rentDao = rentMapper.rentDao(CqlIdentifier.fromCql("rent_a_vehicle"), "rents");
    }

    private void createTable() {
        SimpleStatement createAccounts =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("archive"), DataTypes.BOOLEAN)
                        .withColumn(CqlIdentifier.fromCql("clientId"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("vehicleId"), DataTypes.TEXT).build();
        getSession().execute(createAccounts);
    }

    public void addRent(Rent rent) {
        rentDao.save(rent);
    }

    public void deleteRent(int id) {
        rentDao.deleteById(id);
    }

    public void updateRent(Rent rent) {
        rentDao.update(rent);
    }

    public RentDB getRent(int id) {
        return rentDao.findById(id);
    }
}
