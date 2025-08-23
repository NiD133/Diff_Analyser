package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Tests that reading from an empty input stream immediately returns EOF (-1)
     * when the option to ensure a final line ending is disabled.
     */
    @Test
    public void shouldReturnEofForEmptyStreamWhenNotEnsuringLineFeedAtEos() throws IOException {
        // Arrange: Create an empty input stream and wrap it.
        // The 'lineFeedAtEos' parameter is set to false, so no CRLF should be added.
        final InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        final boolean ensureLineFeedAtEos = false;
        final WindowsLineEndingInputStream windowsStream = new WindowsLineEndingInputStream(emptyStream, ensureLineFeedAtEos);

        // Act: Attempt to read from the stream.
        final int result = windowsStream.read();

        // Assert: The stream should be at its end.
        final int EOF = -1;
        assertEquals("Should return EOF for an empty stream", EOF, result);
    }
}