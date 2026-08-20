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
        List<Task> tasks = new ArrayList<>();

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

            // Handles commands that update a task's completion status.
            // Splits input to command and task number by spaces.
            String[] commandParts = userInput.trim().split("\\s+", 2);
            String command = commandParts[0];
            if (command.equals("mark") || command.equals("unmark")) {
                if (commandParts.length < 2) {
                    System.out.println(separator);
                    System.out.println("Please provide a task number.");
                    System.out.println(separator);
                    System.out.println();
                    continue;
                }

                // Convert the task number to an integer, invalid text throws this exception.
                try {
                    int taskNumber = Integer.parseInt(commandParts[1]);
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        System.out.println(separator);
                        System.out.println("That task number does not exist.");
                        System.out.println(separator);
                        System.out.println();
                        continue;
                    }

                    Task selectedTask = tasks.get(taskNumber - 1);

                    // Mark or unmark based on command.
                    boolean shouldMark = command.equals("mark");
                    if (shouldMark) {
                        selectedTask.markDone();
                    } else {
                        selectedTask.markUndone();
                    }

                    System.out.println(separator);
                    if (shouldMark) {
                        System.out.println("Nice! We got one.");
                    } else {
                        System.out.println("OK, I've marked this task as not done yet:");
                    }
                    System.out.println("  " + selectedTask);
                    System.out.println(separator);
                    System.out.println();
                } catch (NumberFormatException e) {
                    System.out.println(separator);
                    System.out.println("Please enter a whole number for the task number.");
                    System.out.println(separator);
                    System.out.println();
                }
                continue;
            }

            // Display all stored tasks when the list command is entered.
            if (userInput.equals("list")) {
                System.out.println(separator);
                System.out.println("Here's what's on your schedule:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.println(separator);
                System.out.println();
                continue;
            }

            // Store every other command as a task in the order it was entered.
            tasks.add(new Todo(userInput));
            System.out.println(separator);
            System.out.println("Got it. I've made room for this task:");
            System.out.println("  " + userInput);
            System.out.println(separator);
            System.out.println();
        }
    }
}
