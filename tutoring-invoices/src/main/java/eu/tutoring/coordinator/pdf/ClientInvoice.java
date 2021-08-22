package eu.tutoring.coordinator.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import eu.tutoring.coordinator.utilities.ClientInvoiceData;
import eu.tutoring.coordinator.utilities.Company;
import eu.tutoring.coordinator.utilities.GlobalProjectProperties;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;


public class ClientInvoice extends Invoice {

    private String FILE;

    private String fileName;

    private ClientInvoiceData clientInvoiceData;

    private boolean generated;

    public ClientInvoice(ClientInvoiceData clientInvoiceData) {
        super();

        this.clientInvoiceData = clientInvoiceData;

        this.generated = false;
    }

    @Override
    public void generate() throws DocumentException, FileNotFoundException {
        FILE = GlobalProjectProperties.getFilePath();

        fileName = "[" + clientInvoiceData.getTuteeName() + "] invoice for " + YearMonth.now().getMonthValue()
                + "." + YearMonth.now().getYear() + ".pdf";

        FILE += fileName;

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

    public String getFileName() {
        return fileName;
    }

    private void addDate() throws DocumentException {
        Paragraph date = new Paragraph(
                Company.getCity() + ", " + LocalDateTime.now().getDayOfMonth() + " "
                        + LocalDateTime.now().getMonth() + " " + LocalDateTime.now().getYear(),
                font
        );

        date.setAlignment(Element.ALIGN_RIGHT);

        document.add(date);
    }

    private void addPartiesData() throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        String clientsName = clientInvoiceData.getPayersName();
        String clientsAddress = clientInvoiceData.getPayerAddress();

        table.addCell(Company.getName());
        table.addCell(clientsName);

        table.addCell(Company.getAddress());
        table.addCell(clientsAddress);

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
                "INVOICE NO. " + clientInvoiceData.getInvoiceId() + "/" + LocalDateTime.now().getMonthValue()
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

        Integer counter = 1, total = 0;

        for (int i = 0; i < clientInvoiceData.getNumberOfTutorings(); ++i) {
            table.addCell(counter.toString());
            counter++;

            table.addCell(clientInvoiceData.getSubjects().get(i) + " tutoring");

            table.addCell("h");

            Integer numberOfLessons = clientInvoiceData.getNumberOfLessons().get(i);
            table.addCell(numberOfLessons.toString());

            Integer pricePerLesson = clientInvoiceData.getPricePerLesson().get(i);
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
