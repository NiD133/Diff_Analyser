package org.apache.commons.io.input;

import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.fail;

public class BoundedReaderTest {

    @Test(timeout = 4000)
    public void testCloseWithNullReader() {
        // Arrange: Create a BoundedReader with a null underlying reader and a zero limit.
        BoundedReader boundedReader = new BoundedReader(null, 0);

        // Act: Attempt to close the BoundedReader.  We expect a NullPointerException because
        // the underlying reader is null.
        try {
            boundedReader.close();
            fail("Expected NullPointerException when closing BoundedReader with null reader.");
        } catch (NullPointerException e) {
            // Assert:  The NullPointerException is caught, indicating the expected behavior.
            // No further assertion needed, as the catch block indicates success. The specific message
            // of the exception isn't critical to this test.
        }
    }
}