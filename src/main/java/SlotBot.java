import java.util.Scanner;

/**
 * The main entry point for SlotBot.
 */
public class SlotBot {
    /**
     * Starts SlotBot and processes user commands until the user enters bye.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String separator = "____________________________________________________________";
        String greeting = """
                Hello! I'm SlotBot.
                Let's keep your time and tasks in order.
                ____________________________________________________________
                """;
        String ending = """
                All done. See you next time!
                ____________________________________________________________
                """;

        System.out.print(greeting);

        Scanner scanner = new Scanner(System.in);

        // Keep reading commands until the user ends the conversation or input is exhausted.
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            // Print the ending message and stop when the user enters the exit command.
            if (userInput.equals("bye")) {
                System.out.println(separator);
                System.out.print(ending);
                break;
            }

            // Echo each non-exit command so the user can see what SlotBot received.
            System.out.println(separator);
            System.out.println(userInput);
            System.out.println(separator);
            System.out.println();

        }
    }
}
