package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link SparseBloomFilter} focusing on edge cases and error handling.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that attempting to merge a {@code SimpleBloomFilter} with an extremely large shape
     * into a {@code SparseBloomFilter} throws an {@code OutOfMemoryError}.
     *
     * <p>This scenario tests the system's behavior under resource constraints. A Bloom filter
     * with {@code Integer.MAX_VALUE} bits requires a very large backing data structure
     * (e.g., a {@code long[]} of approximately 256MB for a {@code SimpleBloomFilter}).
     * The {@code merge} operation may require allocating significant additional memory,
     * leading to an {@code OutOfMemoryError}.</p>
     *
     * <p>Note: This test's success is dependent on the JVM's heap size configuration. It is
     * designed to fail by exhausting available memory.</p>
     */
    @Test(timeout = 4000)
    public void mergeWithExtremelyLargeFilterThrowsOutOfMemoryError() {
        // Arrange: Create a shape with the maximum possible number of bits and hash functions.
        // This forces the creation of a very large internal data structure for the SimpleBloomFilter.
        final Shape extremeShape = Shape.fromKM(Integer.MAX_VALUE, Integer.MAX_VALUE);

        final SimpleBloomFilter sourceFilter = new SimpleBloomFilter(extremeShape);
        final SparseBloomFilter targetFilter = new SparseBloomFilter(extremeShape);

        // Act & Assert: The merge operation is expected to fail by exhausting memory.
        try {
            targetFilter.merge(sourceFilter);
            fail("Expected an OutOfMemoryError to be thrown, but the merge operation completed.");
        } catch (final OutOfMemoryError expected) {
            // This is the expected outcome. The test passes.
            assertNotNull("The caught OutOfMemoryError should not be null.", expected);
        }
    }
}