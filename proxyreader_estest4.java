package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

// Note: The class name and inheritance from a scaffolding class are artifacts
// of the test generation tool. In a manually written test suite, this would
// typically be a standalone class named ProxyReaderTest.
public class ProxyReader_ESTestTest4 extends ProxyReader_ESTest_scaffolding {

    /**
     * Tests that the mark() method correctly delegates to the underlying reader,
     * allowing the stream to be subsequently reset to the marked position.
     * This verifies that the proxy mechanism for mark() and reset() is working.
     */
    @Test
    public void markShouldDelegateToUnderlyingReaderAndAllowReset() throws IOException {
        // Arrange
        final String testData = "abcdef";
        final StringReader underlyingReader = new StringReader(testData);
        // ProxyReader is abstract, so we use a concrete subclass (TaggedReader) for testing.
        final ProxyReader proxyReader = new TaggedReader(underlyingReader);

        // Advance the reader to the 4th character ('d' at index 3).
        final long skipped = proxyReader.skip(3);
        assertEquals(3, skipped);

        // Act: Mark the current position. The read-ahead limit must be large enough
        // for subsequent reads before the reset.
        proxyReader.mark(10);

        // Assert
        // 1. Read a couple of characters to move the stream past the marked position.
        assertEquals('d', (char) proxyReader.read());
        assertEquals('e', (char) proxyReader.read());

        // 2. Reset the reader, which should return it to the marked position.
        proxyReader.reset();

        // 3. Verify that the next character read is from the marked position ('d').
        assertEquals("After reset, reader should be at the marked position",
                'd', (char) proxyReader.read());
    }
}