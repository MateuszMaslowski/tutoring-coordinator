package eu.tutoring.coordinator.data.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;

@Entity
public class Tutee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    private LocalDate birthDate;

    @OneToOne
    private PhysicalHomeAddress address;

    @OneToOne
    private ContactInformation contactInformation;

    @OneToOne
    private Parent parent;

    private String school;
    private String description;

    private String invoiceId;

    private int updatedMonthlyLessons;

    public Tutee() {

    }

    public Tutee(String firstName, String lastName, LocalDate birthDate, PhysicalHomeAddress address,
                 ContactInformation contactInformation, Parent parent, String school, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.contactInformation = contactInformation;
        this.parent = parent;
        this.school = school;
        this.description = description;

        this.updatedMonthlyLessons = 0;

        Random rnd = new Random();
        this.invoiceId = address.getZipCodeNumbers() + (rnd.nextInt() % 1000000);
    }

    public Tutee(String firstName, String lastName, LocalDate birthDate, PhysicalHomeAddress address,
                 ContactInformation contactInformation, String school, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.contactInformation = contactInformation;
        this.parent = null;
        this.school = school;
        this.description = description;

        Random rnd = new Random();
        this.invoiceId = address.getZipCodeNumbers() + (rnd.nextInt() % 1000000);
    }

    public boolean isAdult() {
        Period period = Period.between(birthDate, LocalDate.now());

        return period.getYears() >= 18;
    }

    public Parent getParent() {
        return parent;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public PhysicalHomeAddress getAddress() {
        return address;
    }

    public ContactInformation getContactInformation() { return contactInformation; }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void IncreaseUpdatedMonthlyLessons() {
        updatedMonthlyLessons++;
    }

    public void updateMonthlyLessons() {
        updatedMonthlyLessons = 0;
    }

    public int getUpdatedMonthlyLessons() {
        return updatedMonthlyLessons;
    }
}
