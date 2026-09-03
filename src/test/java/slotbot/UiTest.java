package slotbot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UiTest {
    @Test
    public void showLines_noArguments_producesNoOutput() {
        StringBuilder output = new StringBuilder();
        new Ui(output::append).showLines();

        assertEquals("", output.toString());
    }

    @Test
    public void showLines_oneLine_appendsLineSeparator() {
        StringBuilder output = new StringBuilder();
        new Ui(output::append).showLines("100% complete");

        assertEquals("100% complete" + System.lineSeparator(), output.toString());
    }

    @Test
    public void showLines_multipleLines_preservesOrderAndBlankLine() {
        StringBuilder output = new StringBuilder();
        new Ui(output::append).showLines("first", "", "last");

        String newline = System.lineSeparator();
        assertEquals("first" + newline + newline + "last" + newline, output.toString());
    }

    @Test
    public void showLoadError_twoLines_preservesWarningText() {
        StringBuilder output = new StringBuilder();
        new Ui(output::append).showLoadError();

        String newline = System.lineSeparator();
        assertEquals("Warning: Unable to load saved tasks." + newline
                + "Starting with an empty list." + newline, output.toString());
    }
}
