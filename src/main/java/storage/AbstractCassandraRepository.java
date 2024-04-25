package storage;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;

import java.net.InetSocketAddress;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;

public class AbstractCassandraRepository {
    private static CqlSession session;

    public static CqlSession getSession() {
        return session;
    }

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .addContactPoint(new InetSocketAddress("cassandra3", 9044))
                .withLocalDatacenter("dc1")
                .withKeyspace(CqlIdentifier.fromCql("rent_a_vehicle"))
                .withAuthCredentials("cassandra", "cassandrapassword")
                .build();

        initKeyspace();
    }

    private void initKeyspace() {
        CreateKeyspace keyspace = createKeyspace(CqlIdentifier.fromCql("rent_a_vehicle"))
                .ifNotExists()
                .withSimpleStrategy(3)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);
    }

    public void dropKeyspace() {
        session.execute(
                SchemaBuilder.dropKeyspace(CqlIdentifier.fromCql("rent_a_vehicle"))
                        .ifExists()
                        .build()
        );
    }
}