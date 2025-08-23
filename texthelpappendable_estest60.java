package org.apache.commons.cli.help;

import org.junit.Test;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Verifies that attempting to append a paragraph to an appendable with
     * insufficient space throws a BufferOverflowException.
     */
    @Test(expected = BufferOverflowException.class)
    public void appendParagraphShouldThrowBufferOverflowWhenAppendableIsFull() throws IOException {
        // Arrange: Create an appendable with a capacity of only one character.
        CharBuffer limitedBuffer = CharBuffer.allocate(1);
        TextHelpAppendable helpAppendable = new TextHelpAppendable(limitedBuffer);

        // The appendParagraph method adds a newline after the text. Appending even a
        // single character will require more space than the buffer has available.
        String paragraph = "a";

        // Act: Attempt to append the paragraph, which should cause an overflow.
        helpAppendable.appendParagraph(paragraph);

        // Assert: The test passes if a BufferOverflowException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}