import java.util.ArrayList;
import java.util.List;
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
        List<String> tasks = new ArrayList<>();

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

            // Display all stored tasks when the list command is entered.
            if (userInput.equals("list")) {
                System.out.println(separator);
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.println(separator);
                System.out.println();
                continue;
            }

            // Store every other command as a task in the order it was entered.
            tasks.add(userInput);
            System.out.println(separator);
            System.out.println("added: " + userInput);
            System.out.println(separator);
            System.out.println();
        }
    }
}
