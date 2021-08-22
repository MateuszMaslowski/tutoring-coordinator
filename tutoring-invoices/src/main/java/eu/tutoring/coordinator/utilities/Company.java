package eu.tutoring.coordinator.utilities;

public class Company {
    private static final String name = "Super Company";

    private static final String address = "ul. Banacha 2, 02-097 Warszawa";

    private static final String city = "Warsaw";

    private static final String bankAccountNumber = "11 1111 1111 1111 1111 1111 1111";

    private static final String taxId = "111-111-11-11";

    private static final String bankName = "CoolBank";

    public static String getName() {
        return name;
    }

    public static String getCity() {
        return city;
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

    public static String getAddress() {
        return address;
    }
}
