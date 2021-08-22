package eu.tutoring.coordinator.utilities;

import java.util.List;

public class ClientInvoiceData {
    private String tuteeName;
    private String tuteeEmailAddress;

    private String payersName;
    private String payerAddress;

    private String invoiceId;

    private List<String> subjects;
    private List<Integer> numberOfLessons;
    private List<Integer> pricePerLesson;

    private int numberOfTutorings;

    public ClientInvoiceData(String tuteeName, String tuteeEmailAddress, String payersName, String payerAddress, String invoiceId, List<String> subjects, List<Integer> numberOfLessons, List<Integer> pricePerLesson, int numberOfTutorings) {
        this.tuteeName = tuteeName;
        this.tuteeEmailAddress = tuteeEmailAddress;
        this.payersName = payersName;
        this.payerAddress = payerAddress;
        this.invoiceId = invoiceId;
        this.subjects = subjects;
        this.numberOfLessons = numberOfLessons;
        this.pricePerLesson = pricePerLesson;
        this.numberOfTutorings = numberOfTutorings;
    }

    public String getTuteeName() {
        return tuteeName;
    }

    public String getPayersName() {
        return payersName;
    }

    public String getPayerAddress() {
        return payerAddress;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<Integer> getNumberOfLessons() {
        return numberOfLessons;
    }

    public List<Integer> getPricePerLesson() {
        return pricePerLesson;
    }

    public int getNumberOfTutorings() {
        return numberOfTutorings;
    }

    public String getTuteeEmailAddress() {
        return tuteeEmailAddress;
    }
}

