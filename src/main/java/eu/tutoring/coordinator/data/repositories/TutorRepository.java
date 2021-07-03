package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.Tutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends CrudRepository<Tutor, Long> {

}