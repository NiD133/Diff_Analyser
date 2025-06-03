package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.Reader;

public class BoundedReaderTest {

    @Test
    public void testNullReaderThrowsNullPointerException() {
        // Arrange: Create a BoundedReader with a null Reader and a limit.
        // This sets up a scenario where the underlying reader is null.
        BoundedReader boundedReader = new BoundedReader(null, 10); // Limit of 10 characters

        // Act: Attempt to read from the BoundedReader. This should trigger a NullPointerException
        // because the underlying reader is null.
        try {
            boundedReader.read(); // Attempt to read a single character.
            fail("Expected NullPointerException was not thrown."); // Fail the test if no exception occurs.
        } catch (NullPointerException e) {
            // Assert: Verify that a NullPointerException is thrown.  This confirms that the BoundedReader
            // correctly handles a null Reader by throwing the appropriate exception when read() is called.
            // We don't need to check the exception message as the purpose is to verify the exception type.
            // Exception caught, test passes.
        } catch (IOException e) {
            fail("Expected NullPointerException, but got IOException: " + e.getMessage());
        }
    }
}