package arguments_provider;

public class UserInputRequesterRunner {

    private UserInputRequesterRunner() { }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        OptionsProvider op = new UserInputOptionsProvider();
        MessagingController controller = new MessagingController(op);
        controller.start();
    }
}
