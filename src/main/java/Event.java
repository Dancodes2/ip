/** Represents a task that starts and ends at specified times. */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event.
     *
     * @param description Event description.
     * @param from Event start time.
     * @param to Event end time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's display text.
     *
     * @return Event type, status, description, and times.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + String.format(" (from: %s to: %s)", from, to);
    }
}
