package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;
import org.junit.Test;

/**
 * Tests for {@link ProxyReader} to ensure it correctly delegates method calls.
 */
public class ProxyReaderTest {

    /**
     * Tests that calling reset() on a ProxyReader throws an IOException
     * if the underlying reader does not support the reset operation.
     * The ProxyReader is expected to delegate the call and propagate the resulting exception.
     */
    @Test
    public void testResetThrowsIOExceptionWhenUnderlyingReaderDoesNotSupportIt() {
        // Arrange: Use a PipedReader as the underlying reader because it does not
        // support the mark/reset operations. We use CloseShieldReader as a concrete
        // implementation of the abstract ProxyReader.
        final Reader underlyingReader = new PipedReader();
        final ProxyReader proxyReader = CloseShieldReader.wrap(underlyingReader);

        // Act & Assert
        try {
            proxyReader.reset();
            fail("Expected an IOException to be thrown because PipedReader does not support reset().");
        } catch (final IOException e) {
            // Verify that the exception is the one thrown by the base java.io.Reader.reset()
            // implementation, confirming the call was delegated correctly.
            assertEquals("mark/reset not supported", e.getMessage());
        }
    }
}