package org.apache.commons.io;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link LineIterator}.
 */
public class LineIteratorTest {

    /**
     * Tests that calling next() on an iterator whose underlying reader has been closed
     * throws an IllegalStateException. The exception should be caused by an IOException.
     */
    @Test
    public void nextShouldThrowIllegalStateExceptionWhenReaderIsClosed() throws IOException {
        // Arrange: Create a LineIterator with a reader that is already closed.
        Reader reader = new StringReader("This data will not be read.");
        LineIterator lineIterator = new LineIterator(reader);
        reader.close(); // Close the reader *before* attempting to iterate.

        // Act & Assert: Attempting to read the next line should fail.
        try {
            lineIterator.next();
            fail("Expected an IllegalStateException because the underlying reader is closed.");
        } catch (final IllegalStateException e) {
            // The exception is expected. Now, verify its cause.
            // The LineIterator is expected to wrap the underlying IOException.
            final Throwable cause = e.getCause();
            assertNotNull("The exception should have a cause.", cause);
            assertTrue("The cause should be an IOException.", cause instanceof IOException);
            assertEquals("The cause message should indicate a closed stream.", "Stream closed", cause.getMessage());
        }
    }
}