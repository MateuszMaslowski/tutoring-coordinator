package eu.tutoring.coordinator.data.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    public Subject() {

    }

    public Subject(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Subject(String name) {
        this.name = name;
        this.description = "";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }
}
