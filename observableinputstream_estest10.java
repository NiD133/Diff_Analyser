package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Collections;
import org.junit.Test;

/**
 * Tests for {@link ObservableInputStream}.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that reading from an empty stream with invalid buffer arguments
     * returns EOF (-1) without throwing an IndexOutOfBoundsException.
     * <p>
     * This test verifies a specific behavior of the underlying {@link SequenceInputStream}.
     * When a SequenceInputStream is constructed with an empty enumeration of streams,
     * its internal stream is null. Its {@code read(byte[], int, int)} method checks for
     * this null state and returns -1 *before* it validates the offset and length arguments.
     * </p>
     */
    @Test
    public void readWithInvalidArgsOnEmptyStreamShouldReturnEOF() throws IOException {
        // Arrange
        // Create an empty stream using SequenceInputStream, which exhibits the desired pre-check behavior.
        final InputStream emptyStream = new SequenceInputStream(Collections.emptyEnumeration());
        final ObservableInputStream observableStream = new ObservableInputStream(emptyStream);

        final byte[] buffer = new byte[10];
        final int invalidOffset = -1; // An invalid offset that would normally cause an exception.
        final int anyLength = 5;

        // Act
        final int bytesRead = observableStream.read(buffer, invalidOffset, anyLength);

        // Assert
        assertEquals("Reading from an empty stream should return EOF, even with invalid arguments.", EOF, bytesRead);
    }
}