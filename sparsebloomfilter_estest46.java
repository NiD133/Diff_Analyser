package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link SparseBloomFilter} class.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that attempting to merge a Bloom filter into another with a smaller,
     * incompatible shape throws an IllegalArgumentException.
     */
    @Test
    public void mergeWithIncompatibleShapeShouldThrowException() {
        // Arrange
        // 1. Create a "large" Bloom filter with 994 bits.
        Shape largeShape = Shape.fromKM(994, 994);
        SparseBloomFilter largeFilter = new SparseBloomFilter(largeShape);

        // 2. Populate the large filter. This hasher will generate indices up to 993.
        // The specific data is not important, only that it creates indices.
        Hasher hasher = new EnhancedDoubleHasher(new byte[4]);
        largeFilter.merge(hasher);

        // 3. Create a "small" Bloom filter with only 408 bits. This filter cannot
        // hold the high-numbered indices from the large filter.
        Shape smallShape = Shape.fromNM(1, 408);
        SparseBloomFilter targetFilter = new SparseBloomFilter(smallShape);

        // Act & Assert
        try {
            // Attempting to merge the large filter into the small one should fail.
            targetFilter.merge(largeFilter);
            fail("Expected an IllegalArgumentException to be thrown due to incompatible shapes.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception was thrown for the correct reason.
            // The message should indicate that an index from the source filter was too large
            // for the target filter.
            String expectedMessageFragment = "is greater than maximum value";
            assertTrue(
                "Exception message should explain the out-of-bounds error.",
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}