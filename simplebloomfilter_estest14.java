package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// The scaffolding class is retained to ensure the test remains within its original structural context.
public class SimpleBloomFilter_ESTestTest14 extends SimpleBloomFilter_ESTest_scaffolding {

    /**
     * Tests that merging a Hasher into an empty SimpleBloomFilter correctly
     * updates its state (cardinality, isEmpty) and reports that a change occurred.
     */
    @Test
    public void mergeHasherIntoEmptyFilterShouldUpdateState() {
        // Arrange
        // A shape with n=38 items and m=38 bits results in k=1 hash function.
        // This means the hasher will set exactly one bit in the filter.
        Shape shape = Shape.fromNM(38, 38);
        SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);
        Hasher hasher = new EnhancedDoubleHasher(38, 38);

        // Pre-condition check: ensure the filter is empty before the operation.
        assertTrue("Filter should be empty before merging", bloomFilter.isEmpty());
        assertEquals("Cardinality should be zero for an empty filter", 0, bloomFilter.cardinality());

        // Act
        // Merge the hasher, which sets the bit(s) it hashes to.
        boolean wasModified = bloomFilter.merge(hasher);

        // Assert
        // 1. The merge operation should report that the filter was changed.
        assertTrue("merge() should return true when the filter is modified", wasModified);

        // 2. The filter should no longer be empty.
        assertFalse("Filter should not be empty after merging an item", bloomFilter.isEmpty());

        // 3. The cardinality (number of set bits) should be 1, as k=1 for this shape.
        assertEquals("Cardinality should be 1 after merging a hasher with k=1", 1, bloomFilter.cardinality());
    }
}