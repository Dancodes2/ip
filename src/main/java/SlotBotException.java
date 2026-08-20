/** Represents an error caused by an invalid SlotBot command. */
public class SlotBotException extends Exception {
    /** Creates an exception with a message that can be shown to the user. */
    public SlotBotException(String message) {
        super(message);
    }
}
