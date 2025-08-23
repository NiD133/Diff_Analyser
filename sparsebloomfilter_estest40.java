package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SparseBloomFilter} class.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that the asBitMapArray() method returns a long array of the correct size
     * to represent all the bits defined by the filter's Shape.
     * The array size must be calculated as ceil(numberOfBits / 64).
     */
    @Test
    public void testAsBitMapArrayReturnsArrayOfCorrectSize() {
        // Arrange
        // Use a number of bits that is not a multiple of 64 to ensure the ceiling
        // calculation for the array size is handled correctly.
        final int numberOfBits = 1618;
        final int numberOfHashFunctions = 5; // A typical value for k.
        final Shape shape = Shape.fromNM(numberOfBits, numberOfHashFunctions);
        final SparseBloomFilter bloomFilter = new SparseBloomFilter(shape);

        // The filter's content doesn't affect the size of the bitmap array,
        // but we merge a hasher to simulate a realistic, non-empty filter.
        final Hasher hasher = new EnhancedDoubleHasher("test data".getBytes());
        bloomFilter.merge(hasher);

        // Act
        final long[] bitMap = bloomFilter.asBitMapArray();

        // Assert
        // The expected length is ceil(1618 / 64.0), which is 26.
        // This can be calculated using integer arithmetic as (numberOfBits + 63) / 64.
        final int expectedLength = (numberOfBits + Long.SIZE - 1) / Long.SIZE;
        assertEquals("The bitmap array should be large enough to hold all the bits.",
                expectedLength, bitMap.length);
    }
}