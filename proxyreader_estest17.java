package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.PipedReader;
import org.junit.Test;

/**
 * Tests for {@link ProxyReader} to ensure it correctly handles and propagates
 * exceptions from the underlying delegate reader.
 */
public class ProxyReaderTest {

    /**
     * Tests that when the delegate reader's skip() method throws an IOException,
     * the ProxyReader correctly propagates this exception.
     */
    @Test
    public void skipShouldPropagateIOExceptionFromUnderlyingReader() {
        // Arrange: Create a ProxyReader wrapping a delegate that is guaranteed to fail.
        // A PipedReader that is not connected to a PipedWriter will throw an
        // IOException on most operations, serving as a good test case.
        final PipedReader unconnectedReader = new PipedReader();
        // CloseShieldReader is a concrete subclass of the abstract ProxyReader.
        final ProxyReader proxyReader = CloseShieldReader.wrap(unconnectedReader);
        final long charactersToSkip = 100L;

        // Act & Assert: Verify that calling skip() on the proxy throws the expected exception.
        // The assertThrows method (available in JUnit 4.13+) is a clean way to assert
        // that a specific exception is thrown and to capture it for further inspection.
        final IOException thrown = assertThrows(IOException.class, () -> {
            proxyReader.skip(charactersToSkip);
        });

        // Further Assert: Check if the exception message is the one expected from the PipedReader.
        assertEquals("Pipe not connected", thrown.getMessage());
    }
}