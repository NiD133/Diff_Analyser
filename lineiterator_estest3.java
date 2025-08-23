package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.io.StringReader;

/**
 * Contains tests for the {@link LineIterator} class.
 */
public class LineIteratorTest {

    /**
     * Tests that the default implementation of isValidLine() returns true for any given string.
     * The method is designed to be overridden, but its base behavior should always be to
     * consider any line as valid.
     */
    @Test
    public void isValidLine_shouldReturnTrueByDefault() {
        // Arrange: Create a LineIterator instance. The reader's content is not
        // relevant for this test, as we are calling isValidLine() directly.
        StringReader reader = new StringReader("irrelevant content");
        LineIterator lineIterator = new LineIterator(reader);
        String anyLine = "w*N4EtL4abL*9i`"; // An arbitrary string to validate

        // Act: Call the method under test.
        boolean isLineValid = lineIterator.isValidLine(anyLine);

        // Assert: Verify that the method returns true as expected.
        assertTrue("isValidLine() should return true for any string by default.", isLineValid);
    }
}