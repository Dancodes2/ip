package slotbot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void constructor_nullDescription_assertionError() {
        assertThrows(AssertionError.class, () -> new Task(null));
    }

    @Test
    public void constructor_emptyDescription_assertionError() {
        assertThrows(AssertionError.class, () -> new Task(""));
    }

    @Test
    public void constructor_whitespaceDescription_assertionError() {
        assertThrows(AssertionError.class, () -> new Task("   "));
    }

    @Test
    public void constructor_validDescription_preservesDescription() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
    }
}
