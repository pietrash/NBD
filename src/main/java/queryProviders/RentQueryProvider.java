package queryProviders;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import model.Rent;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RentQueryProvider {
    private final CqlSession session;


    public RentQueryProvider(MapperContext ctx) {
        this.session = ctx.getSession();
    }

    public void save(Rent rent) {
        Insert addRent = QueryBuilder.insertInto(CqlIdentifier.fromCql("rents"))
                .value("id", literal(rent.getId()))
                .value("vehicleId", literal(rent.getVehicle().getId()))
                .value("clientId", literal(rent.getClient().getPersonalId()))
                .value("archive", literal(rent.isArchive()));

        Update updateClient = QueryBuilder.update(CqlIdentifier.fromCql("clients"))
                .setColumn("noRents", literal(rent.getClient().getNoRents()))
                .where(Relation.column("id").isEqualTo(literal(rent.getClient().getPersonalId())));

        Update updateVehicle = QueryBuilder.update(CqlIdentifier.fromCql("vehicles"))
                .setColumn("rented", literal(rent.getVehicle().isRented()))
                .where(Relation.column("id").isEqualTo(literal(rent.getVehicle().getId())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateClient.build())
                .addStatement(updateVehicle.build())
                .addStatement(addRent.build())
                .build();

        session.execute(batchStatement);
    }

    public void update(Rent rent) {
        Update updateRent = QueryBuilder.update(CqlIdentifier.fromCql("rents"))
                .setColumn("vehicleId", literal(rent.getVehicle().getId()))
                .setColumn("clientId", literal(rent.getClient().getPersonalId()))
                .setColumn("archive", literal(rent.isArchive()))
                .where(Relation.column("id").isEqualTo(literal(rent.getId())));

        Update updateClient = QueryBuilder.update(CqlIdentifier.fromCql("clients"))
                .setColumn("noRents", literal(rent.getClient().getNoRents()))
                .where(Relation.column("id").isEqualTo(literal(rent.getClient().getPersonalId())));

        Update updateVehicle = QueryBuilder.update(CqlIdentifier.fromCql("vehicles"))
                .setColumn("rented", literal(rent.getVehicle().isRented()))
                .where(Relation.column("id").isEqualTo(literal(rent.getVehicle().getId())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateClient.build())
                .addStatement(updateVehicle.build())
                .addStatement(updateRent.build())
                .build();

        session.execute(batchStatement);
    }
}

