package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertThrows;
import org.junit.Test;

/**
 * Contains tests for the {@link SparseBloomFilter}, focusing on its interaction
 * with other filter types under extreme conditions.
 *
 * Note: The original test class name `SparseBloomFilter_ESTestTest14` has been
 * renamed to reflect a standard, human-written test suite.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that a merge operation will fail if the source filter is too large to be
     * instantiated in memory.
     *
     * <p>This test defines a {@link Shape} with the maximum possible number of bits.
     * It then verifies that attempting to create a dense {@code SimpleBloomFilter}
     * with this shape throws an {@link OutOfMemoryError}. This confirms that operations
     * with impractically large, dense filters will fail predictably during instantiation,
     * before a method like {@code merge} is even called.</p>
     */
    @Test
    public void mergeFailsWhenSourceFilterIsTooLargeToCreate() {
        // Arrange: Create a shape with the maximum possible number of bits and hash functions.
        // This shape is too large to be materialized by a dense filter like SimpleBloomFilter.
        final Shape impracticallyLargeShape = Shape.fromKM(Integer.MAX_VALUE, Integer.MAX_VALUE);

        // A SparseBloomFilter can handle this shape because it only stores the indices
        // of enabled bits, and is currently empty.
        final SparseBloomFilter targetFilter = new SparseBloomFilter(impracticallyLargeShape);

        // Act & Assert: Verify that creating the source SimpleBloomFilter throws an
        // OutOfMemoryError, thus preventing any subsequent merge operation.
        assertThrows("Expected an OutOfMemoryError when creating a dense filter with a huge shape",
            OutOfMemoryError.class,
            () -> {
                // This instantiation will fail because it requires allocating a long[] array
                // that is too large to fit in the heap (approx. 2^31 bits).
                SimpleBloomFilter sourceFilter = new SimpleBloomFilter(impracticallyLargeShape);

                // The following line is intentionally unreachable. The test's purpose is to
                // show that the setup for the merge fails under these conditions.
                targetFilter.merge(sourceFilter);
            });
    }
}