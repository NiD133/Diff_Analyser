package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for the {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that calling skip() with a negative argument returns 0, which is
     * consistent with the contract of {@link java.io.Reader#skip(long)}.
     */
    @Test
    public void testSkipWithNegativeArgumentReturnsZero() throws IOException {
        // Arrange
        // ProxyReader is abstract, so we use a concrete subclass for testing.
        // The content of the reader is irrelevant for this test case.
        final Reader underlyingReader = new StringReader("");
        final Reader proxyReader = new TaggedReader(underlyingReader);

        // Act
        final long skippedCount = proxyReader.skip(-1);

        // Assert
        assertEquals("Skipping a negative number of characters should return 0.", 0L, skippedCount);
    }
}