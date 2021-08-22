package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.PhysicalHomeAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalHomeAddressRepository extends CrudRepository<PhysicalHomeAddress, Long> {
}
