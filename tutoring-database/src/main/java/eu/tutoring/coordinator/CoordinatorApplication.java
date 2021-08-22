package eu.tutoring.coordinator;

import com.itextpdf.text.DocumentException;
import eu.tutoring.coordinator.data.models.*;
import eu.tutoring.coordinator.data.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class CoordinatorApplication {

    public static final Logger log = LoggerFactory.getLogger(CoordinatorApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CoordinatorApplication.class, args);
    }

    public static String yourEmail = "maslowskim255@gmail.com";

    @Bean
    public CommandLineRunner demo(
            ContactInformationRepository contactInformationRepository,
            MonthlyLessonsRepository monthlyLessonsRepository,
            ParentRepository parentRepository,
            PhysicalHomeAddressRepository physicalHomeAddressRepository,
            SubjectRepository subjectRepository,
            TuteeRepository tuteeRepository,
            TutoringRepository tutoringRepository,
            TutorRepository tutorRepository) {
        return (args) -> {

            addExampleDataToH2Base(
                    contactInformationRepository,
                    monthlyLessonsRepository,
                    physicalHomeAddressRepository,
                    subjectRepository,
                    tuteeRepository,
                    tutoringRepository,
                    tutorRepository
                    );
        };
    }

    private void addExampleDataToH2Base(ContactInformationRepository contactInformationRepository,
                                        MonthlyLessonsRepository monthlyLessonsRepository,
                                        PhysicalHomeAddressRepository physicalHomeAddressRepository,
                                        SubjectRepository subjectRepository,
                                        TuteeRepository tuteeRepository,
                                        TutoringRepository tutoringRepository,
                                        TutorRepository tutorRepository) {
        final String tuteesSampleEmail = "mateusz.maslowski.prof@gmail.com";

        ContactInformation tuteeContactInformation = new ContactInformation(tuteesSampleEmail);

        PhysicalHomeAddress tuteePhysicalHomeAddress = new PhysicalHomeAddress(
                    "Nowogrodzka 21",
                    37,
                    "Warsaw",
                    "Poland",
                    "02-000"
                    );

        contactInformationRepository.save(tuteeContactInformation);
        physicalHomeAddressRepository.save(tuteePhysicalHomeAddress);

        Tutee tutee = new Tutee(
                "John",
                "Doe",
                LocalDate.now().minusYears(19),
                tuteePhysicalHomeAddress,
                tuteeContactInformation,
                "Best school in Poland",
                "This is our first tutee."
         );

        tuteeRepository.save(tutee);

        ContactInformation tutorContactInformation = new ContactInformation(
                yourEmail,
                "500-500-500"
        );

        PhysicalHomeAddress tutorPhysicalHomeAddress = new PhysicalHomeAddress(
                "Street 13",
                37,
                "Kiev",
                "Ukraine",
                "00-000"
        );

        Subject subject = new Subject(
                "Programming",
                "The art of yelling while staring at the screen"
        );

        contactInformationRepository.save(tutorContactInformation);
        physicalHomeAddressRepository.save(tutorPhysicalHomeAddress);
        subjectRepository.save(subject);

        Tutor tutor = new Tutor(
                "Jack",
                "Sparrow",
                tutorContactInformation,
                tutorPhysicalHomeAddress,
                "00202891038",
                "88 8888 8888 8888 8888 8888 8888",
                "Kiev",
                new HashSet<Subject>(Collections.singleton(subject)),
                "He is good in Java."
        );

        tutorRepository.save(tutor);

        Tutoring tutoring = new Tutoring(
                tutor,
                tutee,
                subject,
                48,
                2,
                100,
                YearMonth.now()//,
         //       monthlyLessons,
        );

        tutoringRepository.save(tutoring);


        log.info("Everything went well");

    }
}
