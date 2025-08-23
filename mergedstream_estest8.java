package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Unit tests for the {@link MergedStream} class, focusing on the available() method.
 */
public class MergedStreamTest {

    /**
     * Tests that the available() method correctly reports the number of bytes
     * available from the prepended buffer when the underlying stream is empty.
     */
    @Test
    public void availableShouldReturnRemainingBytesFromBufferWhenStreamIsEmpty() throws IOException {
        // Arrange
        // The IOContext is not used by the available() method, so null is acceptable for this specific test.
        final IOContext ioContext = null;

        // Use an empty underlying stream to isolate the test to the prepended buffer's logic.
        final InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);
        assertEquals("Precondition: Underlying stream should have 0 bytes available.", 0, underlyingStream.available());

        // A buffer of bytes to be "pushed back" or prepended to the stream.
        final byte[] prependBuffer = new byte[] { 10, 20, 30, 40, 50 };
        
        // Define a slice of the buffer to be used, from index 1 (inclusive) to 4 (exclusive).
        // This corresponds to the bytes {20, 30, 40}.
        final int bufferStartOffset = 1;
        final int bufferEndOffset = 4;
        
        // The number of available bytes from the buffer should be the difference between end and start.
        final int expectedAvailableFromBuffer = bufferEndOffset - bufferStartOffset; // 4 - 1 = 3
        
        MergedStream mergedStream = new MergedStream(ioContext, underlyingStream, prependBuffer, bufferStartOffset, bufferEndOffset);

        // Act
        int actualAvailable = mergedStream.available();

        // Assert
        // The total available bytes should equal the bytes from the buffer slice,
        // as the underlying stream is empty.
        assertEquals("available() should report the number of bytes in the prepended buffer slice.",
                expectedAvailableFromBuffer, actualAvailable);
    }
}