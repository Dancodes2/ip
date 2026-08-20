/** Represents a task that must be completed before a specified time. */
public class Deadline extends Task {
    private final String date;

    /**
     * Creates a deadline.
     *
     * @param description Deadline description.
     * @param date Deadline date.
     */
    public Deadline(String description, String date) {
        super(description);
        this.date = date;
    }

    /**
     * Returns the deadline's display text.
     *
     * @return Deadline type, status, description, and date.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + date + ")";
    }
}
