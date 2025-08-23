package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link ProxyReader}.
 */
public class ProxyReaderTest {

    /**
     * Tests that calling read(char[], int, int) with a length of zero
     * correctly returns zero, as specified by the java.io.Reader contract.
     * This ensures the proxy correctly delegates the call without altering behavior.
     */
    @Test
    public void readWithZeroLengthShouldReturnZero() throws IOException {
        // Arrange: Create a proxy reader with some underlying data.
        // We use TaggedReader as a concrete implementation of the abstract ProxyReader.
        final char[] buffer = new char[10];
        try (Reader proxyReader = new TaggedReader(new StringReader("test data"))) {

            // Act: Attempt to read 0 characters into the buffer.
            final int charsRead = proxyReader.read(buffer, 0, 0);

            // Assert: The number of characters read should be 0.
            assertEquals("Reading zero characters should return 0", 0, charsRead);
        }
    }
}