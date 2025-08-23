package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the SimpleBloomFilter.
 * The original name suggests it was auto-generated. In a real-world scenario,
 * this test would be part of a larger SimpleBloomFilterTest class.
 */
public class SimpleBloomFilter_ESTestTest34 extends SimpleBloomFilter_ESTest_scaffolding {

    /**
     * Tests that SimpleBloomFilter.contains() throws an ArrayIndexOutOfBoundsException
     * when the IndexExtractor provides an index that is larger than the number of bits
     * defined by the filter's Shape.
     */
    @Test(timeout = 4000)
    public void containsShouldThrowExceptionWhenIndexIsOutOfBounds() {
        // Arrange
        // Create a filter with a small shape (10 bits).
        // The internal bitMap array will have a size of 1, as ceil(10 / 64) = 1.
        final int numberOfBits = 10;
        final int numberOfItems = 10;
        final Shape shape = Shape.fromNM(numberOfItems, numberOfBits);
        final SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Create an index extractor that provides an index far outside the filter's valid range [0, 9].
        final int outOfBoundsBitIndex = 977;
        final IndexExtractor indexExtractor = IndexExtractor.fromIndexArray(new int[]{outOfBoundsBitIndex});

        // The internal implementation in the BitMaps class calculates the long array index
        // by dividing the bit index by 64 (the number of bits in a long).
        // Accessing bit 977 requires accessing index 15 of the long array (977 / 64 = 15).
        final int expectedFailingArrayIndex = 15;

        // Act & Assert
        try {
            filter.contains(indexExtractor);
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown.");
        } catch (final ArrayIndexOutOfBoundsException e) {
            // Verify that the exception was thrown for the correct out-of-bounds array access.
            // The exception message is expected to contain the failing array index.
            final String expectedMessage = String.valueOf(expectedFailingArrayIndex);
            assertEquals("Exception message should be the failing array index", expectedMessage, e.getMessage());
        }
    }
}