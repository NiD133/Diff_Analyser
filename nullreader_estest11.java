package org.apache.commons.io.input;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Tests that an EOFException is thrown when attempting to read past the end of the stream
     * if the 'throwEofException' flag is enabled.
     */
    @Test
    public void testReadPastEndWhenThrowEofExceptionIsTrueThrowsEOFException() throws IOException {
        // Arrange: Create a reader of size 1, configured to throw an exception at EOF.
        final long size = 1L;
        final boolean throwEofException = true;
        final boolean markSupported = false; // This parameter is not relevant for this test case.
        final NullReader reader = new NullReader(size, markSupported, throwEofException);

        // Act: Read the entire content of the reader to reach its end.
        // This is the setup for the main assertion.
        char[] buffer = new char[1];
        reader.read(buffer);

        // Assert: Verify that a subsequent read attempt throws an EOFException.
        assertThrows(EOFException.class, () -> reader.read(new char[10], 0, 10));
    }
}