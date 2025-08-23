package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.PipedReader;
import org.junit.Test;

/**
 * Tests for the {@link ProxyReader} class, ensuring it correctly delegates
 * method calls and propagates exceptions from the underlying reader.
 */
public class ProxyReaderTest {

    /**
     * Verifies that an IOException thrown by the underlying reader's read(char[])
     * method is correctly propagated through the ProxyReader.
     */
    @Test
    public void readWithBufferShouldPropagateIOExceptionFromUnderlyingReader() {
        // Arrange: Create a reader that is guaranteed to throw an IOException on read.
        // An unconnected PipedReader serves this purpose perfectly.
        final PipedReader unconnectedPipe = new PipedReader();
        // CloseShieldReader is a concrete subclass of ProxyReader suitable for this test.
        final ProxyReader proxyReader = CloseShieldReader.wrap(unconnectedPipe);
        final char[] buffer = new char[8];

        // Act & Assert: Verify that calling read() on the proxy throws the expected
        // IOException from the underlying (unconnected) pipe.
        final IOException thrown = assertThrows(IOException.class, () -> {
            proxyReader.read(buffer);
        });

        // Further assert that the exception message is the one from the delegate.
        assertEquals("Pipe not connected", thrown.getMessage());
    }
}