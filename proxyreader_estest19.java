package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedReader;

/**
 * Contains tests for the {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that an IOException thrown by the underlying reader's {@code ready()}
     * method is correctly propagated by the {@code ProxyReader}.
     */
    @Test
    public void readyShouldPropagateIOExceptionFromUnderlyingReader() {
        // Arrange: Create a reader that is guaranteed to throw an IOException when ready() is called.
        // An unconnected PipedReader serves this purpose well.
        final PipedReader unconnectedReader = new PipedReader();
        final ProxyReader proxyReader = new TaggedReader(unconnectedReader);

        // Act & Assert: Verify that calling ready() on the proxy throws the expected exception.
        try {
            proxyReader.ready();
            fail("Expected an IOException to be thrown because the underlying reader is not ready.");
        } catch (final IOException e) {
            // The test is successful if an IOException is caught.
            // We also assert the message to ensure it's the correct exception.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}