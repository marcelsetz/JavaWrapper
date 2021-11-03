package arguments_provider;

import java.util.Objects;
import java.util.Scanner;

/**
 * Interface specifies all properties an OptionsProvider should serve.
 *
 * @author michiel
 */
public class UserInputOptionsProvider implements OptionsProvider {
    private String fileName;
    private boolean toCSV;

    /**
     * default constructor.
     */
    public UserInputOptionsProvider() {
        initialize();
    }

    /**
     * greedy instantiation of this object; it will try to obtain all relevant data from the ser immediately.
     */
    private void initialize() {
        Scanner scanner = new Scanner(System.in);
        fetchFileName(scanner);
        fetchOutputFormat(scanner);
    }

    /**
     * fetches and sets the user name.
     * @param scanner the scanner
     */
    private void fetchFileName(final Scanner scanner) {
        System.out.println("Please enter the path to your csv or arff file > ");
        this.fileName = scanner.next();

    }

    private void fetchOutputFormat(final Scanner scanner) {
        System.out.println("Do you want the file to be csv or arff > ");
        if (!Objects.equals(scanner.next(), "csv") || !Objects.equals(scanner.next(), "arff")) {
            System.out.println("\nPlease enter either csv or arff.");
        }
        this.toCSV = Objects.equals(scanner.next(), "csv");

    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean getOutputFormat() {
        return toCSV;
    }
}
