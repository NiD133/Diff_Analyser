package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// NOTE: The test class name and inheritance are preserved from the original.
// A more conventional name would be SparseBloomFilterTest.
public class SparseBloomFilter_ESTestTest3 extends SparseBloomFilter_ESTest_scaffolding {

    /**
     * Tests that merging a Hasher into an empty filter correctly marks the filter as non-empty
     * and that the merge operation reports that a change occurred.
     */
    @Test(timeout = 4000)
    public void testMergeWithHasherMakesFilterNotEmpty() {
        // Arrange: Create an empty sparse Bloom filter and a hasher.
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter filter = new SparseBloomFilter(shape);
        assertTrue("A new filter should be empty before any operations", filter.isEmpty());

        // Use a non-empty byte array to generate hash indices.
        byte[] dataToHash = new byte[] {1, 2, 3, 4, 5, 6};
        Hasher hasher = new EnhancedDoubleHasher(dataToHash);

        // Act: Merge the hasher into the filter.
        boolean wasModified = filter.merge(hasher);

        // Assert: The filter should now contain indices, and the merge operation should report this change.
        assertTrue("merge() should return true when adding indices to an empty filter", wasModified);
        assertFalse("Filter should no longer be empty after merging with a hasher", filter.isEmpty());
    }
}