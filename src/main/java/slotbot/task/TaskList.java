package slotbot.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages SlotBot's collection of tasks.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates a task list containing the given tasks.
     *
     * @param tasks Initial tasks in the list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the given index.
     *
     * @param index Zero-based index of the task to delete.
     * @return Deleted task.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index Zero-based index of the task to return.
     * @return Task at the given index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable snapshot of the tasks.
     *
     * @return Tasks in their current order.
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Returns tasks whose descriptions contain the given keyword.
     *
     * @param keyword Case-sensitive text to search for.
     * @return Matching tasks in their original order as an unmodifiable list.
     */
    public List<Task> findMatchingTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return List.copyOf(matchingTasks);
    }
}
