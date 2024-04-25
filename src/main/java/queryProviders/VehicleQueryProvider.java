package queryProviders;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import model.Bicycle;
import model.Car;
import model.Motorcycle;
import model.Vehicle;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static mapper.FromDBVehicleMapper.*;

public class VehicleQueryProvider {
    private final CqlSession session;
    private final EntityHelper<Car> carEntityHelper;
    private final EntityHelper<Motorcycle> motorcycleEntityHelper;
    private final EntityHelper<Bicycle> bicycleEntityHelper;

    public VehicleQueryProvider(MapperContext ctx,
                                EntityHelper<Car> carEntityHelper,
                                EntityHelper<Motorcycle> motorcycleEntityHelper,
                                EntityHelper<Bicycle> bicycleEntityHelper) {
        this.session = ctx.getSession();
        this.carEntityHelper = carEntityHelper;
        this.motorcycleEntityHelper = motorcycleEntityHelper;
        this.bicycleEntityHelper = bicycleEntityHelper;
    }

    public void save(Vehicle vehicle) {
        session.execute(
                switch (vehicle.getDiscriminator()) {
                    case "car" -> {
                        Car car = (Car) vehicle;
                        yield session.prepare(carEntityHelper.insert().build())
                                .bind()
                                .setString("id", car.getId())
                                .setInt("weight", car.getWeight())
                                .setString("color", car.getColor())
                                .setDouble("price", car.getPrice())
                                .setInt("rented", car.isRented())
                                .setString("discriminator", car.getDiscriminator())
                                .setInt("numberOfSeats", car.getNumberOfSeats());
                    }
                    case "motorcycle" -> {
                        Motorcycle motorcycle = (Motorcycle) vehicle;
                        yield session.prepare(motorcycleEntityHelper.insert().build())
                                .bind()
                                .setString("id", motorcycle.getId())
                                .setInt("weight", motorcycle.getWeight())
                                .setString("color", motorcycle.getColor())
                                .setDouble("price", motorcycle.getPrice())
                                .setInt("rented", motorcycle.isRented())
                                .setString("discriminator", motorcycle.getDiscriminator())
                                .setInt("engineDisplacement", motorcycle.getEngineDisplacement());
                    }
                    case "bicycle" -> {
                        Bicycle bicycle = (Bicycle) vehicle;
                        yield session.prepare(bicycleEntityHelper.insert().build())
                                .bind()
                                .setString("id", bicycle.getId())
                                .setInt("weight", bicycle.getWeight())
                                .setString("color", bicycle.getColor())
                                .setDouble("price", bicycle.getPrice())
                                .setInt("rented", bicycle.isRented())
                                .setString("discriminator", bicycle.getDiscriminator())
                                .setBoolean("helperWheels", bicycle.isHelperWheels());
                    }
                    default -> throw new IllegalArgumentException();
                }
        );
    }

    public Vehicle findById(String id) {
        Select selectVehicle = QueryBuilder.selectFrom(CqlIdentifier.fromCql("vehicles"))
                .all()
                .where(Relation.column("id").isEqualTo(literal(id)));

        Row row = session.execute(selectVehicle.build()).one();
        String discriminator = row.getString("discriminator");

        return switch (discriminator) {
            case "car" -> getCar(row);
            case "motorcycle" -> getMotorcycle(row);
            case "bicycle" -> getBicycle(row);
            default -> throw new IllegalArgumentException();
        };
    }
}
