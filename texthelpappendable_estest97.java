package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that the TextHelpAppendable.systemOut() factory method creates an
     * instance with the correct default values for width, padding, and indentation.
     */
    @Test
    public void systemOutShouldCreateInstanceWithDefaultValues() {
        // Arrange
        // The static factory method systemOut() is the subject of this part of the test.
        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();

        // Act & Assert
        // Check that the newly created instance has been configured with the
        // default values defined as constants in the TextHelpAppendable class.
        assertEquals("Default max width should be set",
                     TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());

        assertEquals("Default left pad should be set",
                     TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());

        assertEquals("Default indent should be set",
                     TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
    }
}