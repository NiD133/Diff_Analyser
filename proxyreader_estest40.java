package org.apache.commons.io.input;

import static org.junit.Assert.assertFalse;

import java.io.PipedReader;
import java.io.Reader;
import org.junit.Test;

/**
 * Unit tests for {@link ProxyReader}.
 */
public class ProxyReaderTest {

    /**
     * Tests that {@link ProxyReader#markSupported()} correctly delegates the call
     * to the underlying reader, returning false when the underlying reader does
     * not support marking.
     */
    @Test
    public void markSupportedShouldReturnFalseWhenUnderlyingReaderDoesNotSupportIt() {
        // Arrange: Create a ProxyReader with an underlying reader (PipedReader)
        // that is known *not* to support the mark() operation.
        final Reader underlyingReader = new PipedReader();
        // We use TaggedReader as a concrete subclass of the abstract ProxyReader for this test.
        final ProxyReader proxyReader = new TaggedReader(underlyingReader);

        // Act: Call the markSupported() method on the proxy.
        final boolean isMarkSupported = proxyReader.markSupported();

        // Assert: The call should be delegated to the underlying reader,
        // which returns false.
        assertFalse("ProxyReader should report mark is not supported when the delegate does not.", isMarkSupported);
    }
}