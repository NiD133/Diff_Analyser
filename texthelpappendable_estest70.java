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
     * Verifies that appendHeader throws a BufferOverflowException when the underlying
     * Appendable has insufficient capacity to hold the formatted header text.
     * The formatting added by appendHeader (like indentation or newlines) requires
     * more space than the buffer provides.
     */
    @Test(expected = BufferOverflowException.class)
    public void appendHeaderShouldThrowBufferOverflowExceptionWhenAppendableIsFull() throws IOException {
        // Arrange: Create a TextHelpAppendable with a small, fixed-size buffer
        // that is too small to accommodate the header text plus any formatting.
        final int bufferCapacity = 6;
        CharBuffer limitedCapacityBuffer = CharBuffer.allocate(bufferCapacity);
        TextHelpAppendable textHelpAppendable = new TextHelpAppendable(limitedCapacityBuffer);

        // The text to append is exactly the size of the buffer.
        final String headerText = "Header";
        assertEquals("Precondition: Header text length should match buffer capacity.",
                bufferCapacity, headerText.length());

        // Act & Assert: Attempting to append the header will cause an overflow
        // because the method adds formatting characters (e.g., newlines, spaces)
        // that exceed the buffer's remaining capacity. The expected exception
        // is declared in the @Test annotation.
        textHelpAppendable.appendHeader(1, headerText);
    }
}