package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Tests for {@link NullReader}.
 */
public class NullReaderTest {

    /**
     * Tests that calling skip() with a value of 0 returns 0 and does not
     * change the reader's position.
     */
    @Test
    public void skipZeroShouldReturnZeroAndNotAdvancePosition() throws IOException {
        // Arrange: Create a reader with a non-zero size.
        final NullReader reader = new NullReader(100L);
        assertEquals("Initial position should be 0.", 0L, reader.getPosition());

        // Act: Attempt to skip zero characters.
        final long skippedCount = reader.skip(0L);

        // Assert: Verify that zero characters were skipped and the position is unchanged.
        assertEquals("Skipping zero characters should return 0.", 0L, skippedCount);
        assertEquals("Position should not change after skipping zero.", 0L, reader.getPosition());
    }
}