package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Test suite for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Tests that skip() on a NullReader with a negative size immediately returns EOF.
     * <p>
     * A NullReader initialized with a negative size is considered to be at the
     * end-of-file from the very beginning, because its initial position (0) is
     * greater than or equal to its size.
     * </p>
     */
    @Test(timeout = 4000)
    public void skipOnReaderWithNegativeSizeShouldReturnEof() throws IOException {
        // Arrange: Create a NullReader with a negative size.
        final long negativeSize = -3294L;
        final boolean markSupported = false;
        final boolean throwEofException = false;
        NullReader reader = new NullReader(negativeSize, markSupported, throwEofException);

        // The initial position is 0, which is already past the negative "end" of the reader.
        long initialPosition = reader.getPosition();
        assertEquals("Initial position should be 0", 0L, initialPosition);

        // Act: Attempt to skip any number of characters.
        long skippedCount = reader.skip(-2904L);

        // Assert: The skip operation should detect EOF, return -1, and not change the position.
        assertEquals("Should return -1 to indicate EOF", -1L, skippedCount);
        assertEquals("Position should not change when at EOF", initialPosition, reader.getPosition());
    }
}