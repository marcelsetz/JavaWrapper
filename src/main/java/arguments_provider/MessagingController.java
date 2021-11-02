package arguments_provider;

import org.apache.commons.io.FilenameUtils;

import java.util.Objects;

public class MessagingController {
    private final OptionsProvider optionsProvider;

    /**
     * the constructor need an OptionsProvider to be able to do its work.
     * @param optionsPovider the options provider
     */
    public MessagingController(final OptionsProvider optionsPovider) {
        this.optionsProvider = optionsPovider;
    }

    /**
     * Starts the application logic.
     */
    public void start() {
        if (optionsProvider == null) {
            throw new IllegalStateException("Please provide valid options. For help type -h");
        }

        printUserSettings();

        //application logic here
        System.out.println("Program Running...");
    }

    /**
     * prints all application settings.
     */
    private void printUserSettings() {
        String fileName = optionsProvider.getFileName();
        String ext = FilenameUtils.getExtension(fileName);
        System.out.println("The file you're trying to open is... " + optionsProvider.getFileName());

        if (Objects.equals(ext, ".csv") || Objects.equals(ext, ".arff")) {
            System.out.println(ext + " is a valid file extension for this program!");
        } else {
            System.out.println(ext + " is not a valid file extension for this program");
        }
    }
}
