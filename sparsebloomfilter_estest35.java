package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This test verifies that a {@link SparseBloomFilter} correctly functions as a
 * {@link BitMapExtractor} when interacting with other Bloom filter implementations,
 * such as a {@link ArrayCountingBloomFilter}.
 */
public class SparseBloomFilterInteractionTest {

    /**
     * Tests that a populated SparseBloomFilter can be used as an extractor to query
     * an empty ArrayCountingBloomFilter for its maximum insertion capacity.
     *
     * <p>This test indirectly verifies the {@code processIndices} method of the
     * SparseBloomFilter by using it as input to the {@code getMaxInsert} method of a
     * counting filter.</p>
     */
    @Test(timeout = 4000)
    public void testGetMaxInsertFromEmptyCountingFilterUsingSparseFilterAsExtractor() {
        // Arrange
        // 1. Define a shape. Shape.fromNM(9, 9) creates a shape with 9 bits and
        // is optimized for 9 items, which results in 1 hash function.
        final Shape shape = Shape.fromNM(9, 9);

        // 2. Create a sparse filter and populate it with a single entry.
        final SparseBloomFilter sparseFilter = new SparseBloomFilter(shape);
        final Hasher hasher = new EnhancedDoubleHasher(0L, 9);
        final boolean wasModified = sparseFilter.merge(hasher);

        // Sanity check: the merge should have changed the filter and added one index.
        assertTrue("The filter should have been modified by the merge operation.", wasModified);
        assertEquals("The filter should contain exactly one index after the merge.", 1, sparseFilter.cardinality());

        // 3. Create an empty counting filter with the same shape.
        final ArrayCountingBloomFilter countingFilter = new ArrayCountingBloomFilter(shape);

        // Act
        // Use the sparse filter as a BitMapExtractor to determine how many times
        // its bits could be inserted into the empty counting filter.
        final int maxInserts = countingFilter.getMaxInsert(sparseFilter);

        // Assert
        // An empty ArrayCountingBloomFilter has all its counts at 0. The default
        // implementation uses 4 bits per counter, allowing a maximum count of 15.
        // Therefore, the maximum number of insertions should be 15.
        final int expectedMaxInserts = 15; // (2^4 - 1)
        assertEquals("Max inserts should be the max counter value for an empty counting filter.",
                expectedMaxInserts, maxInserts);
    }
}