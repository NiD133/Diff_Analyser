package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;


public class BoundedReaderTest {

    @Test(timeout = 4000)
    public void testMarkWithNegativeReadAheadLimit() {
        // Arrange: Create a StringReader and a BoundedReader with a negative limit.
        StringReader stringReader = new StringReader("Some test string");
        BoundedReader boundedReader = new BoundedReader(stringReader, -585);

        // Act & Assert: Attempting to mark the reader with a negative read-ahead limit should throw an IllegalArgumentException.
        try {
            boundedReader.mark(-585);
            fail("Expected IllegalArgumentException was not thrown."); // This will fail the test if no exception is thrown.
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is what we expect. This strengthens the test.
            assertEquals("Read-ahead limit < 0", e.getMessage());
        }
    }
}