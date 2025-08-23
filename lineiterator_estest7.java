package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;

// The original class name is kept to show the refactoring in context.
// In a real-world scenario, this would likely be part of a single `LineIteratorTest` class.
public class LineIterator_ESTestTest7 extends LineIterator_ESTest_scaffolding {

    /**
     * Tests that calling hasNext() on a LineIterator created with a closed reader
     * throws an IllegalStateException. The exception should be caused by an IOException.
     */
    @Test
    public void hasNextOnClosedReaderShouldThrowIllegalStateException() throws IOException {
        // Arrange: Create a reader and close it before passing it to the iterator.
        StringReader reader = new StringReader("This content will not be read.");
        reader.close();

        LineIterator iterator = new LineIterator(reader);

        // Act & Assert: Attempting to check for a next line should fail.
        try {
            iterator.hasNext();
            fail("Expected an IllegalStateException because the underlying reader is closed.");
        } catch (final IllegalStateException e) {
            // Verify that the exception is thrown for the correct reason.
            final Throwable cause = e.getCause();
            assertNotNull("The IllegalStateException should have a cause.", cause);
            assertTrue("The cause should be an IOException.", cause instanceof IOException);
            assertEquals("The cause's message should indicate a closed stream.", "Stream closed", cause.getMessage());
        }
    }
}