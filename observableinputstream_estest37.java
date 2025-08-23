package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for the {@link ObservableInputStream} class.
 */
public class ObservableInputStreamTest {

    /**
     * Verifies that calling read() with a zero-length buffer returns 0,
     * as specified by the InputStream contract.
     */
    @Test
    public void readWithEmptyBufferShouldReturnZero() throws IOException {
        // Arrange
        // An empty stream is used, though its content is irrelevant since the
        // underlying stream is not read from when the buffer length is zero.
        final InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        final ObservableInputStream observableInputStream = new ObservableInputStream(emptyStream);
        final byte[] emptyBuffer = new byte[0];

        // Act
        final int bytesRead = observableInputStream.read(emptyBuffer);

        // Assert
        // The InputStream#read(byte[]) contract states that if the buffer length is zero,
        // then no bytes are read and 0 must be returned.
        assertEquals("Should return 0 when reading into an empty buffer", 0, bytesRead);
    }
}