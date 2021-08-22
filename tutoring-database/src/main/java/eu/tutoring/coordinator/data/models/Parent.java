package eu.tutoring.coordinator.data.models;

import javax.persistence.*;

@Entity
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToOne
    private PhysicalHomeAddress address;

    public Parent() {

    }

    public Parent(String firstName, String lastName, PhysicalHomeAddress address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public PhysicalHomeAddress getAddress() {
        return address;
    }
}
