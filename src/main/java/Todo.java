/** Represents a task without an attached date or time. */
public class Todo extends Task {

    // Constructor.
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
