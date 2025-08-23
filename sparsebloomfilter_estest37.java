package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the SparseBloomFilter.
 * Note: The original class name and structure suggest it was auto-generated.
 * This refactored version focuses on improving a single test case for clarity.
 */
public class SparseBloomFilter_ESTestTest37 extends SparseBloomFilter_ESTest_scaffolding {

    /**
     * Tests that merging a Bloom filter with itself does not change its state
     * and correctly returns false.
     */
    @Test(timeout = 4000)
    public void testMergeWithSelfReturnsFalseWhenFilterIsUnchanged() {
        // Arrange: Create a non-empty Bloom filter.
        // Using smaller, more typical values for k and m makes the test easier to reason about.
        Shape shape = Shape.fromKM(10, 100);
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Populate the filter by merging it with a hasher.
        byte[] dataToHash = {1, 2, 3, 4};
        Hasher hasher = new EnhancedDoubleHasher(dataToHash);

        // Act & Assert (First Merge): Merging into an empty filter should change it.
        boolean wasModifiedByHasher = filter.merge(hasher);
        assertTrue("Merging a hasher into an empty filter should modify it and return true.", wasModifiedByHasher);
        
        int cardinalityBeforeSelfMerge = filter.cardinality();
        assertTrue("Filter should contain elements after the first merge.", cardinalityBeforeSelfMerge > 0);

        // Act & Assert (Second Merge): Merging the filter with itself should not change it.
        boolean wasModifiedBySelfMerge = filter.merge(filter);

        assertFalse("Merging a filter with itself should not modify it and should return false.", wasModifiedBySelfMerge);
        assertEquals("Cardinality should remain unchanged after merging with self.",
                cardinalityBeforeSelfMerge, filter.cardinality());
    }
}