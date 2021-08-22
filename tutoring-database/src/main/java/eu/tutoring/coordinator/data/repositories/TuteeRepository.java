package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.Tutee;
import eu.tutoring.coordinator.data.models.Tutoring;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TuteeRepository extends CrudRepository<Tutee, Long> {
    List<Tutee> findByFirstNameAndLastName(String firstName, String lastName);
}
