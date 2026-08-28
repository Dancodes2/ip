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
    public void parseTaskNumber_surroundingWhitespace_returnsCorrectIndex() throws SlotBotException {
        assertEquals(1, Parser.parseTaskNumber("  mark   2  ", 3));
    }

    @Test
    public void parseTaskNumber_missingTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseTaskNumber("mark", 3));
    }

    @Test
    public void parseTaskNumber_nonNumericTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseTaskNumber("mark two", 3));
    }

    @Test
    public void parseTaskNumber_decimalTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseTaskNumber("mark 1.5", 3));
    }

    @Test
    public void parseTaskNumber_zeroTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseTaskNumber("mark 0", 3));
    }

    @Test
    public void parseTaskNumber_negativeTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseTaskNumber("mark -1", 3));
    }

    @Test
    public void parseTaskNumber_tooLargeTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseTaskNumber("mark 4", 3));
    }

    @Test
    public void parseTaskNumber_emptyTaskList_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseTaskNumber("mark 1", 0));
    }

    @Test
    public void parseFindKeyword_validKeyword_returnsKeyword() throws SlotBotException {
        assertEquals("book", Parser.parseFindKeyword("find book"));
    }

    @Test
    public void parseFindKeyword_missingKeyword_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseFindKeyword("find"));
    }

    @Test
    public void parseFindKeyword_blankKeyword_exceptionThrown() {
        assertThrows(SlotBotException.class,
                () -> Parser.parseFindKeyword("find   "));
    }
}
