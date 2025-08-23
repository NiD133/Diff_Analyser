package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Tests that the read() method correctly reads the first byte from the
     * prepended buffer and returns it as an unsigned integer value.
     * InputStream.read() is specified to return a value in the range 0-255.
     */
    @Test
    public void read_whenBufferHasData_returnsFirstByteAsUnsignedInt() throws IOException {
        // Arrange
        // The IOContext is not used by the read() method, so we can pass null.
        IOContext ioContext = null;

        // The underlying stream is not used in this test, as we only read from the buffer.
        // An empty stream is sufficient and simpler than a PipedInputStream.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);

        // The buffer to be "prepended" to the underlying stream.
        // We use a negative byte value to explicitly test the signed-to-unsigned conversion.
        byte firstByte = -25;
        byte[] buffer = { firstByte, 42 }; // The second byte is arbitrary.

        // The MergedStream should first read from the provided buffer (from index 0 to 2).
        MergedStream mergedStream = new MergedStream(ioContext, underlyingStream, buffer, 0, buffer.length);

        // The InputStream.read() method returns a byte as an unsigned int (0-255).
        // The bitwise AND with 0xFF converts the signed byte (-25) to its unsigned representation (231).
        int expectedValue = firstByte & 0xFF;

        // Act
        int actualValue = mergedStream.read();

        // Assert
        assertEquals("The byte should be read and converted to its unsigned integer equivalent.",
                expectedValue, actualValue);
    }
}