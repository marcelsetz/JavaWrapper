package arguments_provider;

import java.util.Arrays;

public final class CommandLineArgsRunner {

    /**
     * private constructor because this class is not supposed to be instantiated.
     */
    private CommandLineArgsRunner() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        try {
            CLI op = new CLI(args);
            String fileName = op.getFileName();
            boolean toCSV = op.getOutputFormat();
            if (op.helpRequested()) {
                op.printHelp();
                return;
            }

            MessagingController controller = new MessagingController(op, fileName, toCSV);
            controller.start();
        } catch (IllegalStateException ex) {
            System.err.println("Something went wrong while processing your command line \""
                    + Arrays.toString(args) + "\"");
            System.err.println("Parsing failed.  Reason: " + ex.getMessage());
            CLI op = new CLI(new String[]{});
            op.printHelp();
        }
    }
}

