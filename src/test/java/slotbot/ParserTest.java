package slotbot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    @Test
    public void parseTaskNumber_validFirstTask_returnsZero() throws SlotBotException {
        assertEquals(0, Parser.parseTaskNumber("mark 1", 3));
    }

    @Test
    public void parseTaskNumber_validLastTask_returnsLastIndex() throws SlotBotException {
        assertEquals(2, Parser.parseTaskNumber("mark 3", 3));
    }

    @Test
    public void parseTaskNumber_zeroTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseTaskNumber("mark 0", 3));
    }
}