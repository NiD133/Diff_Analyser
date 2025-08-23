package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the {@link SimpleBloomFilter#merge(IndexExtractor)} method.
 */
public class SimpleBloomFilterMergeTest {

    /**
     * Tests that merging an IndexExtractor with an index that is out of bounds
     * for the filter's shape throws an IllegalArgumentException. The valid index
     * range is [0, numberOfBits).
     */
    @Test
    public void mergeWithIndexExtractorShouldThrowExceptionForIndexOutOfBounds() {
        // Arrange
        final int numberOfBits = 1540;
        final int numberOfHashFunctions = 1;
        final Shape shape = Shape.fromKM(numberOfHashFunctions, numberOfBits);
        final SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);

        // Create an IndexExtractor that provides an index outside the valid range of [0, 1539].
        final int outOfBoundsIndex = 5023;
        final IndexExtractor invalidIndexExtractor = IndexExtractor.fromIndexArray(new int[]{outOfBoundsIndex});

        // Act & Assert
        try {
            bloomFilter.merge(invalidIndexExtractor);
            fail("Expected an IllegalArgumentException to be thrown for an out-of-bounds index.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is correct and informative.
            final String expectedMessage = String.format(
                "IndexExtractor should only send values in the range[0,%s)", numberOfBits);
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}