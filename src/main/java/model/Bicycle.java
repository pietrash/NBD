package model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "_clazz", value = "bicycle")
public class Bicycle extends Vehicle {
    @BsonProperty("helperwheels")
    private boolean helperWheels;

    @BsonCreator
    public Bicycle(@BsonProperty("_id") String id,
                   @BsonProperty("weight") int weight,
                   @BsonProperty("color") String color,
                   @BsonProperty("price") double price,
                   @BsonProperty("helperwheels") boolean helperWheels,
                   @BsonProperty("rented") int rented) {
        super(id, weight, color, price, rented);
        this.helperWheels = helperWheels;
    }

    public boolean isHelperWheels() {
        return helperWheels;
    }
}
