package eu.tutoring.coordinator.utilities;

public class GlobalProjectProperties {
    private static final String filePath = "/Users/user/Desktop/Informatyka/IIrok/JNPII/coordinator/invoices/";

    private static final String tuteesSampleEmail = "mateusz.maslowski.prof@gmail.com";

    public static String getFilePath() {
        return filePath;
    }

    public static String getTuteesSampleEmail() {
        return tuteesSampleEmail;
    }
}
