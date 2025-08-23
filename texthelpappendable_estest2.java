package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    @Test
    public void systemOutShouldCreateInstanceWithDefaultFormattingValues() {
        // Arrange & Act: Create a TextHelpAppendable instance using the factory method.
        TextHelpAppendable helpFormatter = TextHelpAppendable.systemOut();

        // Assert: Verify that the instance is configured with the default formatting values.
        assertEquals("Default left pad should be set correctly",
                     TextHelpAppendable.DEFAULT_LEFT_PAD, helpFormatter.getLeftPad());
        assertEquals("Default indent should be set correctly",
                     TextHelpAppendable.DEFAULT_INDENT, helpFormatter.getIndent());
        assertEquals("Default max width should be set correctly",
                     TextHelpAppendable.DEFAULT_WIDTH, helpFormatter.getMaxWidth());
    }
}