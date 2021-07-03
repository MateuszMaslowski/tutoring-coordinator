package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.Parent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends CrudRepository<Parent, Long> {
}
