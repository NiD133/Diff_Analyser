package org.apache.commons.io.input;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Tests that calling read() on a reader that is already at its end and is
     * configured to throw an exception on EOF, does indeed throw an EOFException.
     */
    @Test(expected = EOFException.class)
    public void readOnEndedReaderShouldThrowEOFExceptionWhenConfigured() throws IOException {
        // Arrange: Create a NullReader that is already at its end (due to a negative size)
        // and is configured to throw an exception upon reaching the end of the file.
        final long size = -1L;
        final boolean markSupported = true; // This parameter is not relevant for this test case.
        final boolean throwEofException = true;
        final NullReader reader = new NullReader(size, markSupported, throwEofException);

        // Act: Attempt to read from the exhausted reader.
        // Assert: An EOFException is expected, as specified by the @Test annotation.
        reader.read();
    }
}