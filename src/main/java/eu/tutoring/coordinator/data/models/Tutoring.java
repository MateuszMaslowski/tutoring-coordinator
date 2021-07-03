package eu.tutoring.coordinator.data.models;

import eu.tutoring.coordinator.CoordinatorApplication;
import eu.tutoring.coordinator.data.repositories.MonthlyLessonsRepository;

import javax.persistence.*;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Tutoring {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Tutor tutor;

    @ManyToOne
    private Tutee tutee;

    @ManyToOne
    private Subject subject;

    private int totalNumberOfSessions;
    private int expectedLessonsPerWeek;
    private int pricePerLesson;

    private YearMonth startDate;

//    @OneToMany
//    private Set<MonthlyLessons> monthlyLessonsSet;

    //private HashMap<YearMonth, Integer> lessonsPerMonth;

    public Tutoring() {
    }

    public Tutoring(Tutor tutor, Tutee tutee, Subject subject, int totalNumberOfSessions, int expectedLessonsPerWeek,
                    int pricePerLesson, YearMonth startDate) {
        this.tutor = tutor;
        this.tutee = tutee;
        this.subject = subject;
        this.totalNumberOfSessions = totalNumberOfSessions;
        this.expectedLessonsPerWeek = expectedLessonsPerWeek;
        this.pricePerLesson = pricePerLesson;
        this.startDate = startDate;
    }

    public Subject getSubject() {
        return subject;
    }

    public Integer getNumberOfLessonsByYearMonth(MonthlyLessonsRepository monthlyLessonsRepository,
                                                 YearMonth yearMonth) {
        List<MonthlyLessons> monthlyLessonsList = monthlyLessonsRepository.findByTutoring(this);

        for (MonthlyLessons monthlyLessons : monthlyLessonsList) {
            if (monthlyLessons.getYearMonth().toString().equals(yearMonth.toString())) {
                return monthlyLessons.getNumberOfLessons();
            }
        }

        return 0;
    }

    public int getPricePerLesson() {
        return pricePerLesson;
    }
}
