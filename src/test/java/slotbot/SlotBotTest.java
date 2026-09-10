package slotbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class SlotBotTest {
    @TempDir
    private Path directory;

    @Test
    public void getResponse_commandsAcrossRequests_preservesStateAndSaves() {
        Path save = directory.resolve("tasks.txt");
        SlotBot bot = new SlotBot(save);

        assertTrue(bot.getResponse("todo read book").contains("Now you have 1 tasks"));
        assertTrue(bot.getResponse("deadline return book /by 2026-09-10").contains("Sep 10 2026"));
        assertTrue(bot.getResponse("event study /from 2026-09-10 14:00 /to 2026-09-10 15:00")
                .contains("[E][ ] study"));

        assertTrue(bot.getResponse("mark 1").contains("[T][X] read book"));
        assertTrue(new SlotBot(save).getResponse("list").contains("[T][X] read book"));
        assertTrue(bot.getResponse("unmark 1").contains("[T][ ] read book"));

        assertFalse(bot.getResponse("find book").contains("[E]"));
        assertFalse(bot.getResponse("find missing").contains("[T]"));

        bot.getResponse("delete 3");
        assertFalse(new SlotBot(save).getResponse("list").contains("[E]"));
    }

    @Test
    public void getResponse_invalidCommands_doesNotChangeTasksOrRepeatReplies() {
        SlotBot bot = new SlotBot(directory.resolve("tasks.txt"));
        bot.getResponse("todo read book");
        String before = bot.getResponse("list");
        String[] commands = {"", "todo", "blah", "mark 0", "mark 2", "delete abc",
            "deadline bad /by 2026-02-30", "find", "bye extra"};

        for (String command : commands) {
            String reply = bot.getResponse(command);

            assertFalse(reply.isBlank());
            assertFalse(reply.contains("Got it. I've added"));
            assertEquals(before, bot.getResponse("list"));
            assertFalse(bot.isExiting());
        }
    }

    @Test
    public void getResponse_validBye_stopsFurtherChanges() {
        Path save = directory.resolve("tasks.txt");
        SlotBot bot = new SlotBot(save);

        assertTrue(bot.getResponse(" bye ").contains("See you next time"));
        assertTrue(bot.isExiting());

        bot.getResponse("todo ignored");
        assertFalse(Files.exists(save));
    }

    @Test
    public void getWelcome_corruptedRecord_includesWarningAndLoadsValidTask() throws IOException {
        Path save = directory.resolve("tasks.txt");
        Files.writeString(save, "broken\nT | 0 | kept\n");
        SlotBot bot = new SlotBot(save);

        assertTrue(bot.getWelcome().contains("Hello! I'm SlotBot."));
        assertTrue(bot.getWelcome().contains("Ignoring invalid task data on line 1"));
        assertTrue(bot.getResponse("list").contains("kept"));
        assertFalse(bot.getResponse("list").contains("Warning"));
    }

    @Test
    public void getResponse_unwritableSave_includesWarning() throws IOException {
        Path parentFile = directory.resolve("file");
        Files.writeString(parentFile, "not a directory");
        SlotBot bot = new SlotBot(parentFile.resolve("tasks.txt"));

        assertTrue(bot.getResponse("todo unsaved").contains("Warning: Unable to save tasks to disk."));
    }

    @Test
    public void getResponse_reminders_returnsUpcomingUnfinishedDeadlines() {
        Clock clock = Clock.fixed(Instant.parse("2026-09-11T00:00:00Z"), ZoneOffset.UTC);
        SlotBot bot = new SlotBot(directory.resolve("tasks.txt"), clock);

        bot.getResponse("deadline later /by 2026-09-18");
        bot.getResponse("deadline today /by 2026-09-11");
        bot.getResponse("deadline completed /by 2026-09-12");
        bot.getResponse("deadline outside /by 2026-09-19");
        bot.getResponse("mark 3");

        String response = bot.getResponse("reminders");

        assertTrue(response.contains("today"));
        assertTrue(response.contains("later"));
        assertFalse(response.contains("completed"));
        assertFalse(response.contains("outside"));
        assertTrue(response.indexOf("today") < response.indexOf("later"));
    }

    @Test
    public void getResponse_reminders_emptyMessage() {
        Clock clock = Clock.fixed(Instant.parse("2026-09-11T00:00:00Z"), ZoneOffset.UTC);
        SlotBot bot = new SlotBot(directory.resolve("tasks.txt"), clock);

        assertTrue(bot.getResponse("reminders").contains("No upcoming deadlines."));
    }
}
