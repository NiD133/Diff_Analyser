package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Tests that reading zero characters from a NullReader returns 0 and does not
     * advance the reader's position, even when the position is negative.
     */
    @Test
    public void readWithZeroLengthShouldReturnZeroAndNotChangePosition() throws IOException {
        // Arrange: Create a NullReader and set its internal position to a negative value
        // by skipping backwards. This tests an edge case.
        final long initialSize = -1L; // The size is not relevant for this test.
        final NullReader reader = new NullReader(initialSize);

        final long negativePosition = -5663L;
        reader.skip(negativePosition);
        assertEquals("Precondition failed: Position should be negative after skip.",
                negativePosition, reader.getPosition());

        // Act: Attempt to read zero characters. According to the Reader contract,
        // this should do nothing and return 0.
        final int charsRead = reader.read(null, 0, 0);

        // Assert: Verify that the read operation returned 0 and the position is unchanged.
        assertEquals("A zero-length read should return 0.", 0, charsRead);
        assertEquals("Position should not change after a zero-length read.",
                negativePosition, reader.getPosition());
    }
}