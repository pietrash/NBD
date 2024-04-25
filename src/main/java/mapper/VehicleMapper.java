package mapper;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import dao.VehicleDao;

@Mapper
public interface VehicleMapper {
    @DaoFactory
    VehicleDao vehicleDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable String table);
}
