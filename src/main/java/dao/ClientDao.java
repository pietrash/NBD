package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import model.ClientAddress;

@Dao
public interface ClientDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @Select
    ClientAddress findById(int id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void save(ClientAddress client);

    @Delete(entityClass = ClientAddress.class)
    void deleteById(int id);
}
