package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Tests for the {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that attempting to merge a null BloomFilter throws a NullPointerException.
     * The merge operation requires a non-null filter argument to ensure correctness.
     */
    @Test(expected = NullPointerException.class)
    public void testMergeWithNullFilterThrowsException() {
        // Arrange: Create a standard shape and an empty filter.
        Shape shape = Shape.fromKM(18, 18);
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Act: Attempt to merge with a null filter.
        // The @Test(expected=...) annotation will assert that a NullPointerException is thrown.
        filter.merge((BloomFilter<?>) null);
    }
}