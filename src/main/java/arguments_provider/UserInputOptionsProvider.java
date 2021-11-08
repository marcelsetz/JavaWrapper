package arguments_provider;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
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
    private boolean isSmokeLabel;

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
        fetchClassLabel(scanner);
    }

    /**
     * fetches and sets the user name.
     * @param scanner the scanner
     */
    private void fetchFileName(final Scanner scanner) {
        System.out.println("Please enter the path to your csv or arff file > ");
        this.fileName = scanner.next();

        File newFile = new File(fileName);

        String ext = FilenameUtils.getExtension(fileName);
        if (Objects.equals(ext, "csv") || Objects.equals(ext, "arff")) {
            System.out.println(ext + " is a valid file extension for this program!");
        } else if (Objects.equals(ext, "")) {
            System.out.println("Please enter a valid file format");
            fetchFileName(scanner);
        } else if (!newFile.exists()) {
            System.out.println("The given file does not exist! Please try again.");
            fetchFileName(scanner);
        } else {
            System.out.println(ext + " is not a valid file extension for this program");
            fetchFileName(scanner);
        }

    }

    private void fetchOutputFormat(final Scanner scanner) {
        System.out.println("Do you want the file to be csv or arff > ");
        String output = scanner.next();
        if (Objects.equals(output, "csv")) {
            this.toCSV = true;
        } else if (Objects.equals(output, "arff")) {
            this.toCSV = false;
        } else {
            System.out.println("Please enter either csv or arff");
            fetchOutputFormat(scanner);
        }
    }

    private void fetchClassLabel(final Scanner scanner) {
        System.out.println("Do you want the smoking data (s) or the gender data (g) as output class > ");
        String classLabel = scanner.next();
        if (Objects.equals(classLabel, "s") || Objects.equals(classLabel, "smoke")) {
            this.isSmokeLabel = true;
        } else if (Objects.equals(classLabel, "g") || Objects.equals(classLabel, "gender")) {
            this.isSmokeLabel = false;
        } else {
            System.out.println("Please enter either smoke (or s) or gender (or g)");
            fetchClassLabel(scanner);
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean getOutputFormat() {
        return toCSV;
    }

    @Override
    public boolean getClassLabel() {
        return isSmokeLabel;
    }
}
