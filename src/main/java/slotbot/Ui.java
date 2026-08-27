package slotbot;

import java.util.Scanner;

import slotbot.task.Task;
import slotbot.task.TaskList;

/**
 * Handles console interactions with the user.
 */
public class Ui {
    private static final String SEPARATOR =
            "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available to read.
     *
     * @return True if another command is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next command entered by the user.
     *
     * @return Next user command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the welcome message. */
    public void showWelcome() {
        System.out.print("""
                Hello! I'm SlotBot.
                Let's keep your time and tasks in order.
                ____________________________________________________________
                """);
    }

    /** Displays the goodbye message. */
    public void showGoodbye() {
        System.out.print("""
                %s
                All done. See you next time!
                %s
                """.formatted(SEPARATOR, SEPARATOR));
    }

    /**
     * Displays a task whose completion status changed.
     *
     * @param task Task whose status changed.
     * @param isMarked True if the task was marked, or false if it was unmarked.
     */
    public void showTaskStatusChanged(Task task, boolean isMarked) {
        String message = isMarked
                ? "Nice! We got one."
                : "OK, I've marked this task as not done yet:";
        System.out.print("""
                %s
                %s
                  %s
                %s

                """.formatted(SEPARATOR, message, task, SEPARATOR));
    }

    /**
     * Displays a deleted task and the new task count.
     *
     * @param task Deleted task.
     * @param taskCount Number of tasks remaining.
     */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.print("""
                %s
                Noted. I've removed this task:
                  %s
                Now you have %d tasks in the list.
                %s

                """.formatted(SEPARATOR, task, taskCount, SEPARATOR));
    }

    /**
     * Displays all tasks in their current order.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.print("""
                %s
                Here are the tasks in your list:
                """.formatted(SEPARATOR));
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.print("""
                %s

                """.formatted(SEPARATOR));
    }

    /**
     * Displays an added task and the new task count.
     *
     * @param task Added task.
     * @param taskCount Number of tasks after adding the task.
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.print("""
                %s
                Got it. I've added this task:
                  %s
                Now you have %d tasks in the list.
                %s

                """.formatted(SEPARATOR, task, taskCount, SEPARATOR));
    }

    /**
     * Displays a user-facing command error.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.print("""
                %s
                %s
                %s

                """.formatted(SEPARATOR, message, SEPARATOR));
    }

    /** Displays a warning that tasks could not be saved. */
    public void showSaveError() {
        System.out.println("Warning: Unable to save tasks to disk.");
    }

    /** Displays warnings that saved tasks could not be loaded. */
    public void showLoadError() {
        System.out.println("Warning: Unable to load saved tasks.");
        System.out.println("Starting with an empty list.");
    }

    /**
     * Displays a warning about an invalid line in the task data file.
     *
     * @param lineNumber One-based line number containing invalid data.
     */
    public void showInvalidTaskDataWarning(int lineNumber) {
        System.out.println("Warning: Ignoring invalid task data on line " + lineNumber + ".");
    }
}
