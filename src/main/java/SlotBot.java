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
                System.out.print("""
                        %s
                        %s""".formatted(separator, ending));
                break;
            }

            // Handles commands that update a task's completion status.
            // Splits input to command and task number by spaces.
            String[] commandParts = userInput.trim().split("\\s+", 2);
            String command = commandParts[0];
            if (command.equals("mark") || command.equals("unmark")) {
                if (commandParts.length < 2) {
                    System.out.print("""
                            %s
                            Please provide a task number.
                            %s

                            """.formatted(separator, separator));
                    continue;
                }

                // Convert the task number to an integer, invalid text throws this exception.
                try {
                    int taskNumber = Integer.parseInt(commandParts[1]);
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        System.out.print("""
                                %s
                                That task number does not exist.
                                %s

                                """.formatted(separator, separator));
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

                    String markMessage = shouldMark
                            ? "Nice! We got one."
                            : "OK, I've marked this task as not done yet:";
                    System.out.print("""
                            %s
                            %s
                              %s
                            %s

                            """.formatted(separator, markMessage, selectedTask, separator));
                } catch (NumberFormatException e) {
                    System.out.print("""
                            %s
                            Please enter a whole number for the task number.
                            %s

                            """.formatted(separator, separator));
                }
                continue;
            }

            // Display all stored tasks when the list command is entered.
            if (userInput.equals("list")) {
                System.out.print("""
                        %s
                        Here are the tasks in your list:
                        """.formatted(separator));
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.print("""
                        %s

                        """.formatted(separator));
                continue;
            }

            // Store every other command as a task in the order it was entered.
            Task newTask = parseTask(userInput);
            tasks.add(newTask);
            System.out.print("""
                    %s
                    Got it. I've added this task:
                      %s
                    Now you have %d tasks in the list.
                    %s

                    """.formatted(separator, newTask, tasks.size(), separator));
        }
    }

    // Helper to determine task type.
    private static Task parseTask(String userInput) {
        String[] arguments = userInput.trim().split("\\s+", 2);
        String command = arguments[0];

        if (command.equals("todo") && arguments.length == 2) {
            return new Todo(arguments[1]);
        }

        if (command.equals("deadline") && arguments.length == 2) {
            String[] sentenceDeadline = arguments[1].split(" /by ", 2);
            if (sentenceDeadline.length == 2) {
                String description = sentenceDeadline[0];
                String by = sentenceDeadline[1];
                return new Deadline(description, by);
            }
        }

        if (command.equals("event") && arguments.length == 2) {
            String[] sentenceEvent = arguments[1].split(" /from ", 2);
            if (sentenceEvent.length == 2) {
                String description = sentenceEvent[0];
                String[] datesEvent = sentenceEvent[1].split(" /to ", 2);
                if (datesEvent.length == 2) {
                    String from = datesEvent[0];
                    String to = datesEvent[1];
                    return new Event(description, from, to);
                }
            }
        }

        return new Todo(userInput);
    }
}
