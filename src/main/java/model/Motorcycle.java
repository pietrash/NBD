package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_vehicle")
@CqlName("vehicles")
public class Motorcycle extends Vehicle {
    @CqlName("engineDisplacement")
    private int engineDisplacement;

    public Motorcycle(String id,
                      int weight,
                      String color,
                      double price,
                      int engineDisplacement,
                      int rented,
                      String discriminator) {
        super(id, weight, color, price, rented, discriminator);
        this.engineDisplacement = engineDisplacement;
    }

    public Motorcycle() {
    }

    public int getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(int engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }
}
