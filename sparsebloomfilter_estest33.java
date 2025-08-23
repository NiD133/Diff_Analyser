package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@code merge(IndexExtractor)} method in {@link SparseBloomFilter}.
 */
public class SparseBloomFilterMergeTest {

    /**
     * Tests that merging an empty filter into another empty filter returns true.
     *
     * <p>This test verifies the behavior when a filter is merged with an empty source.
     * In this specific case, an empty filter is merged with itself.
     * The filter's contents do not change, but the {@code merge} method is expected
     * to return {@code true} based on the underlying implementation where the source's
     * {@code processIndices} method returns {@code true} to indicate it completed processing.
     * </p>
     */
    @Test
    public void testMergeWithEmptySourceReturnsTrue() {
        // Arrange: Create an empty SparseBloomFilter.
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter emptyFilter = new SparseBloomFilter(shape);

        // Act: Merge the empty filter with itself. Since the source is empty,
        // no indices are added and the filter's state remains unchanged.
        boolean wasModified = emptyFilter.merge(emptyFilter);

        // Assert: The merge operation should return true.
        assertTrue("Merging an empty filter should return true", wasModified);
    }
}