package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.cli.help.TextStyle.Builder;
import org.junit.Test;

/**
 * Test suite for {@link TextHelpAppendable}.
 * The original test class name 'TextHelpAppendable_ESTestTest52' is kept for context,
 * but a more conventional name would be 'TextHelpAppendableTest'.
 */
public class TextHelpAppendable_ESTestTest52 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Tests that methods relying on the configured text width, such as appendTitle,
     * throw an IllegalArgumentException if the width has been set to an invalid negative value.
     */
    @Test
    public void appendTitleShouldThrowExceptionWhenWidthIsNegative() throws Exception {
        // Arrange: Create a TextHelpAppendable and set its width to a negative value.
        // The resize() method modifies the width of the provided style builder.
        // Using a negative fraction results in a negative width.
        final TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        final Builder styleBuilder = helpAppendable.getTextStyleBuilder();
        helpAppendable.resize(styleBuilder, -1.0);

        // Act & Assert: Verify that attempting to append a title throws an exception
        // with the expected message.
        try {
            helpAppendable.appendTitle("Example Title");
            fail("Expected an IllegalArgumentException because the width is negative.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Width must be greater than 0", e.getMessage());
        }
    }
}