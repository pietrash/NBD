package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import model.Rent;
import model.RentDB;
import queryProviders.RentQueryProvider;

@Dao
public interface RentDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @Select
    RentDB findById(int id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentQueryProvider.class)
    void save(Rent rent);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentQueryProvider.class)
    void update(Rent rent);

    @Delete(entityClass = RentDB.class)
    void deleteById(int id);
}
