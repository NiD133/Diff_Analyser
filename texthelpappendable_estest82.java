package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the TextHelpAppendable class.
 * This class contains the refactored test case.
 */
public class TextHelpAppendable_ESTestTest82 { // Retaining original class name for context

    /**
     * Verifies that calling appendList with an empty collection produces no output
     * and does not alter the default configuration of the TextHelpAppendable instance.
     */
    @Test
    public void appendListWithEmptyCollectionShouldNotChangeStateOrProduceOutput() throws IOException {
        // Arrange: Create a TextHelpAppendable with a StringWriter to capture output.
        StringWriter writer = new StringWriter();
        TextHelpAppendable helpAppendable = new TextHelpAppendable(writer);

        // Act: Call the method under test with an empty collection.
        helpAppendable.appendList(true, Collections.emptyList());

        // Assert: Verify that the behavior is correct.

        // 1. No output should have been written.
        assertEquals("Appending an empty list should not write any output.", "", writer.toString());

        // 2. The configuration properties should remain at their default values.
        assertEquals("Indent should remain at the default value.",
                TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
        assertEquals("Max width should remain at the default value.",
                TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());
        assertEquals("Left pad should remain at the default value.",
                TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
    }
}