package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Contains tests for the merge operations in {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that merging an empty IndexExtractor into a Bloom filter does not change the filter
     * and correctly returns {@code false}.
     *
     * <p>The {@code merge(IndexExtractor)} method should return {@code true} only if the filter's
     * state is potentially modified. Merging an empty set of indices results in no change.
     * In this test, an empty SimpleBloomFilter is used as the IndexExtractor to provide
     * no indices.</p>
     */
    @Test
    public void testMergeWithEmptyIndexExtractorReturnsFalse() {
        // Arrange: Create a standard shape and an empty Bloom filter.
        Shape shape = Shape.fromKM(22, 22);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act: Merge the filter with itself. Since the filter is empty,
        // it acts as an empty IndexExtractor, providing no indices to merge.
        boolean isChanged = filter.merge((IndexExtractor) filter);

        // Assert: The merge operation should report that no change occurred.
        assertFalse("Merging an empty IndexExtractor should return false", isChanged);
    }
}