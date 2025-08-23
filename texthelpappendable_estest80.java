package org.apache.commons.cli.help;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that an instance created with the systemOut() factory method
     * is initialized with the correct default property values.
     */
    @Test
    public void systemOutInstanceShouldHaveDefaultSettings() {
        // Arrange
        TextHelpAppendable helpFormatter = TextHelpAppendable.systemOut();

        // Act
        // No action is performed, as we are testing the initial state after creation.

        // Assert
        // Check that the formatter is configured with the expected default constants.
        assertEquals("Default max width should be set", TextHelpAppendable.DEFAULT_WIDTH, helpFormatter.getMaxWidth());
        assertEquals("Default indent should be set", TextHelpAppendable.DEFAULT_INDENT, helpFormatter.getIndent());
        assertEquals("Default left pad should be set", TextHelpAppendable.DEFAULT_LEFT_PAD, helpFormatter.getLeftPad());
    }
}