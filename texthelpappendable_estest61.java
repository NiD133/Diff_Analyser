package org.apache.commons.cli.help;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that attempting to write to a TextHelpAppendable initialized with a
     * null Appendable results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void appendParagraphShouldThrowNullPointerExceptionWhenConstructedWithNullAppendable() throws IOException {
        // Arrange: Create a TextHelpAppendable with a null underlying Appendable.
        // This is the condition that should cause the failure.
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(null);
        final String paragraph = "This is a test paragraph.";

        // Act: Attempt to append a paragraph. This should trigger the exception
        // because the internal writer is null.
        helpAppendable.appendParagraph(paragraph);

        // Assert: The @Test(expected) annotation handles the assertion that a
        // NullPointerException was thrown.
    }
}