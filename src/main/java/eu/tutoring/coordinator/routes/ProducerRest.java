package eu.tutoring.coordinator.routes;

import eu.tutoring.coordinator.CoordinatorApplication;
import eu.tutoring.coordinator.data.models.Tutee;
import eu.tutoring.coordinator.data.repositories.MonthlyLessonsRepository;
import eu.tutoring.coordinator.data.repositories.TuteeRepository;
import eu.tutoring.coordinator.data.repositories.TutoringRepository;
import eu.tutoring.coordinator.pdf.ClientInvoice;
import eu.tutoring.coordinator.utilities.GlobalProjectProperties;
import org.apache.camel.Exchange;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

@EnableJpaRepositories("eu.tutoring.coordinator.data.repositories")
@Component
public class ProducerRest extends RouteBuilder {

    @Autowired
    TutoringRepository tutoringRepository;

    @Autowired
    MonthlyLessonsRepository monthlyLessonsRepository;

    @Autowired
    TuteeRepository tuteeRepository;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        Properties properties = new Properties();

        rest("/invoice")
                .produces("application/json")
                .get("/get/{id}")
                .to("direct:invoice");

        from("direct:invoice")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Long id = exchange.getIn().getHeader("id", Long.class);

                        Optional<Tutee> tuteeOptional = tuteeRepository.findById(id);
                        
                        ClientInvoice clientInvoice = new ClientInvoice(
                                tuteeOptional.get(),
                                tutoringRepository,
                                monthlyLessonsRepository
                        );

                        clientInvoice.generate();
                    }
                });

        from("file:" + GlobalProjectProperties.getFilePath())
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        AttachmentMessage in = exchange.getMessage(AttachmentMessage.class);

                        String fileName = in.getHeader("CamelFileName", String.class);

                        String fileAbsolutePath = in.getHeader("CamelFileAbsolutePath", String.class);

                        String tuteeFullName = "";
                        int indx = 1;
                        while (fileName.charAt(indx) != ']') {
                            tuteeFullName += fileName.charAt(indx++);
                        }

                        String[] tuteeName = tuteeFullName.split(" ");

                        List<Tutee> tutees = tuteeRepository.findByFirstNameAndLastName(tuteeName[0], tuteeName[1]);

                        Tutee tutee = tutees.get(0);

                        in.setHeader("subject", "Your invoice for this month.");
                        in.setHeader("to", tutee.getContactInformation().getEmail());
                        in.setHeader("from", "jnp2@meetit.pl");

                        exchange.getIn().setBody("Dear " + tutee.getFirstName() + ",\n\n" + "please find the invoice attached.\n\n" +
                                "Having any questions do not hesitate to contact us.\n\nBest,\nJNP2 Staff");

                        in.addAttachment(fileName, new DataHandler(new FileDataSource(fileAbsolutePath)));
                    }
                })
                .to("smtp://smtp.zenbox.pl?username=jnp2@meetit.pl&password=Jnp2jnp2$&contentType=text/plain");
    }
}
