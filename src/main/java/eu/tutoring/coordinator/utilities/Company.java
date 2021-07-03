package eu.tutoring.coordinator.utilities;

import eu.tutoring.coordinator.data.models.PhysicalHomeAddress;

public class Company {
    private static final String name = "Super Company";

    private static final PhysicalHomeAddress address = new PhysicalHomeAddress("Banacha 2", 15,
            "Warsaw", "Poland", "02-001");

    private static final String bankAccountNumber = "11 1111 1111 1111 1111 1111 1111";

    private static final String taxId = "111-111-11-11";

    private static final String bankName = "CoolBank";

    public static String getName() {
        return name;
    }

    public static PhysicalHomeAddress getAddress() {
        return address;
    }

    public static String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public static String getTaxId() {
        return taxId;
    }

    public static String getBankName() {
        return bankName;
    }
}
