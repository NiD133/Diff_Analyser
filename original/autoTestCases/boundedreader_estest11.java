package org.apache.commons.io.input;

import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class BoundedReaderTest {

    @Test(timeout = 4000)
    public void testMark_WithNullReader_ThrowsNullPointerException() {
        // Arrange: Create a BoundedReader with a null Reader and a limit of 0.
        BoundedReader boundedReader = new BoundedReader(null, 0);

        // Act & Assert: Attempting to call mark() on the BoundedReader should throw a NullPointerException.
        try {
            boundedReader.mark(0); // Attempt to mark the current position
            fail("Expected NullPointerException, but no exception was thrown."); // Fail the test if no exception is thrown.
        } catch (NullPointerException e) {
            // Expected exception caught. Test passes.
            // Optionally, you can add assertions here to check the exception's message or other properties.
            // However, the EvoSuite generated test doesn't include a message check, so we'll omit it here for consistency with the original test.
        }
    }
}