package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity(defaultKeyspace = "rent_a_vehicle")
@CqlName("vehicles")
public class Car extends Vehicle {

    @CqlName("numberOfSeats")
    private int numberOfSeats;

    public Car(String id,
               int weight,
               String color,
               double price,
               int rented,
               int numberOfSeats,
               String discriminator) {
        super(id, weight, color, price, rented, discriminator);
        this.numberOfSeats = numberOfSeats;
    }

    public Car() {
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}
