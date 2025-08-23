package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 * This class focuses on verifying the stream's line ending conversions.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Tests that a lone line feed ('\n') at the beginning of the stream
     * is correctly converted to a carriage return and line feed ("\r\n")
     * when reading a block of bytes.
     */
    @Test
    public void readBlockWithLoneLineFeedAtStartIsConvertedToCRLF() throws IOException {
        // Arrange
        // The input data starts with a lone Line Feed ('\n').
        final byte[] inputData = new byte[]{'\n', 'a', 'b', 'c', 'd', 'e'};
        final InputStream sourceStream = new ByteArrayInputStream(inputData);

        // The expected output should have a Carriage Return ('\r') prepended to the '\n'.
        // The read will consume 6 bytes from the transformed stream.
        final byte[] expectedData = new byte[]{'\r', '\n', 'a', 'b', 'c', 'd'};
        final byte[] actualData = new byte[6];

        // The 'ensureLineFeedAtEos' parameter is true, but it won't affect this specific
        // read because we are not reaching the end of the stream.
        try (final WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(sourceStream, true)) {
            // Act
            final int bytesRead = windowsStream.read(actualData);

            // Assert
            assertEquals("Should read the requested number of bytes.", 6, bytesRead);
            assertArrayEquals("A lone '\\n' should be converted to '\\r\\n'.", expectedData, actualData);
        }
    }
}