package model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@BsonDiscriminator(key = "_clazz", value = "motorcycle")
public class Motorcycle extends Vehicle {
    @BsonProperty("engineDisplacement")
    private int engineDisplacement;

    @BsonCreator
    public Motorcycle(@BsonProperty("_id") String id,
                      @BsonProperty("weight") int weight,
                      @BsonProperty("color") String color,
                      @BsonProperty("price") double price,
                      @BsonProperty("engineDisplacement") int engineDisplacement,
                      @BsonProperty("rented") int rented) {
        super(id, weight, color, price, rented);
        this.engineDisplacement = engineDisplacement;
    }

    public int getEngineDisplacement() {
        return engineDisplacement;
    }
}
