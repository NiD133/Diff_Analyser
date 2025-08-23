package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that merging a BitMapExtractor containing an index that is out of bounds
     * for the filter's shape throws an IllegalArgumentException.
     */
    @Test
    public void mergeWithBitMapExtractorShouldThrowExceptionForIndexOutOfBounds() {
        // Arrange
        // Create a shape with 3 bits, so the maximum valid index is 2.
        final int numberOfBits = 3;
        final int numberOfHashFunctions = 3;
        final Shape shape = Shape.fromKM(numberOfHashFunctions, numberOfBits);
        final SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Create a bitmap with a bit set at an index (3) that is outside the
        // valid range [0, 2] for the given shape.
        // 1L << 3 corresponds to setting the 4th bit (index 3).
        final long[] outOfBoundsBitMap = { 1L << 3 };
        final BitMapExtractor extractorWithInvalidIndex = BitMapExtractor.fromBitMapArray(outOfBoundsBitMap);

        // Act & Assert
        try {
            filter.merge(extractorWithInvalidIndex);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message clearly states the error.
            final String expectedMessage = "Value in list 3 is greater than maximum value (2)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}