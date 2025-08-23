package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Contains tests for {@link SimpleBloomFilter} that focus on behavior under
 * extreme memory pressure, such as when handling maximum-sized filters.
 */
public class SimpleBloomFilterMemoryTest {

    /**
     * Tests that attempting to merge a Bloom filter of the maximum possible size
     * results in an OutOfMemoryError.
     *
     * <p>This test case verifies behavior at a boundary condition. Creating a filter
     * with {@code Integer.MAX_VALUE} bits requires allocating a bit map of
     * approximately 256MB. The subsequent {@code merge} operation may require
     * additional large allocations (e.g., for cloning), which is expected to
     * exhaust heap space.</p>
     *
     * <p><b>Note:</b> This test is sensitive to the JVM's heap size configuration.
     * It may fail during setup on memory-constrained systems or pass without error
     * on systems with a very large heap.</p>
     */
    @Test(timeout = 4000)
    public void mergeWithMaxSizedFilterShouldThrowOutOfMemoryError() {
        // Arrange: Create a Bloom filter with the largest possible shape.
        final Shape maxSizeShape = Shape.fromNM(Integer.MAX_VALUE, Integer.MAX_VALUE);
        final SimpleBloomFilter hugeFilter;

        try {
            // This allocation alone requires a significant amount of memory (~256MB).
            hugeFilter = new SimpleBloomFilter(maxSizeShape);
        } catch (final OutOfMemoryError e) {
            // If the filter cannot be created, the test environment lacks sufficient
            // memory. Fail with a clear message instead of proceeding.
            fail("Test setup failed: Could not allocate the initial huge BloomFilter. " +
                "This test requires a large heap size (-Xmx).");
            return; // Unreachable, but improves clarity.
        }

        // Act & Assert: Merging the filter with itself is expected to trigger an
        // OutOfMemoryError, as the implementation may need to create a copy of the
        // massive internal bit map.
        try {
            hugeFilter.merge(hugeFilter);
            fail("Expected an OutOfMemoryError to be thrown, but the merge completed successfully.");
        } catch (final OutOfMemoryError expected) {
            // This is the expected outcome.
            assertNotNull("The caught OutOfMemoryError should not be null.", expected);
        }
    }
}