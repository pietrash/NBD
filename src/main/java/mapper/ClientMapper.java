package mapper;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import dao.ClientDao;

@Mapper
public interface ClientMapper {
    @DaoFactory
    ClientDao clientDao(@DaoKeyspace CqlIdentifier keyspace, @DaoTable String table);
}
