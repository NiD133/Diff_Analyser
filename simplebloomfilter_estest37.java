package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleBloomFilter_ESTestTest37 extends SimpleBloomFilter_ESTest_scaffolding {

    /**
     * Tests that merging an empty SimpleBloomFilter with itself does not change the
     * filter and correctly returns {@code false}.
     *
     * The merge operation should only return {@code true} if the filter's state is
     * modified. Since no bits are set in an empty filter, no changes should occur.
     */
    @Test
    public void testMergeWithSelfOnEmptyFilterReturnsFalse() {
        // Arrange
        // An empty filter is created by instantiating it with a valid shape.
        Shape shape = Shape.fromNM(64, 64);
        SimpleBloomFilter emptyFilter = new SimpleBloomFilter(shape);

        // Pre-condition check to ensure the filter is indeed empty.
        assertTrue("Filter should be empty before the merge operation", emptyFilter.isEmpty());

        // Act
        // Merge the empty filter with itself. This operation should not set any bits.
        boolean wasModified = emptyFilter.merge((BitMapExtractor) emptyFilter);

        // Assert
        // The merge method should return false, as no bits were changed.
        assertFalse("Merging an empty filter with itself should not modify it", wasModified);

        // Post-condition check to ensure the filter remains empty after the operation.
        assertTrue("Filter should remain empty after the merge operation", emptyFilter.isEmpty());
    }
}