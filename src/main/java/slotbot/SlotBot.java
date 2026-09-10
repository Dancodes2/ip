package slotbot;

import java.nio.file.Path;

import slotbot.task.Task;
import slotbot.task.TaskList;

/**
 * Processes commands for the console and graphical interfaces.
 */
public class SlotBot {
    private static final Path SAVE_FILE_PATH = Path.of("data", "slotbot.txt");

    private final StringBuilder response = new StringBuilder();
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;
    private final String welcome;
    private boolean isExiting;

    /**
     * Creates a chatbot using the standard relative save path.
     */
    public SlotBot() {
        this(SAVE_FILE_PATH);
    }

    /**
     * Creates a chatbot and loads its saved tasks.
     *
     * @param saveFilePath Path of the task data file.
     */
    public SlotBot(Path saveFilePath) {
        ui = new Ui(response::append);
        storage = new Storage(saveFilePath, ui);

        ui.showWelcome();
        tasks = new TaskList(storage.loadTasks());

        welcome = response.toString();
        response.setLength(0);
    }

    public String getWelcome() {
        return welcome;
    }

    /**
     * Returns whether a valid exit command has been received.
     *
     * @return True after the user requests exit.
     */
    public boolean isExiting() {
        return isExiting;
    }

    /**
     * Processes one command and returns its response, including storage warnings.
     * Commands received after exit do not change tasks.
     *
     * @param userInput Command entered by the user.
     * @return Formatted response for this command only.
     */
    public String getResponse(String userInput) {
        response.setLength(0);
        if (isExiting) {
            ui.showGoodbye();
        } else {
            processCommand(userInput);
        }

        return response.toString();
    }

    /**
     * Starts the retained console interface.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Ui console = new Ui();
        SlotBot bot = new SlotBot();
        System.out.print(bot.getWelcome());

        while (!bot.isExiting() && console.hasNextCommand()) {
            System.out.print(bot.getResponse(console.readCommand()));
        }
    }

    /**
     * Executes a command against the current task list.
     *
     * @param userInput Command entered by the user.
     */
    private void processCommand(String userInput) {
        String trimmedInput = userInput.trim();

        CommandType commandType = Parser.parseCommandType(userInput);

        // Prints the ending message and stops when the user enters the exit command.
        if (commandType == CommandType.BYE && trimmedInput.equals("bye")) {
            ui.showGoodbye();
            isExiting = true;
            return;
        }

        if (commandType == CommandType.MARK || commandType == CommandType.UNMARK) {
            handleTaskStatusChange(userInput, commandType);
            return;
        }

        if (commandType == CommandType.DELETE) {
            handleDelete(userInput);
            return;
        }

        // Displays all stored tasks when the list command is entered.
        if (commandType == CommandType.LIST && trimmedInput.equals("list")) {
            ui.showTaskList(tasks);
            return;
        }

        if (commandType == CommandType.FIND) {
            handleFind(userInput);
            return;
        }

        // Stores valid task commands and catches parsing errors.
        try {
            Task newTask = Parser.parseTask(userInput, commandType);
            tasks.add(newTask);
            storage.saveTasks(tasks);
            ui.showAddedTask(newTask, tasks.size());
        } catch (SlotBotException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Marks or unmarks the task selected by the command.
     *
     * @param userInput Command containing the task number.
     * @param commandType MARK or UNMARK command type.
     */
    private void handleTaskStatusChange(String userInput, CommandType commandType) {
        try {
            int taskIndex = Parser.parseTaskNumber(userInput, tasks.size());
            Task selectedTask = tasks.get(taskIndex);
            boolean shouldMark = commandType == CommandType.MARK;

            if (shouldMark) {
                selectedTask.markDone();
            } else {
                selectedTask.markUndone();
            }

            storage.saveTasks(tasks);
            ui.showTaskStatusChanged(selectedTask, shouldMark);
        } catch (SlotBotException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Deletes the task selected by the command.
     *
     * @param userInput Command containing the task number.
     */
    private void handleDelete(String userInput) {
        try {
            int taskIndex = Parser.parseTaskNumber(userInput, tasks.size());
            Task removedTask = tasks.delete(taskIndex);

            storage.saveTasks(tasks);
            ui.showDeletedTask(removedTask, tasks.size());
        } catch (SlotBotException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Displays tasks matching the keyword in the command.
     *
     * @param userInput Command containing the search keyword.
     */
    private void handleFind(String userInput) {
        try {
            String keyword = Parser.parseFindKeyword(userInput);
            ui.showMatchingTasks(tasks.findMatchingTasks(keyword));
        } catch (SlotBotException e) {
            ui.showError(e.getMessage());
        }
    }
}
