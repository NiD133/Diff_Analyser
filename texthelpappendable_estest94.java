package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that the TextHelpAppendable instance created via the systemOut()
     * factory method is initialized with the correct default values for padding,
     * width, and indentation.
     */
    @Test
    public void systemOutShouldCreateInstanceWithDefaultValues() {
        // Arrange & Act: Create a TextHelpAppendable instance using the factory method.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();

        // Assert: Check that the instance has the expected default properties.
        // We use the public constants from the class under test to avoid magic numbers
        // and to ensure the test is consistent with the class's public contract.
        assertEquals("Default left pad should match the constant",
                     TextHelpAppendable.DEFAULT_LEFT_PAD, helpAppendable.getLeftPad());

        assertEquals("Default max width should match the constant",
                     TextHelpAppendable.DEFAULT_WIDTH, helpAppendable.getMaxWidth());

        assertEquals("Default indent should match the constant",
                     TextHelpAppendable.DEFAULT_INDENT, helpAppendable.getIndent());
    }
}