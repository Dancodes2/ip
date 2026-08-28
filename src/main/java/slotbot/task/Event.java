package slotbot.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that starts and ends at specified times.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm", Locale.ENGLISH);
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event.
     *
     * @param description Event description.
     * @param from Event start time.
     * @param to Event end time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event's start time.
     *
     * @return Event start time.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns this event's end time.
     *
     * @return Event end time.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns the event's display text.
     *
     * @return Event type, status, description, and times.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + String.format(" (from: %s to: %s)",
                        from.format(DISPLAY_FORMATTER), to.format(DISPLAY_FORMATTER));
    }
}
