package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;

public class LineIteratorRemoveTest {

    @Test
    public void testRemoveThrowsUnsupportedOperationException() {
        // Arrange: Create a StringReader and wrap it in a BufferedReader.
        String inputString = "!77U'||S(5";
        StringReader stringReader = new StringReader(inputString);
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        // Act: Create a LineIterator from the BufferedReader.
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // Assert:  Calling remove() should throw an UnsupportedOperationException.
        try {
            lineIterator.remove();
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException e) {
            // Expected exception.  This confirms that the remove() method is not supported.
            assertEquals("remove not supported", e.getMessage()); // Verify exception message.
        } finally {
            // Clean up (optional, but good practice): Close the LineIterator to release resources.
            LineIterator.closeQuietly(lineIterator);
        }
    }
}