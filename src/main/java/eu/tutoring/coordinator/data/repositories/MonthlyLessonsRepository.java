package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.MonthlyLessons;
import eu.tutoring.coordinator.data.models.Tutoring;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyLessonsRepository extends CrudRepository<MonthlyLessons, Long> {
    List<MonthlyLessons> findByTutoring(Tutoring tutoring);
}
