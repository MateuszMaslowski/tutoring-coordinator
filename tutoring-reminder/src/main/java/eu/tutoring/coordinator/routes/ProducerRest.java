package eu.tutoring.coordinator.routes;


import com.google.gson.Gson;
import eu.tutoring.coordinator.utilities.TutoringCurrentInfo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

@EnableJpaRepositories("eu.tutoring.coordinator.data.repositories")
@Component
public class ProducerRest extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        Properties properties = new Properties();

        final long minute = 60000;
        final long month = 31 * 24 * 60 * minute;

        from("timer://monthly-reminder?fixedRate=true&period=" + month)
                .setBody(constant("request"))
                .setHeader(KafkaConstants.KEY, constant("Camel"))
                .to("kafka:tutoring-reminder?brokers=localhost:9092");

        /*
        * What do I need here?
        * For each tutor information who is their tutee
        * */
        from("kafka:tutoring-current-info?brokers=localhost:9092")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        AttachmentMessage in = exchange.getMessage(AttachmentMessage.class);

                        in.setHeader("emails-to-process", in.getBody());
                    }
                })
                .to("direct:send-emails");

        from("direct:send-emails")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        AttachmentMessage in = exchange.getMessage(AttachmentMessage.class);

                        TutoringCurrentInfo tutoringCurrentInfo =
                                new Gson().fromJson(in.getHeader("emails-to-process").toString(), TutoringCurrentInfo.class);

                        int indx = tutoringCurrentInfo.getTutorsNames().size() - 1;

                        in.setHeader("subject", "Lessons statistics for this month");
                        in.setHeader("to", tutoringCurrentInfo.getTutorsEmails().get(indx));
                        in.setHeader("from", "jnp2@meetit.pl");

                        String[] tutorFullName = tutoringCurrentInfo.getTutorsNames().get(indx).split(" ");

                        String message = "Dear " + tutorFullName[0]
                                + ",\nplease upload the number of lessons you had with the aftermentioned students during this month:\n";

                        for (String tutee : tutoringCurrentInfo.getTutees().get(indx)) {
                            String[] tuteeFullName = tutee.split(" ");

                            message = message + "http://localhost:8082/invoice/get/"
                                    + tutorFullName[0] + "/" + tutorFullName[1] + "/"
                                    + tuteeFullName[0] + "/" + tuteeFullName[1] + "/"
                                    + "\n\n";
                        }

                        message += "\n\nBest,\nJNP2 Staff";

                        in.setBody(message);

                        if (indx == 0) {
                            in.setHeader(
                                    "emails-to-process",
                                    ""
                            );
                        }
                        else {
                            tutoringCurrentInfo.getTutees().remove(indx);
                            tutoringCurrentInfo.getTutorsEmails().remove(indx);
                            tutoringCurrentInfo.getTutorsNames().remove(indx);

                            in.setHeader(
                                    "emails-to-process",
                                    new Gson().toJson(tutoringCurrentInfo)
                            );
                        }
                    }
                })
                .choice()
                    .when(header("emails-to-process").isEqualTo("")).to("smtp://smtp.zenbox.pl?username=jnp2@meetit.pl&password=Jnp2jnp2$&contentType=text/plain")
                .otherwise()
                    .to("direct:send-emails", "smtp://smtp.zenbox.pl?username=jnp2@meetit.pl&password=Jnp2jnp2$&contentType=text/plain");
    }
}
