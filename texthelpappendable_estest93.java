package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on instance creation and default values.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that the TextHelpAppendable instance created via the static
     * systemOut() factory method is initialized with the correct default
     * formatting values.
     */
    @Test
    public void systemOutShouldCreateInstanceWithDefaultFormattingValues() {
        // Arrange: No setup needed, the test uses a static factory method.

        // Act: Create an instance using the systemOut() factory.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();

        // Assert: Check that the instance has the expected default properties.
        // Using the public constants from the class under test makes the assertions
        // more maintainable and clearly expresses the intent.
        assertEquals("Default max width should match the constant.",
                TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());

        assertEquals("Default indent should match the constant.",
                TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());

        assertEquals("Default left pad should match the constant.",
                TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());
    }
}