package eu.tutoring.coordinator.data.repositories;

import eu.tutoring.coordinator.data.models.MonthlyLessons;
import eu.tutoring.coordinator.data.models.Tutor;
import eu.tutoring.coordinator.data.models.Tutoring;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface MonthlyLessonsRepository extends CrudRepository<MonthlyLessons, Long> {
    List<MonthlyLessons> findByTutoringAndYearMonth(Tutoring tutoring, YearMonth yearMonth);

    List<MonthlyLessons> findByTutoring(Tutoring tutoring);
}
