package eu.tutoring.coordinator.data.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PhysicalHomeAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String street;
    private int apartmentNumber;
    private String city;
    private String country;
    private String zipCode;

    public PhysicalHomeAddress() {

    }

    public PhysicalHomeAddress(String street, int apartmentNumber, String city, String country, String zipCode) {
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCodeNumbers() {
        String result = "";

        for (char s : zipCode.toCharArray()) {
            if ('0' <= s && s <= '9') {
                result += s;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return street + "/" + apartmentNumber + ", " + zipCode + " " + city + ", " + country;
    }
}
