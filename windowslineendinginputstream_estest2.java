package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Tests that reading a single byte that is not a line-ending character
     * returns the byte unchanged and consumes one byte from the underlying stream.
     */
    @Test
    public void readShouldPassThroughNonLineEndingByte() throws IOException {
        // Arrange
        final byte[] inputData = new byte[]{0, 1, 2, 3, 4, 5};
        final InputStream rawInputStream = new ByteArrayInputStream(inputData);

        // The 'ensureLineFeedAtEos' parameter is false, so no CRLF is added at the end.
        final boolean ensureLineFeedAtEos = false;

        // Use try-with-resources to ensure the stream is closed automatically.
        try (final WindowsLineEndingInputStream windowsLineEndingStream =
                     new WindowsLineEndingInputStream(rawInputStream, ensureLineFeedAtEos)) {

            // Act
            final int firstByteRead = windowsLineEndingStream.read();

            // Assert
            // The first byte (0) should be returned as is, since it's not a CR or LF.
            assertEquals("The byte read should match the first byte of the input.", 0, firstByteRead);

            // The underlying stream should have one less byte available.
            final int expectedAvailable = inputData.length - 1;
            assertEquals("The underlying stream should have advanced by one byte.",
                    expectedAvailable, rawInputStream.available());
        }
    }
}