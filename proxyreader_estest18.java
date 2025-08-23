package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.PipedReader;
import java.io.Reader;

/**
 * Tests for {@link ProxyReader} to ensure it correctly delegates method calls.
 */
public class ProxyReaderTest {

    /**
     * Tests that calling reset() on a ProxyReader throws an IOException if the
     * underlying delegate reader does not support the operation.
     */
    @Test(expected = IOException.class)
    public void resetShouldThrowIOExceptionWhenDelegateDoesNotSupportReset() throws IOException {
        // Arrange: Create a ProxyReader with a delegate that is known to not support reset().
        // PipedReader is a good example as its reset() method always throws an IOException.
        final Reader delegateReader = new PipedReader();
        final ProxyReader proxyReader = new TaggedReader(delegateReader);

        // Act: Attempt to reset the proxy reader. This call should be delegated
        // to the PipedReader, which will throw the expected IOException.
        proxyReader.reset();
    }
}