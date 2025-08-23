package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import org.junit.Test;

/**
 * Test suite for the {@link ProxyReader} class, focusing on exception propagation.
 */
public class ProxyReaderTest {

    /**
     * Tests that an {@link IOException} thrown by the underlying reader's {@code skip()}
     * method is correctly propagated by the {@link ProxyReader}.
     */
    @Test
    public void skipShouldPropagateIOExceptionFromUnderlyingReader() {
        // Arrange: Create a ProxyReader with a delegate reader that is known to fail.
        // A PipedReader that is not connected to a PipedWriter will throw an
        // IOException on read or skip operations.
        final Reader failingReader = new PipedReader();
        final ProxyReader proxyReader = new TaggedReader(failingReader);

        // Act & Assert
        try {
            proxyReader.skip(100L);
            fail("An IOException should have been thrown because the underlying pipe is not connected.");
        } catch (final IOException e) {
            // Verify that the expected exception was caught and has the correct message.
            assertEquals("Pipe not connected", e.getMessage());
        }
    }
}