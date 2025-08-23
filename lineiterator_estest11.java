package org.apache.commons.io;

import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

/**
 * Contains tests for the {@link LineIterator} class.
 */
public class LineIteratorTest {

    /**
     * Tests that the close() method can be called on a newly created iterator
     * without any prior operations. This ensures that the iterator can be safely
     * closed even if it's never used to iterate, for example, in a
     * try-with-resources block where an early return occurs.
     */
    @Test
    public void testCloseOnUnusedIteratorSucceeds() throws IOException {
        // Arrange: Create a LineIterator with some sample content.
        StringReader reader = new StringReader("line1\nline2");
        LineIterator lineIterator = new LineIterator(reader);

        // Act & Assert:
        // The test's purpose is to ensure that closing an unused iterator
        // does not cause an error. The test passes if no exception is thrown.
        lineIterator.close();
    }
}