package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.EOFException;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link NullReader}.
 */
public class NullReaderTest {

    /**
     * Tests that read() throws an EOFException if the reader is configured to do so
     * and the end of the stream has been reached.
     */
    @Test
    public void testReadThrowsEOFExceptionWhenPastEndAndConfiguredToThrow() throws IOException {
        // Arrange: Create a NullReader of a fixed size, configured to throw an
        // exception when the end of the file (EOF) is reached.
        // Constructor args are: size, markSupported, throwEofException.
        final long size = 7L;
        final NullReader reader = new NullReader(size, true, true);

        // Act: Skip all the characters to reach the end of the emulated stream.
        long skipped = reader.skip(size);
        assertEquals("Should have skipped all characters to reach the end", size, skipped);

        // Assert: Attempting to read past the end should throw the configured exception.
        try {
            reader.read();
            fail("Expected an EOFException to be thrown when reading past the end of the stream.");
        } catch (final EOFException e) {
            // This is the expected behavior. The test passes.
        }
    }
}