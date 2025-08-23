package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Queue;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 * This class focuses on improving the understandability of a specific test case.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that makeColumnQueue does not wrap a string that is shorter than the
     * maximum line width.
     */
    @Test
    public void makeColumnQueue_shouldNotWrapText_whenTextIsShorterThanMaxWidth() {
        // Arrange
        // The systemOut() factory method creates an instance with default settings.
        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        TextStyle defaultStyle = TextStyle.DEFAULT;
        String shortText = "org.apache.commons.cli.help.TextStyle$1";

        // Act
        Queue<String> resultQueue = helpAppendable.makeColumnQueue(shortText, defaultStyle);

        // Assert
        // 1. Verify the primary behavior: the queue contains the unwrapped string.
        assertNotNull("The returned queue should not be null.", resultQueue);
        assertEquals("Queue should contain exactly one line for a short string.", 1, resultQueue.size());
        assertEquals("The single line in the queue should be the original string.", shortText, resultQueue.peek());

        // 2. Verify the state of the TextHelpAppendable instance.
        // This confirms the test was executed against an instance with the expected default values.
        assertEquals("Default indent should be used.", TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
        assertEquals("Default max width should be used.", TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
    }
}