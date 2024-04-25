package model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ClientAddress {
    @BsonProperty("_id")
    private final int personalId;
    @BsonProperty("firstName")
    private final String firstName;
    @BsonProperty("lastname")
    private final String lastName;
    @BsonProperty("noRents")
    private final int noRents;
    @BsonProperty("street")
    private final String street;
    @BsonProperty("streetNumber")
    private final int streetNumber;
    @BsonProperty("city")
    private final String city;
    @BsonProperty("postcode")
    private final int postcode;

    @BsonCreator
    public ClientAddress(@BsonProperty("_id") int personalId,
                         @BsonProperty("firstName") String firstName,
                         @BsonProperty("lastname") String lastName,
                         @BsonProperty("noRents") int noRents,
                         @BsonProperty("street") String street,
                         @BsonProperty("streetNumber") int streetNumber,
                         @BsonProperty("city") String city,
                         @BsonProperty("postcode") int postcode) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.noRents = noRents;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postcode = postcode;
    }

    public ClientAddress(int personalId,
                         String firstName,
                         String lastName,
                         String street,
                         int streetNumber,
                         String city,
                         int postcode) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.noRents = 0;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postcode = postcode;
    }

    public int getNoRents() {
        return noRents;
    }

    public String getStreet() {
        return street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getCity() {
        return city;
    }

    public int getPostcode() {
        return postcode;
    }

    public int getPersonalId() {
        return personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
