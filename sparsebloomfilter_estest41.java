package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that an empty filter correctly reports that it contains another empty filter.
     * This is a test of the subset property, where an empty set is always a subset
     * of any other set, including itself.
     */
    @Test
    public void testContainsWithEmptyFilterReturnsTrue() {
        // Arrange: Create an empty Bloom filter with a standard shape.
        Shape shape = Shape.fromKM(17, 72); // A common shape for testing
        SparseBloomFilter emptyFilter = new SparseBloomFilter(shape);

        // Act: Check if the empty filter contains itself.
        // Since it has no bits set, it should contain any other filter that also has no bits set.
        boolean result = emptyFilter.contains(emptyFilter);

        // Assert: The result should be true.
        assertTrue("An empty filter should always contain itself.", result);
    }
}