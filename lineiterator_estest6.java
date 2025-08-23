package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link LineIterator} focusing on its behavior with closed resources.
 */
public class LineIteratorTest {

    /**
     * Tests that calling next() on a LineIterator created with a closed reader
     * throws an IllegalStateException. The underlying cause should be an IOException.
     */
    @Test
    public void nextOnClosedReaderThrowsIllegalStateException() throws IOException {
        // Arrange: Create a LineIterator with a reader that is already closed.
        final StringReader reader = new StringReader("This content will not be read.");
        reader.close();

        final LineIterator lineIterator = new LineIterator(reader);

        // Act & Assert: Attempting to iterate should fail.
        try {
            lineIterator.next();
            fail("Expected an IllegalStateException to be thrown because the reader is closed.");
        } catch (final IllegalStateException e) {
            // Verify that the exception is thrown for the correct reason.
            final Throwable cause = e.getCause();
            assertNotNull("The IllegalStateException should have a cause.", cause);
            assertTrue("The cause should be an IOException.", cause instanceof IOException);
            assertEquals("The IOException should have the message 'Stream closed'.", "Stream closed", cause.getMessage());
        }
    }
}