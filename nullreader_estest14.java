package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Tests that attempting to skip more characters than the reader's total size
     * will only advance the position to the end of the reader, not beyond it.
     */
    @Test
    public void skipPastEndShouldNotAdvancePositionBeyondSize() throws IOException {
        // Arrange: Create a NullReader with a small, fixed size.
        final long readerSize = 1L;
        final NullReader reader = new NullReader(readerSize);

        // Act: Attempt to skip a number of characters greater than the reader's size.
        final long charactersToSkip = 1695L;
        reader.skip(charactersToSkip);

        // Assert: The position should be at the end of the reader, capped at its size.
        final long finalPosition = reader.getPosition();
        assertEquals("Position should be capped at the reader's size.", readerSize, finalPosition);
    }
}