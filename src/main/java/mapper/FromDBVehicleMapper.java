package mapper;

import com.datastax.oss.driver.api.core.cql.Row;
import model.Bicycle;
import model.Car;
import model.Motorcycle;

public class FromDBVehicleMapper {
    public static Car getCar(Row car) {
        return new Car(
                car.getString("id"),
                car.getInt("weight"),
                car.getString("color"),
                car.getDouble("price"),
                car.getInt("rented"),
                car.getInt("numberOfSeats"),
                car.getString("discriminator")
        );
    }

    public static Bicycle getBicycle(Row bicycle) {
        return new Bicycle(
                bicycle.getString("id"),
                bicycle.getInt("weight"),
                bicycle.getString("color"),
                bicycle.getDouble("price"),
                bicycle.getBoolean("helperWheels"),
                bicycle.getInt("rented"),
                bicycle.getString("discriminator")
        );
    }

    public static Motorcycle getMotorcycle(Row motorcycle) {
        return new Motorcycle(
                motorcycle.getString("id"),
                motorcycle.getInt("weight"),
                motorcycle.getString("color"),
                motorcycle.getDouble("price"),
                motorcycle.getInt("engineDisplacement"),
                motorcycle.getInt("rented"),
                motorcycle.getString("discriminator")
        );
    }
}
