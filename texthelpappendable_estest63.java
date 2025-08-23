package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

/**
 * Tests for {@link TextHelpAppendable} focusing on buffer handling.
 */
public class TextHelpAppendable_ESTestTest63 extends TextHelpAppendable_ESTest_scaffolding {

    /**
     * Verifies that appendParagraph throws a BufferOverflowException when the underlying
     * Appendable has insufficient space to write the entire paragraph.
     */
    @Test(timeout = 4000, expected = BufferOverflowException.class)
    public void appendParagraphShouldThrowExceptionWhenAppendableHasInsufficientSpace() throws IOException {
        // Arrange: Create a small, fixed-size buffer as the destination for the help text.
        final CharBuffer destinationBuffer = CharBuffer.allocate(5);
        final TextHelpAppendable textHelpAppendable = new TextHelpAppendable(destinationBuffer);

        // Act & Assert
        // Append one character, leaving only 4 characters of space in the buffer.
        textHelpAppendable.append('#');

        // Define a paragraph that is too long (5 chars) to fit in the remaining space.
        final String oversizedParagraph = "12345";

        // Attempting to append this paragraph should cause a BufferOverflowException.
        // The @Test(expected=...) annotation handles the assertion.
        textHelpAppendable.appendParagraph(oversizedParagraph);
    }
}