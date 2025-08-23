package org.apache.commons.cli.help;

import org.junit.Test;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that appendParagraph() throws an IllegalArgumentException if the text width
     * has been resized to a negative value. The internal logic for wrapping text
     * requires a positive width.
     */
    @Test
    public void appendParagraphShouldThrowExceptionWhenWidthIsSetToNegative() {
        // Arrange: Create a TextHelpAppendable and get its style builder.
        // A StringWriter is used to prevent test output from printing to the console.
        TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringWriter());
        TextStyle.Builder styleBuilder = helpAppendable.getTextStyleBuilder();

        // Act: Resize the style builder with a negative fraction. This results in the
        // underlying max width becoming a negative number.
        helpAppendable.resize(styleBuilder, -1.0);

        // Assert: Verify that attempting to append a paragraph throws an exception
        // with the expected message.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            helpAppendable.appendParagraph("Attempting to append with invalid width.");
        });

        assertEquals("Width must be greater than 0", exception.getMessage());
    }
}