package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link SimpleBloomFilter#merge(BloomFilter)} method.
 */
public class SimpleBloomFilterMergeTest {

    /**
     * Tests that merging an empty Bloom filter into another empty Bloom filter
     * does not change the target filter and correctly returns {@code false} to
     * indicate that no modification occurred.
     */
    @Test
    public void testMergeWithEmptyFilterIntoEmptyFilterReturnsFalseAndIsUnchanged() {
        // Arrange: Create two empty Bloom filters with the same shape.
        final Shape shape = Shape.fromNM(2, 2);
        final SimpleBloomFilter targetFilter = new SimpleBloomFilter(shape);
        final BloomFilter<?> emptyFilterToMerge = new SparseBloomFilter(shape);

        // Pre-condition check: Ensure the target filter is empty before the merge.
        assertTrue("Target filter should be empty before the operation", targetFilter.isEmpty());

        // Act: Merge the empty filter into the target filter.
        final boolean wasModified = targetFilter.merge(emptyFilterToMerge);

        // Assert: The merge operation should report no change, and the target filter's
        // state should remain unchanged (i.e., still empty).
        assertFalse("Merging an empty filter should return false, indicating no change", wasModified);
        assertTrue("Target filter should remain empty after merging an empty filter", targetFilter.isEmpty());
        assertEquals("Cardinality should remain 0 after merging an empty filter", 0, targetFilter.cardinality());
    }
}