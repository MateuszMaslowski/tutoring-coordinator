package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.Tutee;
import eu.tutoring.coordinator.data.models.Tutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorRepository extends CrudRepository<Tutor, Long> {
    List<Tutor> findByFirstNameAndLastName(String firstName, String lastName);

    List<Tutor> findAll();
}