package org.apache.commons.io.input;

import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import org.apache.commons.io.output.NullWriter;
import org.junit.Test;

/**
 * Tests for {@link ProxyReader}.
 */
public class ProxyReaderTest {

    /**
     * Tests that the markSupported() method correctly delegates to the underlying reader.
     * A StringReader is used as the delegate because it is known to support marking.
     */
    @Test
    public void markSupportedShouldDelegateToUnderlyingReader() {
        // Arrange: Create a proxy reader with an underlying reader that supports marking.
        // TeeReader is a concrete subclass of ProxyReader.
        // StringReader is a reader that is known to return true for markSupported().
        final Reader underlyingReader = new StringReader("test data");
        final Reader proxyReader = new TeeReader(underlyingReader, NullWriter.INSTANCE);

        // Act: Call markSupported() on the proxy reader.
        final boolean isMarkSupported = proxyReader.markSupported();

        // Assert: The result should be true, as the call is delegated to the StringReader.
        assertTrue("markSupported() should be delegated to the underlying reader.", isMarkSupported);
    }
}