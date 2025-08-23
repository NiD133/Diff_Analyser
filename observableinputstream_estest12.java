package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

/**
 * Unit tests for the {@link ObservableInputStream} class, focusing on basic read functionality.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that the basic {@code read()} method correctly reads and returns a single byte
     * from the underlying input stream.
     */
    @Test
    public void readShouldReturnSingleByteFromUnderlyingStream() throws IOException {
        // Arrange: Create an input stream with a single byte 'M'.
        final byte[] sourceData = { 'M' }; // ASCII value is 77
        final InputStream underlyingStream = new ByteArrayInputStream(sourceData);
        final ObservableInputStream observableStream = new ObservableInputStream(underlyingStream);

        // Act: Read one byte from the observable stream.
        final int byteRead = observableStream.read();

        // Assert: The byte read should be the one from the source data.
        assertEquals("The read byte should match the source byte.", 'M', byteRead);
    }
}