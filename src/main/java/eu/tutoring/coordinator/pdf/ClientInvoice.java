package eu.tutoring.coordinator.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import eu.tutoring.coordinator.data.models.Tutoring;
import eu.tutoring.coordinator.data.repositories.MonthlyLessonsRepository;
import eu.tutoring.coordinator.data.repositories.TutoringRepository;
import eu.tutoring.coordinator.data.models.Parent;
import eu.tutoring.coordinator.data.models.Tutee;
import eu.tutoring.coordinator.utilities.Company;
import eu.tutoring.coordinator.data.models.PhysicalHomeAddress;
import eu.tutoring.coordinator.utilities.GlobalProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;


public class ClientInvoice extends Invoice {
    private Tutee tutee;
    private TutoringRepository tutoringRepository;
    private MonthlyLessonsRepository monthlyLessonsRepository;

    private String FILE;

    private boolean generated;


    public ClientInvoice(Tutee tutee, TutoringRepository tutoringRepository,
                         MonthlyLessonsRepository monthlyLessonsRepositry) {
        super();

        this.tutee = tutee;
        this.tutoringRepository = tutoringRepository;
        this.monthlyLessonsRepository = monthlyLessonsRepositry;

        this.generated = false;
    }

    @Override
    public void generate() throws DocumentException, FileNotFoundException {
        FILE = GlobalProjectProperties.getFilePath();

        FILE += "[" + tutee.getFullName() + "] invoice for " + YearMonth.now().getMonthValue()
                + "." + YearMonth.now().getYear()
        + ".pdf";
        document = new Document(PageSize.A4, 50, 50, 50, 50);

        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();

        addDate();
        addPartiesData();
        addInvoiceNumber();
        addTransactions();
        addFooter();

        document.close();

        generated = true;
    }

    public String getDocumentFilePath() throws DocumentNotGenerated {
        if (!generated) {
            throw new DocumentNotGenerated();
        }

        return FILE;
    }

    private void addDate() throws DocumentException {
        Paragraph date = new Paragraph(
                Company.getAddress().getCity() + ", " + LocalDateTime.now().getDayOfMonth() + " "
                        + LocalDateTime.now().getMonth() + " " + LocalDateTime.now().getYear(),
                font
        );

        date.setAlignment(Element.ALIGN_RIGHT);

        document.add(date);
    }

    private void addPartiesData() throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        String clientsName;
        PhysicalHomeAddress clientsAddress;

        if (tutee.isAdult()) {
            clientsName = tutee.getFullName();
            clientsAddress = tutee.getAddress();
        }
        else {
            Parent parent = tutee.getParent();

            clientsName = parent.getFullName();
            clientsAddress = parent.getAddress();
        }

        table.addCell(Company.getName());
        table.addCell(clientsName);

        table.addCell(Company.getAddress().toString());
        table.addCell(clientsAddress.toString());

        table.addCell("Tax ID: " + Company.getTaxId());
        table.addCell("");

        for (PdfPRow row : table.getRows()) {
            if (row == null) {
                continue;
            }

            for (PdfPCell cell : row.getCells()) {
                if (cell == null) {
                    continue;
                }
                cell.setPaddingLeft(10);
                cell.setPaddingRight(10);
                cell.setBorderColor(BaseColor.WHITE);
            }
        }

        table.setSpacingBefore(20);
        table.setWidthPercentage(100);

        document.add(table);
    }

    private void addInvoiceNumber() throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");


        Paragraph invoiceNumber = new Paragraph(
                "INVOICE NO. " + tutee.getInvoiceId() + "/" + LocalDateTime.now().getMonthValue()
                        + "/" + LocalDateTime.now().getYear(),
                boldFont
        );

        invoiceNumber.setAlignment(Element.ALIGN_CENTER);

        document.add(invoiceNumber);
    }

    private void addTransactions() throws DocumentException {
        PdfPTable table = new PdfPTable(6);

        //First row
        table.addCell("No.");
        table.addCell("Service name");
        table.addCell("Unit");
        table.addCell("Number of units");
        table.addCell("Price per unit");
        table.addCell("Total");

        List<Tutoring> tutoringList = tutoringRepository.findByTutee(tutee);

        Integer counter = 1, total = 0;
        for (Tutoring tutoring : tutoringList) {
            table.addCell(counter.toString());
            counter++;

            table.addCell(tutoring.getSubject().toString() + " tutoring");

            table.addCell("h");

            Integer numberOfLessons = tutoring.getNumberOfLessonsByYearMonth(monthlyLessonsRepository, YearMonth.now());
            table.addCell(numberOfLessons.toString());

            Integer pricePerLesson = tutoring.getPricePerLesson();
            table.addCell(pricePerLesson.toString());

            Integer localTotal = numberOfLessons * pricePerLesson;
            table.addCell(localTotal.toString());

            total += localTotal;
        }

        //Last row
        table.addCell("Total");
        for (int i = 0; i < 4; ++i) {
            table.addCell("");
        }
        table.addCell(total.toString());

        for (PdfPRow row : table.getRows()) {
            if (row == null) {
                continue;
            }

            for (PdfPCell cell : row.getCells()) {
                if (cell == null) {
                    continue;
                }
                cell.setPaddingLeft(10);
                cell.setPaddingRight(10);
                cell.setPaddingTop(3);
                cell.setPaddingBottom(3);
            }
        }

        table.setSpacingBefore(20);
        table.setWidthPercentage(100);

        document.add(table);
    }

    private void addFooter() throws DocumentException {
        Paragraph paymentMethod = new Paragraph(
                "Payment method: Bank transfer",
                font
        );

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        LocalDateTime localDateTime = LocalDateTime.now().plusDays(10);

        Paragraph paymentPeriod = new Paragraph(
                "Payment period: " + localDateTime.getDayOfMonth() + " " + localDateTime.getDayOfMonth()
                        + " " + localDateTime.getYear(),
                font
        );

        Paragraph bank = new Paragraph(
                "Bank: " + Company.getBankName(),
                font
        );

        Paragraph accountNumber = new Paragraph(
                "Account number: " + Company.getBankAccountNumber(),
                font
        );

        document.add(paymentMethod);
        document.add(paymentPeriod);
        document.add(bank);
        document.add(accountNumber);
    }
}
