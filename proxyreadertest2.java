package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ProxyReader}.
 */
class ProxyReaderTest {

    /**
     * A custom NullReader that returns 0 when a null CharBuffer is passed.
     * This allows us to verify that the proxy correctly delegates the call
     * without throwing a NullPointerException itself.
     */
    private static final class NullSafeReader extends NullReader {

        NullSafeReader() {
            super(0); // Size is irrelevant for this test.
        }

        @Override
        public int read(final CharBuffer target) throws IOException {
            // Return 0 for a null target, which is a predictable behavior for the test.
            return target == null ? 0 : super.read(target);
        }
    }

    /**
     * A minimal ProxyReader implementation for testing the abstract base class.
     */
    private static final class TestProxyReader extends ProxyReader {

        TestProxyReader(final Reader proxy) {
            super(proxy);
        }
    }

    /**
     * Tests that calling read() with a null CharBuffer is proxied correctly
     * and returns the value from the underlying reader.
     */
    @Test
    void testReadWithNullCharBufferReturnsZero() throws IOException {
        // Arrange: Create a reader that is designed to handle a null buffer gracefully.
        final Reader underlyingReader = new NullSafeReader();
        try (final ProxyReader proxyReader = new TestProxyReader(underlyingReader)) {

            // Act: Call the method under test with a null buffer.
            final int result = proxyReader.read((CharBuffer) null);

            // Assert: The proxy should delegate the call and return the underlying reader's result (0).
            assertEquals(0, result, "Reading from a null CharBuffer should return 0, as defined by the underlying reader.");
        }
    }
}