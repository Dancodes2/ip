package slotbot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void constructor_nullTaskList_assertionError() {
        assertThrows(AssertionError.class, () -> new TaskList(null));
    }

    @Test
    public void constructor_taskListContainingNull_assertionError() {
        assertThrows(AssertionError.class, () -> new TaskList(Arrays.asList((Task) null)));
    }

    @Test
    public void add_nullTask_assertionError() {
        TaskList tasks = new TaskList(List.of());

        assertThrows(AssertionError.class, () -> tasks.add(null));
    }

    @Test
    public void get_indexEqualToSize_assertionError() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertThrows(AssertionError.class, () -> tasks.get(1));
    }

    @Test
    public void delete_negativeIndex_assertionError() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertThrows(AssertionError.class, () -> tasks.delete(-1));
    }

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

        assertThrows(UnsupportedOperationException.class, () -> matches.add(new Todo("new task")));
    }

    @Test
    public void findUpcomingDeadlines_withinWindow_returnsSortedUnfinishedDeadlines() {
        Deadline laterDeadline = new Deadline("later", LocalDate.of(2026, 9, 15));
        Deadline earlierDeadline = new Deadline("earlier", LocalDate.of(2026, 9, 12));
        Deadline completedDeadline = new Deadline("completed", LocalDate.of(2026, 9, 13));
        completedDeadline.markDone();
        TaskList tasks = new TaskList(List.of(
                laterDeadline,
                new Todo("not a deadline"),
                earlierDeadline,
                completedDeadline));

        List<Deadline> reminders = tasks.findUpcomingDeadlines(LocalDate.of(2026, 9, 11), 4);

        assertEquals(List.of(earlierDeadline, laterDeadline), reminders);
    }

    @Test
    public void findUpcomingDeadlines_excludesOutsideWindow() {
        Deadline overdue = new Deadline("overdue", LocalDate.of(2026, 9, 10));
        Deadline atBoundary = new Deadline("boundary", LocalDate.of(2026, 9, 18));
        Deadline beyondBoundary = new Deadline("later", LocalDate.of(2026, 9, 19));
        TaskList tasks = new TaskList(List.of(overdue, atBoundary, beyondBoundary));

        List<Deadline> reminders = tasks.findUpcomingDeadlines(LocalDate.of(2026, 9, 11), 7);

        assertEquals(List.of(atBoundary), reminders);
    }

    @Test
    public void findUpcomingDeadlines_nullStartDate_assertionError() {
        TaskList tasks = new TaskList(List.of());

        assertThrows(AssertionError.class, () -> tasks.findUpcomingDeadlines(null, 7));
    }
}
