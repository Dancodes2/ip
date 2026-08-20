/**
 * Represents a task that can be marked as done or not done.
 */
public class Task {
    private final String description;
    private boolean isDone;

    // Constructor.
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    // Marks task as done.
    public void markDone() {
        isDone = true;
    }

    // Marks task as not done.
    public void markUndone() {
        isDone = false;
    }

    // Returns this task's completion status.
    public boolean getIsDone() {
        return isDone;
    }

    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        return status + " " + description;
    }
}
