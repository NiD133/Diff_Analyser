package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for the SimpleBloomFilter, focusing on exception handling.
 * Note: The original test was part of an auto-generated suite. This version has been
 * refactored for clarity.
 */
public class SimpleBloomFilterExceptionTest {

    /**
     * Tests that merging an IndexExtractor with an index outside the valid range
     * (specifically, a negative index) throws an IllegalArgumentException.
     * The valid range of indices is defined by the Bloom filter's shape.
     */
    @Test
    public void mergeWithIndexExtractorContainingNegativeIndexShouldThrowException() {
        // 1. Arrange: Create a Bloom filter with a defined shape.
        // The shape defines the number of bits, so valid indices are in the range [0, 9].
        Shape shape = Shape.fromNM(10, 10);
        SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);

        // Create an IndexExtractor that provides an invalid (negative) index.
        int[] indicesWithInvalidValue = {-89};
        IndexExtractor invalidIndexExtractor = IndexExtractor.fromIndexArray(indicesWithInvalidValue);

        // 2. Act & Assert: Attempt the merge and verify the exception.
        try {
            bloomFilter.merge(invalidIndexExtractor);
            fail("Expected an IllegalArgumentException to be thrown due to the negative index.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message clearly states the problem and the valid range.
            String expectedMessage = "IndexExtractor should only send values in the range[0,10)";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}