package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@Entity(defaultKeyspace = "rent_a_vehicle")
@CqlName("vehicles")
public class Vehicle {
    @PartitionKey
    @CqlName("id")
    private String id;
    @CqlName("weight")
    private int weight;
    @CqlName("color")
    private String color;
    @CqlName("price")
    private double price;
    @CqlName("rented")
    private int rented;
    @CqlName("discriminator")
    private String discriminator;

    public Vehicle(String id,
                   int weight,
                   String color,
                   double price,
                   int rented,
                   String discriminator) {
        this.id = id;
        this.weight = weight;
        this.color = color;
        this.price = price;
        this.rented = rented;
        this.discriminator = discriminator;
    }

    public Vehicle() {
    }

    public int getRented() {
        return rented;
    }

    public void setRented(int rented) {
        this.rented = rented;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int isRented() {
        return rented;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
