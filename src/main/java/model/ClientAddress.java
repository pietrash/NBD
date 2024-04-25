package model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@Entity(defaultKeyspace = "rent_a_vehicle")
public class ClientAddress {
    @CqlName("street")
    private String street;
    @CqlName("streetNumber")
    private int streetNumber;
    @CqlName("city")
    private String city;
    @CqlName("postcode")
    private int postcode;
    @CqlName("firstName")
    private String firstName;
    @PartitionKey
    @CqlName("id")
    private int personalId;
    @CqlName("lastName")
    private String lastName;
    @CqlName("noRents")
    private int noRents;

    public ClientAddress(int personalId,
                         String firstName,
                         String lastName,
                         int noRents,
                         String street,
                         int streetNumber,
                         String city,
                         int postcode) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.noRents = noRents;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.postcode = postcode;
    }

    public ClientAddress() {
    }

    public int getNoRents() {
        return noRents;
    }

    public void setNoRents(int noRents) {
        this.noRents = noRents;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public int getPersonalId() {
        return personalId;
    }

    public void setPersonalId(int personalId) {
        this.personalId = personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
