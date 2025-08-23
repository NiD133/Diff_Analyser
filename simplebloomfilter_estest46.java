package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that a SimpleBloomFilter is initialized with a correctly sized internal
     * bitmap array. The size of this array is determined by the number of bits
     * specified in the filter's Shape, where the array length must be sufficient
     * to hold all the bits.
     */
    @Test
    public void testConstructorAllocatesCorrectlySizedBitMapArray() {
        // Arrange
        // Define a shape with a large number of bits to test the allocation logic.
        final int numberOfBits = 2_147_483_605;
        final int numberOfItems = 1000; // This value is not critical for this test.
        final Shape shape = Shape.fromNM(numberOfItems, numberOfBits);

        // The expected size of the long array is the number of bits divided by 64
        // (the number of bits in a long), rounded up to the nearest integer.
        // This is equivalent to Math.ceil(numberOfBits / 64.0).
        final int expectedBitMapArrayLength = (numberOfBits + 63) / 64;

        // Act
        final SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);
        final long[] bitMapArray = bloomFilter.asBitMapArray();

        // Assert
        assertEquals("The bitmap array should have the correct calculated size",
                expectedBitMapArrayLength, bitMapArray.length);
    }
}