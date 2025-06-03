package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class NullReaderTest { // Renamed class for clarity

    @Test(timeout = 4000)
    public void testReadWithNullCharArray() { // More descriptive test name
        // Arrange: Create a NullReader instance.  The argument (-955L) is irrelevant for this test.
        NullReader nullReader = new NullReader((-955L));

        // Act & Assert:  Verify that calling read(null) throws a NullPointerException.
        try {
            nullReader.read((char[]) null);
            fail("Expected NullPointerException to be thrown."); // Explicit failure message if no exception
        } catch (NullPointerException e) {
            // Assert:  NullPointerException was caught, test passes.  No further checks on the exception's message is made as it's known to be null.
            // The EvoSuite generated comment "no message in exception (getMessage() returned null)" is already implied by the exception type.
            // Success! The expected exception was thrown.
        }
    }
}