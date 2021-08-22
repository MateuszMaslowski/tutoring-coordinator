package eu.tutoring.coordinator.utilities;

import org.springframework.data.util.Pair;

import java.util.ArrayList;

public class TutoringCurrentInfo {
    private ArrayList<String> tutorsNames;
    private ArrayList<String> tutorsEmails;
    private ArrayList<ArrayList<String>> tutees;

    public TutoringCurrentInfo(ArrayList<String> tutorsNames, ArrayList<String> tutorsEmails, ArrayList<ArrayList<String>> tutees) {
        this.tutorsNames = tutorsNames;
        this.tutorsEmails = tutorsEmails;
        this.tutees = tutees;
    }

    public ArrayList<String> getTutorsNames() {
        return tutorsNames;
    }

    public ArrayList<String> getTutorsEmails() {
        return tutorsEmails;
    }

    public ArrayList<ArrayList<String>> getTutees() {
        return tutees;
    }
}
