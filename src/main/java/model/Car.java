package model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "_clazz", value = "car")
public class Car extends Vehicle {
    @BsonProperty("numberOfSeats")
    private int numberOfSeats;

    @BsonCreator
    public Car(@BsonProperty("_id") String id,
               @BsonProperty("weight") int weight,
               @BsonProperty("color") String color,
               @BsonProperty("price") double price,
               @BsonProperty("rented") int rented,
               @BsonProperty("numberOfSeats") int numberOfSeats) {
        super(id, weight, color, price, rented);
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}
