package arguments_provider;

import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

import java.util.Objects;

public class CLI implements OptionsProvider {

    private final String[] clArguments;
    private Options options;
    private CommandLine commandLine;
    public String fileName;

    public CLI(final String[] args) {
        this.clArguments = args;
        initialize();
    }

    /**
     * Options initialization and processing.
     */
    private void initialize() {
        buildOptions();
        processCommandLine();
    }

    /**
     * check if help was requested; if so, return true.
     * @return helpRequested
     */
    public boolean helpRequested() {
        return this.commandLine.hasOption("help");
    }

    /**
     * builds the Options object.
     */
    private void buildOptions() {
        // create Options object
        this.options = new Options();
        Option file = new Option("f", "file", true, "file with new data");

        options.addOption(file);
    }

    /**
     * processes the command line arguments.
     */
    private void processCommandLine() {
        try {
            CommandLineParser parser = new DefaultParser();
            this.commandLine = parser.parse(this.options, this.clArguments);

            if (commandLine.hasOption("file")) {
                String f = commandLine.getOptionValue("file").trim();
                if (isLegalFile(f)) {
                    String fileName = this.commandLine.getOptionValue("file");
                    System.out.println("File is Valid");
                } else {
                    throw new IllegalArgumentException("file extension is not legal: \"" + f + "\""
                            + "must be either csv or arff");
                }
            } else {
                System.out.println("Please provide a csv or arff file with all of the needed instances (found in the ReadMe.md");
            }

        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private boolean isLegalFile(String f) {
        String ext = FilenameUtils.getExtension(f);
        return Objects.equals(ext, ".csv") || Objects.equals(ext, ".arff");
    }
    /**
     * prints help.
     */
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyCoolTool", options);
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }


}
