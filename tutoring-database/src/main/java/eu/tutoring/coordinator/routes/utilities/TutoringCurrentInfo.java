package eu.tutoring.coordinator.routes.utilities;

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
}
