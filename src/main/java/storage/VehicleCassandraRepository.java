package storage;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.VehicleDao;
import mapper.VehicleMapper;
import mapper.VehicleMapperBuilder;
import model.Vehicle;

public class VehicleCassandraRepository extends AbstractCassandraRepository {

    VehicleDao vehicleDao;

    public VehicleCassandraRepository() {
        initSession();
        createTable();
        VehicleMapper vehicleMapper = new VehicleMapperBuilder(getSession()).build();
        vehicleDao = vehicleMapper.vehicleDao(CqlIdentifier.fromCql("rent_a_vehicle"), "vehicles");
    }

    private void createTable() {
        SimpleStatement createAccounts =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("vehicles"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("weight"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("color"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("price"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("rented"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("helperWheels"), DataTypes.BOOLEAN)
                        .withColumn(CqlIdentifier.fromCql("engineDisplacement"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("numberOfSeats"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("discriminator"), DataTypes.TEXT).build();
        getSession().execute(createAccounts);
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleDao.save(vehicle);
    }

    public void deleteVehicle(String id) {
        vehicleDao.deleteById(id);
    }

    public void updateVehicle(Vehicle vehicle) {
        vehicleDao.save(vehicle);
    }

    public Vehicle getVehicle(String id) {
        return vehicleDao.findById(id);
    }

}
