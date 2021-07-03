package eu.tutoring.coordinator.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import eu.tutoring.coordinator.data.models.PhysicalHomeAddress;

import java.io.FileNotFoundException;


public abstract class Invoice {
    protected static final Font font = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
    protected static final Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);

    protected Document document;

    public Invoice() {
        this.document = null;
    }

    public abstract void generate() throws DocumentException, FileNotFoundException;

    private boolean isGenerated() {
        return document != null;
    }

    public Document getDocument() throws DocumentNotGenerated {
        if (!isGenerated()) {
            throw new DocumentNotGenerated();
        }

        return document;
    }
}
