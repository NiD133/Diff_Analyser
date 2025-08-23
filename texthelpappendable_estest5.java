package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 * This class focuses on the writeColumnQueues method.
 */
// The original test class name "TextHelpAppendable_ESTestTest5" was likely auto-generated.
// A more descriptive name like "TextHelpAppendableTest" would be better.
public class TextHelpAppendableTest {

    /**
     * Verifies that calling writeColumnQueues with empty lists does not cause errors
     * and does not alter the configuration of the TextHelpAppendable instance.
     */
    @Test
    public void writeColumnQueuesWithEmptyInputsShouldBeANoOp() throws IOException {
        // Arrange: Create a TextHelpAppendable instance with default settings.
        // The method under test should not produce any output with empty inputs,
        // so using systemOut() is safe and simple.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        final List<Queue<String>> emptyColumnQueues = Collections.emptyList();
        final List<TextStyle> emptyStyles = Collections.emptyList();

        // Act: Call the method with empty lists for columns and styles.
        helpAppendable.writeColumnQueues(emptyColumnQueues, emptyStyles);

        // Assert: Verify that the properties of the helpAppendable instance remain
        // unchanged and equal to their default values.
        assertEquals("Left pad should remain at default", TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
        assertEquals("Indent should remain at default", TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
        assertEquals("Max width should remain at default", TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
    }
}