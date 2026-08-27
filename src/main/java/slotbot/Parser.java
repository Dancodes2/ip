package slotbot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import slotbot.task.Deadline;
import slotbot.task.Event;
import slotbot.task.Task;
import slotbot.task.Todo;

/**
 * Interprets user input as SlotBot commands and task details.
 */
public final class Parser {
    private static final DateTimeFormatter EVENT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    private Parser() {
    }

    /**
     * Returns the command type represented by the user input.
     *
     * @param userInput Command entered by the user.
     * @return Matching command type, or UNKNOWN if there is no match.
     */
    public static CommandType parseCommandType(String userInput) {
        String[] commandParts = userInput.trim().split("\\s+", 2);
        return CommandType.fromText(commandParts[0]);
    }

    /**
     * Parses a task number and returns its zero-based index.
     *
     * @param userInput Command containing the task number.
     * @param taskCount Number of tasks in the list.
     * @return Zero-based index of the selected task.
     * @throws SlotBotException If the task number is missing, invalid, or out of range.
     */
    public static int parseTaskNumber(String userInput, int taskCount) throws SlotBotException {
        String[] commandParts = userInput.trim().split("\\s+", 2);
        String command = commandParts[0];

        if (commandParts.length < 2) {
            throw new SlotBotException("Please provide a task number.\n"
                    + "Use: " + command + " [NUMBER]");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(commandParts[1]);
        } catch (NumberFormatException e) {
            throw new SlotBotException("Please enter a whole number for the task number.\n"
                    + "Use: " + command + " [NUMBER]");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new SlotBotException("That task number does not exist.\n"
                    + "Use: " + command + " [NUMBER]");
        }

        return taskNumber - 1;
    }

    /**
     * Creates a task from a user command.
     *
     * @param userInput Command entered by the user.
     * @param commandType Type of the entered command.
     * @return Task created from the valid command.
     * @throws SlotBotException If the command or its arguments are invalid.
     */
    public static Task parseTask(String userInput, CommandType commandType) throws SlotBotException {
        String[] arguments = userInput.trim().split("\\s+", 2);

        switch (commandType) {
        case TODO: {
            if (arguments.length < 2 || arguments[1].isBlank()) {
                throw new SlotBotException("The description of a todo cannot be empty.\n"
                        + "Use: todo DESCRIPTION");
            }
            return new Todo(arguments[1]);
        }

        case DEADLINE: {
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
            try {
                LocalDate by = LocalDate.parse(sentenceDeadline[1]);
                return new Deadline(description, by);
            } catch (DateTimeParseException e) {
                throw new SlotBotException("The deadline date must use yyyy-MM-dd.\n"
                        + "Use: deadline DESCRIPTION /by yyyy-MM-dd");
            }
        }

        case EVENT: {
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

            try {
                LocalDateTime from = LocalDateTime.parse(datesEvent[0], EVENT_DATE_TIME_FORMATTER);
                LocalDateTime to = LocalDateTime.parse(datesEvent[1], EVENT_DATE_TIME_FORMATTER);
                return new Event(description, from, to);
            } catch (DateTimeParseException e) {
                throw new SlotBotException("The event times must use yyyy-MM-dd HH:mm.\n"
                        + "Use: event DESCRIPTION /from yyyy-MM-dd HH:mm /to yyyy-MM-dd HH:mm");
            }
        }

        default:
            throw new SlotBotException("I don't recognise that command.\n"
                    + "Try: todo DESCRIPTION, deadline DESCRIPTION /by DATE,\n"
                    + "event DESCRIPTION /from START /to END, list, mark [NUMBER],\n"
                    + "unmark [NUMBER], or bye.");
        }
    }
}
