import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The main entry point for SlotBot.
 */
public class SlotBot {
    private static final Path SAVE_FILE_PATH = Path.of("data", "slotbot.txt");
    private static final DateTimeFormatter EVENT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Starts SlotBot and processes user commands until the user enters bye.
     *
     * @param args Command-line arguments, which are not used.
     */

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        TaskList tasks = new TaskList(loadTasks(ui));

        // Keeps reading commands until the user ends the conversation or input is exhausted.
        while (ui.hasNextCommand()) {
            String userInput = ui.readCommand();
            String trimmedInput = userInput.trim();

            CommandType commandType = Parser.parseCommandType(userInput);

            // Prints the ending message and stops when the user enters the exit command.
            if (commandType == CommandType.BYE && trimmedInput.equals("bye")) {
                ui.showGoodbye();
                break;
            }

            if (commandType == CommandType.MARK || commandType == CommandType.UNMARK) {
                try {
                    int taskIndex = Parser.parseTaskNumber(userInput, tasks.size());
                    Task selectedTask = tasks.get(taskIndex);

                    // Marks or unmarks the selected task based on the command.
                    boolean shouldMark = commandType == CommandType.MARK;
                    if (shouldMark) {
                        selectedTask.markDone();
                    } else {
                        selectedTask.markUndone();
                    }
                    saveTasks(tasks, ui);
                    ui.showTaskStatusChanged(selectedTask, shouldMark);
                } catch (SlotBotException e) {
                    ui.showError(e.getMessage());
                }
                continue;
            }

            // Removes the selected task from the list.
            if (commandType == CommandType.DELETE) {
                try {
                    int taskIndex = Parser.parseTaskNumber(userInput, tasks.size());
                    Task removedTask = tasks.delete(taskIndex);
                    saveTasks(tasks, ui);
                    ui.showDeletedTask(removedTask, tasks.size());
                } catch (SlotBotException e) {
                    ui.showError(e.getMessage());
                }
                continue;
            }

            // Displays all stored tasks when the list command is entered.
            if (commandType == CommandType.LIST && trimmedInput.equals("list")) {
                ui.showTaskList(tasks);
                continue;
            }

            // Stores valid task commands and catches parsing errors.
            try {
                Task newTask = Parser.parseTask(userInput, commandType);
                tasks.add(newTask);
                saveTasks(tasks, ui);
                ui.showAddedTask(newTask, tasks.size());
            } catch (SlotBotException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Saves all tasks to the configured save file.
     *
     * @param tasks Tasks to save.
     * @param ui User interface used to report a save error.
     */
    private static void saveTasks(TaskList tasks, Ui ui) {
        try {
            Files.createDirectories(SAVE_FILE_PATH.getParent());

            List<String> taskLines = new ArrayList<>();
            for (Task task : tasks.getTasks()) {
                taskLines.add(formatTaskForSaving(task));
            }
            Files.write(SAVE_FILE_PATH, taskLines);
        } catch (IOException e) {
            ui.showSaveError();
        }
    }

    /**
     * Loads all previously saved tasks, or returns an empty list for a first launch.
     *
     * @param ui User interface used to report loading problems.
     * @return Tasks loaded from the save file.
     */
    private static List<Task> loadTasks(Ui ui) {
        if (!Files.exists(SAVE_FILE_PATH)) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        List<String> taskLines;
        try {
            taskLines = Files.readAllLines(SAVE_FILE_PATH);
        } catch (IOException e) {
            ui.showLoadError();
            return tasks;
        }

        for (int i = 0; i < taskLines.size(); i++) {
            try {
                tasks.add(parseSavedTask(taskLines.get(i)));
            } catch (RuntimeException e) {
                ui.showInvalidTaskDataWarning(i + 1);
            }
        }
        return tasks;
    }

    /**
     * Converts one saved line into a task.
     *
     * @param taskLine Save-file representation of a task.
     * @return Task represented by the saved line.
     */
    private static Task parseSavedTask(String taskLine) {
        String[] fields = taskLine.split("\\s*\\|\\s*", -1);
        if (fields.length < 2 || (!fields[1].equals("0") && !fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid task status.");
        }

        boolean isDone = fields[1].equals("1");
        Task task = switch (fields[0]) {
        case "T" -> {
            validateTaskFields(fields, 3);
            yield new Todo(fields[2]);
        }
        case "D" -> {
            validateTaskFields(fields, 4);
            yield new Deadline(fields[2], LocalDate.parse(fields[3]));
        }
        case "E" -> {
            validateTaskFields(fields, 5);
            yield new Event(
                    fields[2],
                    LocalDateTime.parse(fields[3], EVENT_DATE_TIME_FORMATTER),
                    LocalDateTime.parse(fields[4], EVENT_DATE_TIME_FORMATTER));
        }
        default -> throw new IllegalArgumentException("Unknown task type.");
        };

        if (isDone) {
            task.markDone();
        }
        return task;
    }

    /**
     * Checks that a saved task has all required non-empty fields.
     *
     * @param fields Fields from one saved task line.
     * @param expectedFieldCount Expected number of fields for the task type.
     */
    private static void validateTaskFields(String[] fields, int expectedFieldCount) {
        if (fields.length != expectedFieldCount) {
            throw new IllegalArgumentException("Incorrect number of task fields.");
        }

        for (int i = 2; i < fields.length; i++) {
            if (fields[i].isBlank()) {
                throw new IllegalArgumentException("Empty task field.");
            }
        }
    }

    /**
     * Formats one task as a line in the save file.
     *
     * @param task Task to format.
     * @return Save-file representation of the task.
     */
    private static String formatTaskForSaving(Task task) {
        String completionStatus = task.getIsDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T | %s | %s".formatted(completionStatus, task.getDescription());
        }
        if (task instanceof Deadline deadline) {
            return "D | %s | %s | %s".formatted(
                    completionStatus, deadline.getDescription(), deadline.getDate());
        }
        if (task instanceof Event event) {
            return "E | %s | %s | %s | %s".formatted(
                    completionStatus,
                    event.getDescription(),
                    event.getFrom().format(EVENT_DATE_TIME_FORMATTER),
                    event.getTo().format(EVENT_DATE_TIME_FORMATTER));
        }

        return "T | %s | %s".formatted(completionStatus, task.getDescription());
    }

}
