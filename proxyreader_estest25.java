package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import org.junit.Test;

/**
 * Contains tests for the exception-handling behavior of the {@link ProxyReader}.
 */
public class ProxyReaderExceptionTest {

    /**
     * Tests that an IOException thrown by the underlying reader's read(char[]) method
     * is correctly propagated by the ProxyReader.
     */
    @Test
    public void readShouldPropagateIOExceptionFromUnderlyingReader() {
        // Arrange: Create a proxy reader that wraps a reader designed to fail.
        // A PipedReader that is not connected to a PipedWriter will throw an
        // IOException upon any read attempt.
        final Reader failingReader = new PipedReader();
        final ProxyReader proxyReader = new CloseShieldReader(failingReader);
        final char[] buffer = new char[10];

        // Act & Assert: Verify that calling read() on the proxy throws the expected
        // IOException from the underlying reader.
        final IOException thrown = assertThrows(IOException.class, () -> {
            proxyReader.read(buffer);
        });

        // Verify that the exception message is the one from the underlying reader.
        assertEquals("Pipe not connected", thrown.getMessage());
    }
}