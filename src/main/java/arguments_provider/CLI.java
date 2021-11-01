package arguments_provider;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CLI {

    public static void main(String[] args) {

    }

    private void buildOptions() {
        Options options = new Options();
        Option file = new Option("f", "file", false, "file with new data");
        Option island = new Option("i", "island", false, "CpG island following by methylation rate");
        Option methylation_rate = new Option("r", "rate", false, "Methylation rate of previous given CpG island");

        options.addOption(file);
        options.addOption(island);
        options.addOption(methylation_rate);
    }
}
