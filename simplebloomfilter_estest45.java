package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Contains tests for the {@link SimpleBloomFilter} class.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that merging an empty SimpleBloomFilter with itself does not change its state
     * and correctly reports that no modification occurred.
     */
    @Test
    public void mergeWithSelfWhenEmptyShouldNotChangeFilterAndReturnFalse() {
        // Arrange: Create an empty Bloom filter with a specific shape.
        Shape shape = Shape.fromNMK(15, 15, 15);
        SimpleBloomFilter emptyFilter = new SimpleBloomFilter(shape);

        // Act: Merge the filter with itself.
        boolean wasModified = emptyFilter.merge(emptyFilter);

        // Assert: The merge operation should report no changes, and the filter's state should be unaltered.
        assertFalse("Merging an empty filter with itself should return false, indicating no modification.", wasModified);
        assertEquals("Cardinality should remain 0 after the merge.", 0, emptyFilter.cardinality());
        assertEquals("Characteristics should remain 0 after the merge.", 0, emptyFilter.characteristics());
    }
}