package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Contains tests for the {@link SparseBloomFilter} class.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that attempting to merge a null Hasher throws a NullPointerException.
     * A null Hasher is invalid because it cannot produce the indices to merge into the filter.
     */
    @Test
    public void testMergeWithNullHasherThrowsNullPointerException() {
        // Arrange: Create a standard Bloom filter. The shape is required for instantiation
        // but its specific values are not relevant to this null-check test.
        final Shape shape = Shape.fromNM(10, 10);
        final SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Act & Assert: Attempt to merge a null hasher and verify the exception.
        try {
            filter.merge((Hasher) null);
            fail("Expected a NullPointerException to be thrown, but no exception was thrown.");
        } catch (final NullPointerException e) {
            // Verify that the exception message correctly identifies the null parameter.
            // This confirms that the intended null-check was triggered.
            assertEquals("hasher", e.getMessage());
        }
    }
}