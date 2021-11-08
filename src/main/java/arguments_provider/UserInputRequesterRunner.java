package arguments_provider;

public class UserInputRequesterRunner {

    private UserInputRequesterRunner() { }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        OptionsProvider op = new UserInputOptionsProvider();
        String fileName = op.getFileName();
        boolean toCSV = op.getOutputFormat();
        boolean isSmokeLabel = op.getClassLabel();

        MessagingController controller = new MessagingController(op, fileName, toCSV, isSmokeLabel);
        controller.start();
    }
}
