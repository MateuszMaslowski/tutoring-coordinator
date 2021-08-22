package eu.tutoring.coordinator.data.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ContactInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String mobileNumber;

    public ContactInformation() {

    }

    public ContactInformation(String email, String mobileNumber) {
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public ContactInformation(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
