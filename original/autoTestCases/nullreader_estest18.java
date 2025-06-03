package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.EOFException;

public class NullReaderTest {

    @Test(timeout = 4000)
    public void testReadPastEndOfNullReaderThrowsEOFException() throws Throwable {
        // Arrange: Create a NullReader with a negative initial size, which is then skipped over.
        // The 'markSupported' is set to false, and 'throwEofOnExhaustion' is set to true. This will trigger the desired EOFException.
        NullReader nullReader = new NullReader((-2271L), false, true);

        // Act: Skip past the initial negative size (effectively resetting the read pointer).
        nullReader.skip((-2271L));  // Skipping a negative value effectively resets the counter.

        // Assert: Attempting to read from the reader should now throw an EOFException because the reader is effectively exhausted.
        try {
            nullReader.read();
            fail("Expected EOFException to be thrown, but it wasn't."); // Fail the test if no exception is thrown
        } catch (EOFException e) {
            // Expected: The EOFException confirms that the reader has been exhausted.
            // The original test just verifies that the exception is thrown, we don't need to check the message.
        }
    }
}