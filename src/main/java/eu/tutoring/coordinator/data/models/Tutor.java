package eu.tutoring.coordinator.data.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Tutor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToOne
    private ContactInformation contactInformation;

    @OneToOne
    private PhysicalHomeAddress address;

    private String nationalIdentificationNumber;

    private String bankAccount;
    private String taxOffice;

    @OneToMany
    private Set<Subject> specializations;

    private String description;

    public Tutor() {
        this.specializations = null;
    }

    public Tutor(String firstName, String lastName, ContactInformation contactInformation,
                 PhysicalHomeAddress address, String nationalIdentificationNumber, String bankAccount, String taxOffice,
                 Set<Subject> specializations, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInformation = contactInformation;
        this.address = address;
        this.nationalIdentificationNumber = nationalIdentificationNumber;
        this.bankAccount = bankAccount;
        this.taxOffice = taxOffice;
        this.specializations = specializations;
        this.description = description;
    }
}
