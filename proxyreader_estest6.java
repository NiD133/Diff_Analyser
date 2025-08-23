package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.io.StringReader;

/**
 * Contains tests for the ProxyReader class, using CloseShieldReader as a concrete implementation.
 */
public class ProxyReaderTest {

    /**
     * Verifies that a CloseShieldReader reports itself as "not ready" after its close() method is called.
     * <p>
     * The {@link CloseShieldReader#close()} method is designed to prevent the underlying
     * reader from being closed. It achieves this by internally replacing the delegate
     * with a closed reader instance, making the proxy itself behave as if it were closed.
     * </p>
     *
     * @throws IOException Should not be thrown in this test case.
     */
    @Test
    public void readyShouldReturnFalseAfterProxyReaderIsClosed() throws IOException {
        // Arrange: Create a reader and wrap it in a CloseShieldReader, which is a type of ProxyReader.
        final StringReader sourceReader = new StringReader("test data");
        final CloseShieldReader shieldedReader = CloseShieldReader.wrap(sourceReader);

        // Act: Close the proxy reader. This action makes the shieldedReader behave as if closed,
        // without affecting the underlying sourceReader.
        shieldedReader.close();
        final boolean isReady = shieldedReader.ready();

        // Assert: The proxy reader should now report that it is not ready for reading.
        assertFalse("A reader wrapped by a closed CloseShieldReader should not be ready.", isReady);
    }
}