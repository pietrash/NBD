package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_vehicle")
@CqlName("vehicles")
public class Bicycle extends Vehicle {

    @CqlName("helperWheels")
    private boolean helperWheels;

    public Bicycle(String id,
                   int weight,
                   String color,
                   double price,
                   boolean helperWheels,
                   int rented,
                   String discriminator) {
        super(id, weight, color, price, rented, discriminator);
        this.helperWheels = helperWheels;
    }

    public Bicycle() {
    }

    public boolean isHelperWheels() {
        return helperWheels;
    }

    public void setHelperWheels(boolean helperWheels) {
        this.helperWheels = helperWheels;
    }
}
