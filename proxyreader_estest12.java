package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for the {@link ProxyReader} class.
 * This test suite focuses on the delegation behavior of ProxyReader methods.
 */
public class ProxyReaderTest {

    /**
     * Tests that calling read(CharBuffer) with an empty buffer returns 0,
     * as specified by the java.io.Reader contract. The ProxyReader should
     * correctly delegate this call.
     */
    @Test
    public void readWithEmptyCharBufferShouldReturnZero() throws IOException {
        // Arrange: Create a proxy reader and an empty destination buffer.
        // A TaggedReader is used here as a concrete implementation of the abstract ProxyReader.
        final Reader underlyingReader = new StringReader("some data");
        final ProxyReader proxyReader = new TaggedReader(underlyingReader);
        final CharBuffer emptyBuffer = CharBuffer.wrap("");

        // Act: Attempt to read from the reader into the empty buffer.
        final int charsRead = proxyReader.read(emptyBuffer);

        // Assert: Verify that zero characters were read.
        assertEquals("Reading into an empty buffer should return 0 characters", 0, charsRead);
    }
}