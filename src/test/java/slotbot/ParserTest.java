package slotbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import slotbot.task.Deadline;

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
        assertThrows(SlotBotException.class, () -> Parser.parseTaskNumber("mark", 3));
    }

    @Test
    public void parseTaskNumber_nonNumericTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTaskNumber("mark two", 3));
    }

    @Test
    public void parseTaskNumber_decimalTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTaskNumber("mark 1.5", 3));
    }

    @Test
    public void parseTaskNumber_zeroTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTaskNumber("mark 0", 3));
    }

    @Test
    public void parseTaskNumber_negativeTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTaskNumber("mark -1", 3));
    }

    @Test
    public void parseTaskNumber_tooLargeTaskNumber_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTaskNumber("mark 4", 3));
    }

    @Test
    public void parseTaskNumber_emptyTaskList_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTaskNumber("mark 1", 0));
    }

    @Test
    public void parseFindKeyword_validKeyword_returnsKeyword() throws SlotBotException {
        assertEquals("book", Parser.parseFindKeyword("find book"));
    }

    @Test
    public void parseFindKeyword_missingKeyword_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseFindKeyword("find"));
    }

    @Test
    public void parseFindKeyword_blankKeyword_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseFindKeyword("find   "));
    }

    @Test
    public void parseCommandType_reminders_returnsReminders() {
        assertEquals(CommandType.REMINDERS, Parser.parseCommandType("reminders"));
    }

    @Test
    public void parseTask_deadlineWithExtraSeparator_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTask(
                "deadline submit /by 2026-09-18 /by 2026-09-19", CommandType.DEADLINE));
    }

    @Test
    public void parseTask_deadlineWithExtraSpacing_returnsDeadline() throws SlotBotException {
        Deadline deadline = (Deadline) Parser.parseTask(
                "deadline submit   /by   2026-09-18", CommandType.DEADLINE);

        assertEquals("submit", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 18), deadline.getDate());
    }

    @Test
    public void parseTask_descriptionWithStorageSeparator_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTask(
                "todo unsafe | description", CommandType.TODO));
    }

    @Test
    public void parseTask_eventWithExtraSeparator_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTask(
                "event meeting /from 2026-09-18 10:00 /from 2026-09-18 11:00 "
                        + "/to 2026-09-18 12:00",
                CommandType.EVENT));
    }

    @Test
    public void parseTask_eventWithEqualTimes_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTask(
                "event meeting /from 2026-09-18 10:00 /to 2026-09-18 10:00",
                CommandType.EVENT));
    }

    @Test
    public void parseTask_eventWithReversedTimes_exceptionThrown() {
        assertThrows(SlotBotException.class, () -> Parser.parseTask(
                "event meeting /from 2026-09-18 11:00 /to 2026-09-18 10:00",
                CommandType.EVENT));
    }
}
