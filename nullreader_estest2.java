package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Tests for {@link NullReader}.
 */
public class NullReaderTest {

    /**
     * Verifies that calling reset() restores the reader's position to the
     * location where mark() was previously called.
     */
    @Test
    public void testResetAfterMarkRestoresPosition() throws IOException {
        // Arrange
        final long readerSize = 100L;
        final int charsToRead = 4;
        final NullReader reader = new NullReader(readerSize, true, false); // Enable mark support
        final char[] buffer = new char[charsToRead];

        // Act
        // 1. Read some characters to advance the stream's position.
        reader.read(buffer);
        assertEquals("Position should be advanced after reading.", charsToRead, reader.getPosition());

        // 2. Mark the current position.
        reader.mark(10); // The read-ahead limit is not critical for this test.

        // 3. Reset the stream to the last marked position.
        reader.reset();

        // Assert
        // The position should be restored to where mark() was called.
        assertEquals("Position should be restored to the marked location after reset.",
                charsToRead, reader.getPosition());
    }
}