package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the merge() method in SimpleBloomFilter.
 */
public class SimpleBloomFilterMergeTest {

    @Test
    public void mergeShouldReturnTrueOnModificationAndFalseOtherwise() {
        // Arrange: Create a shape, an empty Bloom filter, a hasher, and another empty filter.
        Shape shape = Shape.fromKM(19, 19);
        SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);
        Hasher hasher = new EnhancedDoubleHasher(19, 2654L);
        
        // An empty filter to test merging with no new data.
        BloomFilter<?> emptyFilter = new SparseBloomFilter(shape);

        // Act & Assert: Part 1
        // Merging a hasher into an empty filter should modify it.
        boolean changedByHasher = bloomFilter.merge(hasher);
        
        assertTrue("Merging a hasher into an empty filter should return true, as it modifies the filter.",
                changedByHasher);

        // Act & Assert: Part 2
        // Merging an empty filter should not cause any changes.
        boolean changedByEmptyFilter = bloomFilter.merge(emptyFilter);

        assertFalse("Merging an empty filter should return false, as it does not modify the filter.",
                changedByEmptyFilter);
    }
}