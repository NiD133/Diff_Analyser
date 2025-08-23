package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

/**
 * Tests for the {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that an empty {@code SparseBloomFilter} does not contain any indices.
     * The {@code contains} method should return {@code false} when checking against
     * an {@code IndexExtractor}, as none of the specified indices will be present.
     */
    @Test
    public void containsShouldReturnFalseForAnyIndexWhenFilterIsEmpty() {
        // Arrange: Create an empty filter and an extractor with some indices to check.
        Shape shape = Shape.fromNM(64, 64);
        SparseBloomFilter emptyFilter = new SparseBloomFilter(shape);
        IndexExtractor indicesToCheck = IndexExtractor.fromIndexArray(new int[]{5, 10, 15});

        // Act: Check if the empty filter contains the specified indices.
        boolean result = emptyFilter.contains(indicesToCheck);

        // Assert: The result must be false, as an empty filter contains nothing.
        assertFalse("An empty filter should not report containing any indices", result);
    }
}