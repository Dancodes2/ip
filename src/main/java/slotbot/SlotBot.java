package slotbot;

import java.nio.file.Path;

import slotbot.task.Task;
import slotbot.task.TaskList;

/**
 * The main entry point for SlotBot.
 */
public class SlotBot {
    private static final Path SAVE_FILE_PATH = Path.of("data", "slotbot.txt");

    /**
     * Starts SlotBot and processes user commands until the user enters bye.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(SAVE_FILE_PATH, ui);
        ui.showWelcome();

        TaskList tasks = new TaskList(storage.loadTasks());

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
                    storage.saveTasks(tasks);
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
                    storage.saveTasks(tasks);
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

            // Displays tasks whose descriptions contain the requested keyword.
            if (commandType == CommandType.FIND) {
                try {
                    String keyword = Parser.parseFindKeyword(userInput);
                    ui.showMatchingTasks(tasks.findMatchingTasks(keyword));
                } catch (SlotBotException e) {
                    ui.showError(e.getMessage());
                }
                continue;
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
    }

}
