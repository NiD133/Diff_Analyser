package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Tests for {@link ObservableInputStream}.
 */
public class ObservableInputStreamTest {

    @Test
    public void readFromEmptyStreamBuiltFromCharSequenceShouldReturnEOF() throws IOException {
        // Arrange
        // Build an ObservableInputStream that reads from an empty CharSequence.
        final ObservableInputStream inputStream = new ObservableInputStream.Builder()
                .setCharSequence("")
                .get();

        final byte[] buffer = new byte[8];

        // Act
        // Attempt to read from the stream, which should be at its end immediately.
        final int bytesRead = inputStream.read(buffer);

        // Assert
        // Verify that the read operation returns -1, indicating the end of the stream (EOF).
        assertEquals("Reading from an empty stream should return -1 for EOF.", -1, bytesRead);
    }
}