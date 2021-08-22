package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.ContactInformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactInformationRepository extends CrudRepository<ContactInformation, Long> {
}
