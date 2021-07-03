package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

}
