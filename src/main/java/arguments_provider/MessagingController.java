package arguments_provider;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class MessagingController {
    private final OptionsProvider optionsProvider;
    String fileName;
    boolean toCSV;
    boolean isSmokeLabel;

    /**
     * the constructor need an OptionsProvider to be able to do its work.
     * @param optionsProvider the options provider
     */
    public MessagingController(final OptionsProvider optionsProvider, String fileName, boolean toCSV, boolean isSmokeLabel) {
        this.optionsProvider = optionsProvider;
        this.fileName = fileName;
        this.toCSV = toCSV;
        this.isSmokeLabel = isSmokeLabel;
    }

    /**
     * Starts the application logic.
     */
    public void start() {
        if (fileName == null) {
            System.out.println("Program Stopping...\n" + StringUtils.repeat("=", 100));
            System.exit(0);
        }

        printUserSettings();

        System.out.println("Program Running...\n");
        if(!toCSV) {
            System.out.println(StringUtils.repeat("=", 100));
        }

        WekaRunner weka = new WekaRunner();
        weka.RunWeka(fileName, toCSV, isSmokeLabel);

    }

    /**
     * prints all application settings.
     */
    private void printUserSettings() {
        String fileName = optionsProvider.getFileName();
        String ext = FilenameUtils.getExtension(fileName);
        System.out.println("The file you're trying to open is... " + fileName + "\n");

        if (Objects.equals(ext, "csv") || Objects.equals(ext, "arff")) {
            System.out.println(ext + " is a valid file extension for this program!");
        } else {
            System.out.println(ext + " is not a valid file extension for this program");
        }

        if (toCSV) {
            System.out.println("Output Format: csv\n");
        } else {
            System.out.println("Output format: arff\n");
        }

        if (isSmokeLabel) {
            System.out.println("Class label: Smoking.Status");
        } else {
            System.out.println("Class label: Gender");
        }
    }
}
