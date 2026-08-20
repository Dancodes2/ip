/** Represents a command recognized by SlotBot. */
public enum CommandType {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    UNKNOWN;

    /**
     * Returns the command type for the given command text.
     *
     * @param commandText Command text entered by the user.
     * @return Matching command type, or UNKNOWN if there is no match.
     */
    public static CommandType fromText(String commandText) {
        return switch (commandText) {
            case "bye" -> BYE;
            case "list" -> LIST;
            case "mark" -> MARK;
            case "unmark" -> UNMARK;
            case "delete" -> DELETE;
            case "todo" -> TODO;
            case "deadline" -> DEADLINE;
            case "event" -> EVENT;
            default -> UNKNOWN;
        };
    }
}
