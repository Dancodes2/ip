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
     * @param args Command-line arguments, which are not used.
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

        // Keeps reading commands until the user ends the conversation or input is exhausted.
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();

            // Prints the ending message and stops when the user enters the exit command.
            if (userInput.equals("bye")) {
                System.out.print("""
                        %s
                        %s""".formatted(separator, ending));
                break;
            }

            // Splits input into a command and task number by spaces.
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

                // Converts the task number; invalid text throws this exception.
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

                    // Marks or unmarks the selected task based on the command.
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

            // Removes the selected task from the list.
            if (command.equals("delete")) {
                if (commandParts.length < 2) {
                    System.out.print("""
                            %s
                            Please provide a task number.
                            %s

                            """.formatted(separator, separator));
                    continue;
                }

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

                    Task removedTask = tasks.remove(taskNumber - 1);
                    System.out.print("""
                            %s
                            Noted. I've removed this task:
                              %s
                            Now you have %d tasks in the list.
                            %s

                            """.formatted(separator, removedTask, tasks.size(), separator));
                } catch (NumberFormatException e) {
                    System.out.print("""
                            %s
                            Please enter a whole number for the task number.
                            %s

                            """.formatted(separator, separator));
                }
                continue;
            }

            // Displays all stored tasks when the list command is entered.
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

            // Stores valid task commands and catches parsing errors.
            try {
                Task newTask = parseTask(userInput);
                tasks.add(newTask);
                System.out.print("""
                        %s
                        Got it. I've added this task:
                          %s
                        Now you have %d tasks in the list.
                        %s

                        """.formatted(separator, newTask, tasks.size(), separator));
            } catch (SlotBotException e) {
                System.out.print("""
                        %s
                        %s
                        %s

                        """.formatted(separator, e.getMessage(), separator));
            }
        }
    }

    /**
     * Determines the task type from a user command.
     *
     * @param userInput The command entered by the user.
     * @return The task created from the valid command.
     * @throws SlotBotException If the command or its arguments are invalid.
     */
    private static Task parseTask(String userInput) throws SlotBotException {
        String[] arguments = userInput.trim().split("\\s+", 2);
        String command = arguments[0];

        if (command.equals("todo")) {
            if (arguments.length < 2 || arguments[1].isBlank()) {
                throw new SlotBotException("The description of a todo cannot be empty.\n"
                        + "Use: todo DESCRIPTION");
            }
            return new Todo(arguments[1]);
        }

        if (command.equals("deadline")) {
            if (arguments.length < 2 || arguments[1].isBlank()) {
                throw new SlotBotException("The description of a deadline cannot be empty.\n"
                        + "Use: deadline DESCRIPTION /by DATE");
            }

            String[] sentenceDeadline = arguments[1].split(" /by ", 2);
            if (sentenceDeadline.length < 2
                    || sentenceDeadline[0].isBlank()
                    || sentenceDeadline[1].isBlank()) {
                throw new SlotBotException("Use: deadline DESCRIPTION /by DATE");
            }

            String description = sentenceDeadline[0];
            String by = sentenceDeadline[1];
            return new Deadline(description, by);
        }

        if (command.equals("event")) {
            if (arguments.length < 2 || arguments[1].isBlank()) {
                throw new SlotBotException("The description of an event cannot be empty.\n"
                        + "Use: event DESCRIPTION /from START /to END");
            }

            String[] sentenceEvent = arguments[1].split(" /from ", 2);
            if (sentenceEvent.length < 2
                    || sentenceEvent[0].isBlank()
                    || sentenceEvent[1].isBlank()) {
                throw new SlotBotException("Use: event DESCRIPTION /from START /to END");
            }

            String description = sentenceEvent[0];
            String[] datesEvent = sentenceEvent[1].split(" /to ", 2);
            if (datesEvent.length < 2
                    || datesEvent[0].isBlank()
                    || datesEvent[1].isBlank()) {
                throw new SlotBotException("Use: event DESCRIPTION /from START /to END");
            }

            String from = datesEvent[0];
            String to = datesEvent[1];
            return new Event(description, from, to);
        }

        throw new SlotBotException("I don't recognise that command.\n"
                + "Try: todo DESCRIPTION, deadline DESCRIPTION /by DATE,\n"
                + "event DESCRIPTION /from START /to END, list, mark NUMBER,\n"
                + "unmark NUMBER, or bye.");
    }
}
