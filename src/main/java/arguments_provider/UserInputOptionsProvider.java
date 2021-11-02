package arguments_provider;

import java.util.Scanner;

/**
 * Interface specifies all properties an OptionsProvider should serve.
 *
 * @author michiel
 */
public class UserInputOptionsProvider implements OptionsProvider {
    private String fileName;

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
    }

    /**
     * fetches and sets the user name.
     * @param scanner the scanner
     */
    private void fetchFileName(final Scanner scanner) {
        System.out.println("Please enter the path to your csv or arff file > ");
        this.fileName = scanner.next();

    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
