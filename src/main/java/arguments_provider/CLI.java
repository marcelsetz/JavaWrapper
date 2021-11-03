package arguments_provider;

import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class CLI implements OptionsProvider {

    private final String[] clArguments;
    private Options options;
    private CommandLine commandLine;
    public String fileName;
    public boolean toCSV;

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
        Option outputFormat = new Option("a", "to-arff", false, "The format in which the output will be written (if not given the output will be csv)");
        Option helpOption = new Option("h", "help", false, "Prints this message");

        options.addOption(file);
        options.addOption(outputFormat);
        options.addOption(helpOption);
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
                this.fileName = f;
                if (isLegalFile(f)) {
                    System.out.println(StringUtils.repeat("=", 100) + "\nFile is Valid");
                } else {
                    fileName = null;
                    System.out.println(StringUtils.repeat("=", 100) + "\nfile extension is not legal: \"" + f + "\""
                            + " must be either .csv or .arff");
                }
            } else if (!helpRequested()){
                System.out.println(StringUtils.repeat("=", 100) + "\nPlease provide a csv or arff file (-f) with all of the needed instances (found in the ReadMe.md)");
            }
            toCSV = !commandLine.hasOption("to-arff");
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

    }

    private boolean isLegalFile(String f) {
        String ext = FilenameUtils.getExtension(f);
        return Objects.equals(ext, "csv") || Objects.equals(ext, "arff");
    }
    /**
     * prints help.
     */
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("... -f [arff or csv file]", options);
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public boolean getOutputFormat() {
        return this.toCSV;
    }


}
