package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests for {@link ProxyReader}.
 */
public class ProxyReaderTest {

    /**
     * Tests that reading from a ProxyReader wrapping an empty source
     * immediately returns the end-of-file marker (-1).
     */
    @Test
    public void readCharArrayFromEmptyReaderShouldReturnEOF() throws IOException {
        // Arrange: Create a ProxyReader wrapping an empty source reader.
        // We use TaggedReader as a concrete implementation of the abstract ProxyReader.
        final StringReader emptyReader = new StringReader("");
        final ProxyReader proxyReader = new TaggedReader(emptyReader);
        final char[] buffer = new char[10];

        // Act: Attempt to read from the empty reader into the buffer.
        final int charsRead = proxyReader.read(buffer);

        // Assert: The read operation should return EOF, as the underlying stream is empty.
        assertEquals(IOUtils.EOF, charsRead);
    }
}