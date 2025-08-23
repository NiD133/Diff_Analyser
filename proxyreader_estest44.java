package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;

// Note: The class name and inheritance are artifacts from a test generation tool.
// In a real-world scenario, this test would be part of a consolidated `ProxyReaderTest` class.
public class ProxyReader_ESTestTest44 extends ProxyReader_ESTest_scaffolding {

    /**
     * Tests that calling mark() with a negative read-ahead limit throws an IllegalArgumentException.
     * This verifies that the method call is correctly delegated to the underlying reader,
     * which is responsible for input validation.
     */
    @Test
    public void markWithNegativeReadAheadLimitShouldThrowIllegalArgumentException() throws IOException {
        // Arrange
        // A TaggedReader is used as a concrete implementation of the abstract ProxyReader.
        final StringReader underlyingReader = new StringReader("test-data");
        final ProxyReader proxyReader = new TaggedReader(underlyingReader);
        final int invalidReadAheadLimit = -1;

        // Act & Assert
        try {
            proxyReader.mark(invalidReadAheadLimit);
            fail("Expected an IllegalArgumentException to be thrown for a negative read-ahead limit.");
        } catch (final IllegalArgumentException e) {
            // This assertion confirms that the exception originates from the underlying
            // java.io.StringReader, which is responsible for throwing this specific error.
            assertEquals("Read-ahead limit < 0", e.getMessage());
        }
    }
}