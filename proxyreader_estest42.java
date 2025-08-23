package org.apache.commons.io.input;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import org.junit.Test;

/**
 * Tests for {@link ProxyReader}.
 */
public class ProxyReaderTest {

    /**
     * A simple Reader implementation that tracks whether its close() method has been called.
     * This is used to verify that the proxy correctly delegates the close operation.
     */
    private static class CloseTrackerReader extends Reader {
        private boolean isClosed;

        @Override
        public void close() {
            this.isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) {
            // Not needed for this test.
            return -1;
        }
    }

    /**
     * Tests that calling close() on the ProxyReader also closes the underlying delegate reader.
     */
    @Test
    public void testCloseDelegatesToUnderlyingReader() throws IOException {
        // Arrange: Create a reader that can track if it has been closed,
        // and wrap it in a ProxyReader.
        final CloseTrackerReader underlyingReader = new CloseTrackerReader();
        // Since ProxyReader is abstract, we use a simple concrete implementation for the test.
        final ProxyReader proxyReader = new ProxyReader(underlyingReader) {
            // No additional implementation needed for this test case.
        };

        // Act: Close the proxy reader.
        proxyReader.close();

        // Assert: Verify that the underlying reader was also closed.
        assertTrue("Expected close() to be called on the underlying reader", underlyingReader.isClosed());
    }
}