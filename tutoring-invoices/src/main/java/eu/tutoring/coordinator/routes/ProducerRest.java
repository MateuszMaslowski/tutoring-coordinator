package eu.tutoring.coordinator.routes;

import ch.qos.logback.core.net.server.Client;
import com.google.gson.stream.JsonReader;
import eu.tutoring.coordinator.pdf.ClientInvoice;
import eu.tutoring.coordinator.pdf.DocumentNotGenerated;
import eu.tutoring.coordinator.utilities.ClientInvoiceData;
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
import com.google.gson.Gson;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;

//@EnableJpaRepositories("eu.tutoring.coordinator.data.repositories")
@Component
public class ProducerRest extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        Properties properties = new Properties();

        rest("/invoice")
                .produces("application/json")
                .get("/get/{tutor_fname}/{tutor_lname}/{tutee_fname}/{tutee_lname}/{lno}")
                .to("direct:update");

        from("direct:update")
                .to("kafka:tutoring-monthly-update?brokers=localhost:9092");

        from("kafka:tutoring-ready-invoice-data?brokers=localhost:9092")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        AttachmentMessage in = exchange.getMessage(AttachmentMessage.class);

                        ClientInvoiceData clientInvoiceData =
                                new Gson().fromJson(in.getBody().toString(), ClientInvoiceData.class);

                        ClientInvoice clientInvoice = new ClientInvoice(clientInvoiceData);

                        clientInvoice.generate();

                        in.setHeader("subject", "Your invoice for this month.");
                        in.setHeader("to", clientInvoiceData.getTuteeEmailAddress());
                        in.setHeader("from", "jnp2@meetit.pl");

                        String[] tuteeName = clientInvoiceData.getTuteeName().split(" ");

                        exchange.getIn().setBody("Dear " + tuteeName[0] + ",\n\n" + "please find the invoice attached.\n\n" +
                                "Having any questions do not hesitate to contact us.\n\nBest,\nJNP2 Staff");

                        try {
                            in.addAttachment(clientInvoice.getFileName(), new DataHandler(new FileDataSource(clientInvoice.getDocumentFilePath())));
                        } catch (DocumentNotGenerated documentNotGenerated) {
                            documentNotGenerated.printStackTrace();
                        }
                    }
                })
                .to("smtp://smtp.zenbox.pl?username=jnp2@meetit.pl&password=Jnp2jnp2$&contentType=text/plain");
    }
}
