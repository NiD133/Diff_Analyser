package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link SimpleBloomFilter}.
 * This test focuses on the behavior of merging an empty filter.
 */
public class SimpleBloomFilterTest {

    /**
     * Verifies that merging an empty SimpleBloomFilter with itself does not change its state
     * and correctly reports that no modification occurred.
     */
    @Test
    public void testMergeWithSelfOnEmptyFilterDoesNotChangeState() {
        // Arrange: Create an empty Bloom filter.
        Shape shape = Shape.fromNMK(19, 19, 19);
        SimpleBloomFilter emptyFilter = new SimpleBloomFilter(shape);

        // Pre-condition check: ensure the filter is empty before the operation.
        assertTrue("Filter should be empty upon creation", emptyFilter.isEmpty());
        assertEquals("Characteristics of an empty filter should be 0", 0, emptyFilter.characteristics());

        // Act: Merge the empty filter with itself. The merge operation returns true
        // if the filter was modified, and false otherwise.
        boolean wasModified = emptyFilter.merge(emptyFilter);

        // Assert: Verify the filter's state and the merge operation's result.
        assertFalse("Merging an empty filter with itself should not modify the filter", wasModified);
        assertTrue("Filter should remain empty after the merge operation", emptyFilter.isEmpty());
        assertEquals("Characteristics should remain 0 after the merge", 0, emptyFilter.characteristics());
    }
}