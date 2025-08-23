package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;

/**
 * Tests for {@link ProxyReader}.
 */
public class ProxyReaderTest {

    /**
     * Tests that an IOException thrown by the underlying reader's read(char[], int, int)
     * method is correctly propagated by the ProxyReader.
     */
    @Test
    public void readWithBuffer_whenUnderlyingReaderThrowsIOException_propagatesException() {
        // Arrange: Create a ProxyReader wrapping a reader that is guaranteed to throw an
        // IOException on read. An unconnected PipedReader serves this purpose well.
        Reader underlyingReader = new PipedReader();
        // We use TaggedReader as a concrete implementation of the abstract ProxyReader.
        ProxyReader proxyReader = new TaggedReader(underlyingReader);
        char[] buffer = new char[10];

        // Act & Assert
        try {
            proxyReader.read(buffer, 0, buffer.length);
            fail("Expected an IOException to be thrown because the underlying PipedReader is not connected.");
        } catch (final IOException e) {
            // Verify that the specific exception from the underlying reader was propagated.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}