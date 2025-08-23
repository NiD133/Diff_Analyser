package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link SimpleBloomFilter#merge(Hasher)} and {@link SimpleBloomFilter#merge(BitMapExtractor)} methods.
 */
public class SimpleBloomFilterMergeTest {

    /**
     * Tests that the merge methods correctly report whether the Bloom filter was modified.
     * - Merging a hasher into an empty filter should modify it and return true.
     * - Merging a filter with itself should not modify it and should return false.
     */
    @Test
    public void mergeShouldReturnTrueOnModificationAndFalseOnNoOp() {
        // Arrange: Create an empty Bloom filter and a hasher to populate it.
        Shape shape = Shape.fromNMK(78, 78, 2);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);
        Hasher hasher = new EnhancedDoubleHasher(1L, -9223372036854775808L);

        // Act 1: Merge the hasher into the empty filter.
        boolean wasModifiedByHasher = filter.merge(hasher);

        // Assert 1: The filter should have been modified.
        assertTrue("Merging a hasher into an empty filter should modify it and return true.", wasModifiedByHasher);

        // Act 2: Merge the filter with itself. Since all bits are already present,
        // this operation should not change the filter's state.
        boolean wasModifiedBySelfMerge = filter.merge((BitMapExtractor) filter);

        // Assert 2: The filter should not have been modified.
        assertFalse("Merging a filter with itself should not modify it and should return false.", wasModifiedBySelfMerge);
    }
}