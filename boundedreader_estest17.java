package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that calling mark() on a BoundedReader propagates the IOException
     * from the underlying reader if it has been closed.
     */
    @Test
    public void markShouldThrowIOExceptionWhenUnderlyingReaderIsClosed() throws IOException {
        // Arrange: Create a reader and close it immediately.
        final Reader closedReader = new StringReader("This content will not be read.");
        closedReader.close();

        // Wrap the closed reader in a BoundedReader.
        final BoundedReader boundedReader = new BoundedReader(closedReader, 100);
        final int arbitraryReadAheadLimit = 50;

        // Act & Assert: Verify that calling mark() throws an IOException.
        // We use assertThrows for a clear and concise exception check.
        IOException thrown = assertThrows(
            IOException.class,
            () -> boundedReader.mark(arbitraryReadAheadLimit)
        );

        // Further assert that the exception message is the one expected from the closed StringReader.
        assertEquals("Stream closed", thrown.getMessage());
    }
}