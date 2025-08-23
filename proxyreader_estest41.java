package org.apache.commons.io.input;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Unit tests for the {@link ProxyReader} class, focusing on its core proxying behavior.
 */
public class ProxyReaderTest {

    /**
     * Tests that the ready() method correctly delegates to the underlying reader.
     * When the underlying reader is ready (i.e., has data to be read), the
     * ProxyReader should also report that it is ready.
     */
    @Test
    public void testReadyDelegatesToUnderlyingReader() throws IOException {
        // Arrange: Create an underlying reader that has content and is therefore "ready".
        final StringReader underlyingReader = new StringReader("test data");

        // Use CloseShieldReader as a concrete implementation of the abstract ProxyReader
        // to test the inherited proxying behavior.
        final ProxyReader proxyReader = CloseShieldReader.wrap(underlyingReader);

        // Act: Call the ready() method on the proxy.
        final boolean isReady = proxyReader.ready();

        // Assert: The result should be true, reflecting the state of the underlying reader.
        assertTrue("The proxy reader should be ready because the underlying reader is.", isReady);
    }
}