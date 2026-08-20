import java.util.Scanner;

/**
 * The main entry point for SlotBot.
 */
public class SlotBot {
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

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            if (userInput.equals("bye")) {
                System.out.print(ending);
                break;
            }

            System.out.println(userInput);
            System.out.println(separator);
        }
    }
}
