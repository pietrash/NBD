package mapper;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.internal.mapper.DaoCacheKey;
import com.datastax.oss.driver.internal.mapper.DefaultMapperContext;
import dao.RentDao;
import dao.RentDaoImpl__MapperGenerated;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Do not instantiate this class directly, use {@link RentMapperBuilder} instead.
 *
 * <p>Generated by the DataStax driver mapper, do not edit directly.
 */
@SuppressWarnings("all")
public class RentMapperImpl__MapperGenerated implements RentMapper {
  private final DefaultMapperContext context;

  private final ConcurrentMap<DaoCacheKey, RentDao> rentDaoCache = new ConcurrentHashMap<>();

  public RentMapperImpl__MapperGenerated(DefaultMapperContext context) {
    this.context = context;
  }

  @Override
  public RentDao rentDao(CqlIdentifier keyspace, String table) {
    DaoCacheKey key = new DaoCacheKey(keyspace, table, null, null);
    return rentDaoCache.computeIfAbsent(key, k -> RentDaoImpl__MapperGenerated.init(context.withDaoParameters(k.getKeyspaceId(), k.getTableId(), k.getExecutionProfileName(), k.getExecutionProfile())));
  }
}
