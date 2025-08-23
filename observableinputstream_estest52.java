package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link ObservableInputStream}.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling read() on an empty stream correctly returns the
     * end-of-file marker without reading any data.
     */
    @Test
    public void readOnEmptyStreamShouldReturnEof() throws IOException {
        // Arrange: Create an ObservableInputStream that wraps an empty input stream.
        final byte[] emptyData = new byte[0];
        final ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(emptyData);
        
        try (final ObservableInputStream observableInputStream = new ObservableInputStream(emptyInputStream)) {
            // Act: Attempt to read a single byte from the stream.
            final int result = observableInputStream.read();

            // Assert: The result should be the end-of-file marker.
            assertEquals("Reading from an empty stream should return EOF", EOF, result);
        }
    }
}