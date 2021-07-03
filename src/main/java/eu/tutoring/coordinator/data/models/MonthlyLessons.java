package eu.tutoring.coordinator.data.models;

import javax.persistence.*;
import java.time.YearMonth;

@Entity
public class MonthlyLessons {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private YearMonth yearMonth;
    private int numberOfLessons;

    @ManyToOne
    private Tutoring tutoring;

    public MonthlyLessons() {

    }

    public MonthlyLessons(YearMonth yearMonth, int numberOfLessons, Tutoring tutoring) {
        this.yearMonth = yearMonth;
        this.numberOfLessons = numberOfLessons;
        this.tutoring = tutoring;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public int getNumberOfLessons() {
        return numberOfLessons;
    }
}
