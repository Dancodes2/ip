package slotbot.task;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {

    @Test
    public void findMatchingTasks_matchingDescriptions_preservesOrder() {
        Task firstMatch = new Todo("read book");
        Task nonMatch = new Todo("join club");
        Task secondMatch = new Deadline("return book", LocalDate.of(2026, 9, 1));
        TaskList tasks = new TaskList(List.of(firstMatch, nonMatch, secondMatch));

        List<Task> matches = tasks.findMatchingTasks("book");

        assertEquals(List.of(firstMatch, secondMatch), matches);
    }

    @Test
    public void findMatchingTasks_zeroMatches_returnsEmptyList() {
        TaskList tasks = new TaskList(List.of(new Todo("read notes")));

        assertEquals(List.of(), tasks.findMatchingTasks("book"));
    }

    @Test
    public void findMatchingTasks_caseMismatch_doesNotMatch() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertFalse(tasks.findMatchingTasks("Book").contains(tasks.get(0)));
    }

    @Test
    public void findMatchingTasks_keywordOnlyInRenderedTask_doesNotMatch() {
        TaskList tasks = new TaskList(List.of(
                new Deadline("submit report", LocalDate.of(2026, 9, 1))));

        assertEquals(List.of(), tasks.findMatchingTasks("2026"));
    }

    @Test
    public void findMatchingTasks_resultIsUnmodifiable() {
        List<Task> matches = new TaskList(List.of(new Todo("read book")))
                .findMatchingTasks("book");

        assertThrows(UnsupportedOperationException.class,
                () -> matches.add(new Todo("new task")));
    }
}
