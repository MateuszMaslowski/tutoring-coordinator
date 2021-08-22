package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.Tutee;
import eu.tutoring.coordinator.data.models.Tutor;
import eu.tutoring.coordinator.data.models.Tutoring;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutoringRepository extends CrudRepository<Tutoring, Long> {
    List<Tutoring> findByTuteeAndTutor(Tutee tutee, Tutor tutor);

    List<Tutoring> findByTutor(Tutor tutor);

    List<Tutoring> findByTutee(Tutee tutee);
}