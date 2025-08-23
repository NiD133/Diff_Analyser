package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the XXHash32 class.
 */
public class XXHash32Test {

    /**
     * Tests that calling the update method with a zero-length segment
     * does not change the hash state. The final hash should be the
     * pre-calculated value for an empty input.
     */
    @Test
    public void testUpdateWithZeroLengthIsANoOp() {
        // Arrange
        final XXHash32 xxHash = new XXHash32(); // Uses the default seed of 0
        final byte[] anyData = new byte[8];

        // This is the pre-calculated hash for an empty input with a seed of 0.
        // It is the result of the finalization steps applied to the initial state.
        final long expectedHashForEmptyInput = 46947589L;

        // Act
        // Call update with a length of 0. This should not process any bytes.
        xxHash.update(anyData, 0, 0);
        final long actualHash = xxHash.getValue();

        // Assert
        assertEquals("Updating with zero length should not alter the initial hash value",
                expectedHashForEmptyInput, actualHash);
    }
}