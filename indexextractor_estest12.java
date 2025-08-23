package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link IndexExtractor}.
 */
public class IndexExtractorTest {

    /**
     * Tests that an IndexExtractor created from a BitMapExtractor correctly
     * returns an array containing the index of every set bit.
     */
    @Test
    public void asIndexArrayFromBitMapExtractorShouldReturnAllSetBitIndices() {
        // Arrange
        // The long value -2401L is chosen because its 64-bit two's complement
        // representation has exactly 60 bits set to 1.
        // We use Long.bitCount() to make this relationship explicit and avoid magic numbers.
        final long bitMapValue = -2401L;
        final int expectedIndexCount = Long.bitCount(bitMapValue);
        
        // This assertion confirms our premise and makes the test more robust.
        assertEquals(60, expectedIndexCount);

        final long[] sourceBitMap = { bitMapValue };
        final BitMapExtractor bitMapExtractor = BitMapExtractor.fromBitMapArray(sourceBitMap);
        final IndexExtractor indexExtractor = IndexExtractor.fromBitMapExtractor(bitMapExtractor);

        // Act
        final int[] extractedIndices = indexExtractor.asIndexArray();

        // Assert
        assertEquals("The number of extracted indices should match the number of set bits in the source bitmap.",
                expectedIndexCount, extractedIndices.length);
    }
}