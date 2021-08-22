package eu.tutoring.coordinator;

import com.itextpdf.text.DocumentException;
import eu.tutoring.coordinator.pdf.DocumentNotGenerated;
import eu.tutoring.coordinator.utilities.GlobalProjectProperties;
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

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {

//            addExampleDataToH2Base(
//                    contactInformationRepository,
//                    monthlyLessonsRepository,
//                    physicalHomeAddressRepository,
//                    subjectRepository,
//                    tuteeRepository,
//                    tutoringRepository,
//                    tutorRepository
//                    );

//            try {
//                preparePdf(tuteeRepository, tutoringRepository, monthlyLessonsRepository);
//                log.info("EVERYTHINS IS COOL AND I LIKE PANCAKES");
//            } catch (DocumentNotGenerated documentNotGenerated) {
//                documentNotGenerated.printStackTrace();
//            }

        };
    }

//    private void preparePdf(
//            TuteeRepository tuteeRepository,
//            TutoringRepository tutoringRepository,
//            MonthlyLessonsRepository monthlyLessonsRepository
//    ) throws DocumentException, DocumentNotGenerated, FileNotFoundException {
//        Optional<Tutee> optionalTutee = tuteeRepository.findById(3L);
//
//        Tutee tutee = optionalTutee.get();
//
//        ClientInvoice clientInvoice = new ClientInvoice(tutee, tutoringRepository, monthlyLessonsRepository);
//
//        clientInvoice.generate();
//    }
}
