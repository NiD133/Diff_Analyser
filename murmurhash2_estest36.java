package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the 32-bit hash of an empty byte array with a specific seed
     * produces a known, pre-calculated hash value. This ensures the algorithm's
     * consistency and correctness for edge cases like empty input.
     */
    @Test
    public void hash32ShouldProduceKnownValueForEmptyArrayAndCustomSeed() {
        // Arrange
        final byte[] emptyData = new byte[0];
        final int seed = -1819289676;
        final int expectedHash = -563837603;

        // Act
        // The original test called a two-argument version, which is ambiguous.
        // We call the three-argument version to make the intent clear:
        // hash an array of length 0 with a specific seed.
        final int actualHash = MurmurHash2.hash32(emptyData, emptyData.length, seed);

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}