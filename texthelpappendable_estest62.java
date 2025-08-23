package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that calling appendParagraph throws a NegativeArraySizeException
     * if the left padding has been set to a negative value. This occurs because
     * an internal utility attempts to create a character array with a negative size
     * for the padding.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void appendParagraphShouldThrowNegativeArraySizeExceptionForNegativeLeftPad() throws IOException {
        // Arrange: Create a TextHelpAppendable and set a negative left padding.
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringWriter());
        helpAppendable.setLeftPad(-1);
        final String paragraphText = "This is a test paragraph.";

        // Act: Attempt to append a paragraph. This action is expected to throw the exception.
        helpAppendable.appendParagraph(paragraphText);

        // Assert: The @Test(expected=...) annotation handles the exception assertion.
    }
}