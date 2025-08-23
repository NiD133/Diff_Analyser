package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    @Test
    public void copy_fromEmptyReaderToWriter_returnsZeroAndLeavesWriterUnchanged() throws IOException {
        // Arrange: Create an empty source reader and a destination writer backed by a CharBuffer.
        StringReader emptyReader = new StringReader("");
        CharBuffer destinationBuffer = CharStreams.createBuffer();
        Writer bufferWriter = CharStreams.asWriter(destinationBuffer);

        // Capture the buffer's state before the operation.
        int initialRemainingCapacity = destinationBuffer.remaining();

        // Act: Attempt to copy characters from the empty reader to the writer.
        long charsCopied = CharStreams.copy(emptyReader, bufferWriter);

        // Assert: Verify that no characters were copied and the buffer remains untouched.
        assertEquals("The number of characters copied should be 0 for an empty source.", 0L, charsCopied);
        assertEquals(
            "The destination buffer's capacity should be unchanged.",
            initialRemainingCapacity,
            destinationBuffer.remaining());
    }
}