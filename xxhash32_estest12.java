package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link XXHash32} class.
 */
public class XXHash32ImprovedTest {

    /**
     * Tests that updating the hash with a segment of a zero-filled byte array
     * produces the correct, known hash value. This verifies that the
     * update(byte[], int, int) method correctly handles offsets and lengths.
     */
    @Test
    public void testUpdateWithSubArrayOfZeroesProducesCorrectHash() {
        // Arrange
        final int offset = 21;
        final int length = 21;
        // This is the pre-calculated hash for 21 zero bytes using the default seed (0).
        final long expectedHash = 86206869L;

        // Create an array larger than the hashed segment to ensure the offset and length
        // parameters are respected. The array is filled with zeros by default.
        final byte[] data = new byte[100];
        final XXHash32 hasher = new XXHash32();

        // Act
        hasher.update(data, offset, length);
        final long actualHash = hasher.getValue();

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}