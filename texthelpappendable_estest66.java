package org.apache.commons.cli.help;

import org.junit.Test;

import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that appendList throws a BufferOverflowException when the underlying
     * Appendable has insufficient capacity to hold the formatted list output.
     */
    @Test
    public void appendListShouldThrowBufferOverflowWhenAppendableHasInsufficientCapacity() {
        // Arrange: Create an Appendable with a very small capacity (1 character).
        // The TextHelpAppendable will attempt to write to this buffer.
        CharBuffer limitedCapacityBuffer = CharBuffer.allocate(1);
        TextHelpAppendable textHelpAppendable = new TextHelpAppendable(limitedCapacityBuffer);

        // Arrange: Create a list with some content. The formatted output for this list,
        // which includes numbering (e.g., "1. ") and indentation, will be larger
        // than the buffer's 1-character capacity.
        List<CharSequence> listToAppend = Collections.singletonList("some text");

        // Act & Assert: Verify that calling appendList throws a BufferOverflowException
        // because the formatted list cannot fit into the underlying buffer.
        assertThrows(BufferOverflowException.class, () -> {
            textHelpAppendable.appendList(true, listToAppend);
        });
    }
}