// Task that must be completed before a specified time.
public class Deadline extends Task {
    private final String date;

    // Constructor.
    public Deadline(String description, String date) {
        super(description);
        this.date = date;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + date + ")";
    }
}
