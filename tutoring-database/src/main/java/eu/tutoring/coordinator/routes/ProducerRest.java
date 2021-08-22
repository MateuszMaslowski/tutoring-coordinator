package eu.tutoring.coordinator.routes;

import ch.qos.logback.core.net.server.Client;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import eu.tutoring.coordinator.data.models.*;
import eu.tutoring.coordinator.data.repositories.MonthlyLessonsRepository;
import eu.tutoring.coordinator.data.repositories.TuteeRepository;
import eu.tutoring.coordinator.data.repositories.TutorRepository;
import eu.tutoring.coordinator.data.repositories.TutoringRepository;
import eu.tutoring.coordinator.routes.utilities.ClientInvoiceData;
import eu.tutoring.coordinator.routes.utilities.TutoringCurrentInfo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.data.util.Pair;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.theme.CookieThemeResolver;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@EnableJpaRepositories("eu.tutoring.coordinator.data.repositories")
@Component
public class ProducerRest extends RouteBuilder {

    @Autowired
    TutoringRepository tutoringRepository;

    @Autowired
    MonthlyLessonsRepository monthlyLessonsRepository;

    @Autowired
    TuteeRepository tuteeRepository;

    @Autowired
    TutorRepository tutorRepository;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        Properties properties = new Properties();


        from("kafka:tutoring-reminder?brokers=localhost:9092")
                .log("xdd ${body}")
                .process(new Processor() {
                             @Override
                             public void process(Exchange exchange) throws Exception {
                                 AttachmentMessage in = exchange.getMessage(AttachmentMessage.class);

                                 in.setBody(getTutoringCurrentInfo());
                             }
                })
                .to("kafka:tutoring-current-info?brokers=localhost:9092");


        from("kafka:tutoring-monthly-update?brokers=localhost:9092")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        AttachmentMessage in = exchange.getMessage(AttachmentMessage.class);

                        Tutor tutor = getTutor(in);
                        Tutee tutee = getTutee(in);

                        Tutoring tutoring = getTutoring(tutor, tutee);

                        boolean isUpdated = updateMonthlyLessons(in, tutoring, tutee);

                        if (isUpdated && tutee.getUpdatedMonthlyLessons() == tutoringRepository.findByTutee(tutee).size()) {
                            prepareExchange(in, tutor, tutee);

                            tutee.updateMonthlyLessons();
                        }
                    }
                })
                .choice()
                    .when(header("update_completed").isEqualTo(true)).to("kafka:tutoring-ready-invoice-data?brokers=localhost:9092")
                .end();
    }

    private boolean updateMonthlyLessons(AttachmentMessage in, Tutoring tutoring, Tutee tutee) {
        List<MonthlyLessons> monthlyLessonsList = monthlyLessonsRepository.findByTutoringAndYearMonth(tutoring, YearMonth.now());

        if (monthlyLessonsList.size() > 0) {
            return false;
        }

        int lessonsNumber = Integer.parseInt(in.getHeader("lno", String.class));

        MonthlyLessons monthlyLessons = new MonthlyLessons(
                YearMonth.now(),
                lessonsNumber,
                tutoring
        );

        tutee.IncreaseUpdatedMonthlyLessons();

        tuteeRepository.save(tutee);
        monthlyLessonsRepository.save(monthlyLessons);

        return true;
    }

    private void prepareExchange(AttachmentMessage in, Tutor tutor, Tutee tutee) {
        in.setHeader("update_completed", true);

        ClientInvoiceData clientInvoiceData = extractClientInvoiceData(tutor, tutee);

        in.setBody(new Gson().toJson(clientInvoiceData));
    }

    private ClientInvoiceData extractClientInvoiceData(Tutor tutor, Tutee tutee) {
        String payersName;
        String payersAddress;

        if (tutee.isAdult()) {
            payersName = tutee.getFullName();
            payersAddress = tutee.getAddress().toString();
        }
        else {
            Parent parent = tutee.getParent();

            payersName = parent.getFullName();
            payersAddress = parent.getAddress().toString();
        }

        List<String> subjects = new ArrayList<>();
        List<Integer> numberOfLessons = new ArrayList<>();
        List<Integer> pricePerLesson =new ArrayList<>();

        List<Tutoring> tutoringList = tutoringRepository.findByTutee(tutee);

        for (Tutoring tutoring : tutoringList) {
            subjects.add(tutoring.getSubject().toString());

            numberOfLessons.add(tutoring.getNumberOfLessonsByYearMonth(monthlyLessonsRepository, YearMonth.now()));

            pricePerLesson.add(tutoring.getPricePerLesson());
        }

        return new ClientInvoiceData(
                tutee.getFullName(),
                tutee.getContactInformation().getEmail(),
                payersName,
                payersAddress,
                tutee.getInvoiceId(),
                subjects,
                numberOfLessons,
                pricePerLesson,
                subjects.size()
        );
    }

    private Tutor getTutor(AttachmentMessage in) {
        String tutorFirstName = in.getHeader("tutor_fname", String.class);
        String tutorLastName = in.getHeader("tutor_lname", String.class);

        return tutorRepository.findByFirstNameAndLastName(tutorFirstName, tutorLastName).get(0);
    }

    private Tutee getTutee(AttachmentMessage in) {
        String tuteeFirstName = in.getHeader("tutee_fname", String.class);
        String tuteeLastName = in.getHeader("tutee_lname", String.class);

        return tuteeRepository.findByFirstNameAndLastName(tuteeFirstName, tuteeLastName).get(0);
    }

    private Tutoring getTutoring(Tutor tutor, Tutee tutee) {
        return tutoringRepository.findByTuteeAndTutor(tutee, tutor).get(0);
    }

    private String getTutoringCurrentInfo() {
        ArrayList<Tutor> tutors = (ArrayList<Tutor>) tutorRepository.findAll();

        ArrayList<String> tutorsNames = new ArrayList<>();
        ArrayList<String> tutorsEmails = new ArrayList<>();

        ArrayList<ArrayList<String>> tuteesList = new ArrayList<>();

        for (Tutor tutor : tutors) {
            tutorsEmails.add(
                    tutor.getContactInformation().getEmail()

            );

            tutorsNames.add(
                    tutor.getFullName()
            );

            ArrayList<String> tutorsTutees = new ArrayList<>();

            List<Tutoring> tutorings = tutoringRepository.findByTutor(tutor);

            for (Tutoring tutoring : tutorings) {
                tutorsTutees.add(
                        tutoring.getTutee().getFullName()
                );
            }

            tuteesList.add(tutorsTutees);
        }

        TutoringCurrentInfo tutoringCurrentInfo = new TutoringCurrentInfo(tutorsNames, tutorsEmails, tuteesList);

        return new Gson().toJson(tutoringCurrentInfo);
    }
}
