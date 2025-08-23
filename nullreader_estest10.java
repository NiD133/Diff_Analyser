package org.apache.commons.io.input;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Tests for the {@link NullReader} class, focusing on its behavior at the end of the emulated stream.
 */
public class NullReaderTest {

    /**
     * Verifies that skip() throws an EOFException on an empty reader
     * when it's configured to do so.
     */
    @Test(expected = EOFException.class)
    public void testSkipOnEmptyReaderWhenThrowEofExceptionIsTrueThrowsEOFException() throws IOException {
        // Arrange: Create a NullReader with a size of 0.
        // The constructor is NullReader(size, markSupported, throwEofException).
        // The key setting for this test is the final argument, 'throwEofException', set to true.
        final NullReader emptyReader = new NullReader(0, true, true);

        // Act: Attempt to skip any number of characters. Since the reader is empty,
        // this should immediately trigger the end-of-file condition.
        emptyReader.skip(512L);

        // Assert: An EOFException is expected, which is declared by the
        // @Test(expected = ...) annotation. If no exception is thrown, the test will fail.
    }
}