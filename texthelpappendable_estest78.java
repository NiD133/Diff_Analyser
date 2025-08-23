package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the static utility methods in {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that indexOfWrap() throws an IllegalArgumentException when the provided
     * width is negative, as the width must be a positive value.
     */
    @Test
    public void indexOfWrapShouldThrowExceptionForNegativeWidth() {
        // Arrange: Define the invalid width and valid placeholder values for other arguments.
        final int negativeWidth = -1;
        final CharSequence text = "some text";
        final int startPos = 0;

        // Act & Assert
        try {
            TextHelpAppendable.indexOfWrap(text, negativeWidth, startPos);
            fail("Expected an IllegalArgumentException to be thrown due to negative width.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals("Width must be greater than 0", e.getMessage());
        }
    }
}