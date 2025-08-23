package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the SparseBloomFilter.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class SparseBloomFilter_ESTestTest32 extends SparseBloomFilter_ESTest_scaffolding {

    /**
     * Tests that merging an IndexExtractor containing an index greater than the
     * maximum allowed by the filter's shape throws an IllegalArgumentException.
     */
    @Test
    public void mergeWithOutOfBoundsIndexShouldThrowException() {
        // GIVEN: A Bloom filter with a specific shape and an index that is out of bounds.
        final int numberOfBits = 12; // The filter will have indices from 0 to 11.
        final Shape shape = Shape.fromNMK(12, 12, numberOfBits);
        final SparseBloomFilter filter = new SparseBloomFilter(shape);

        // An index equal to numberOfBits is invalid, as the max valid index is (numberOfBits - 1).
        final int invalidIndex = numberOfBits;
        final int[] indicesWithInvalidValue = { invalidIndex };
        final IndexExtractor extractorWithInvalidIndex = IndexExtractor.fromIndexArray(indicesWithInvalidValue);

        // WHEN: We attempt to merge the out-of-bounds index into the filter.
        try {
            filter.merge(extractorWithInvalidIndex);
            fail("Expected an IllegalArgumentException to be thrown for an out-of-bounds index.");
        } catch (IllegalArgumentException e) {
            // THEN: The correct exception is thrown with a descriptive message.
            final int maxValidIndex = numberOfBits - 1;
            final String expectedMessage = String.format(
                "Value in list %d is greater than maximum value (%d)",
                invalidIndex, maxValidIndex);
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}