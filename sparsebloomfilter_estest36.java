package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains tests for the SparseBloomFilter.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that an empty SparseBloomFilter does not contain a populated BloomFilter.
     * The 'contains' method should return false when checking if all bits from a non-empty
     * filter are set in an empty filter.
     */
    @Test
    public void testContainsReturnsFalseWhenEmptyFilterChecksPopulatedFilter() {
        // Arrange: Set up the test conditions.
        // 1. Define a shape for the Bloom filters.
        Shape shape = Shape.fromKM(1045, 1045);

        // 2. Create a hasher to populate one of the filters.
        Hasher hasher = new EnhancedDoubleHasher(997L, -1591L);

        // 3. Create a populated filter to serve as the check condition.
        //    Here we use a SimpleBloomFilter, but any BloomFilter implementation would work.
        BloomFilter<?> populatedFilter = new SimpleBloomFilter(shape);
        populatedFilter.merge(hasher);

        // 4. Create an empty SparseBloomFilter, which is the system under test.
        SparseBloomFilter emptyFilter = new SparseBloomFilter(shape);

        // Act: Call the method being tested.
        boolean result = emptyFilter.contains(populatedFilter);

        // Assert: Verify the outcome is as expected.
        assertFalse("An empty filter should not 'contain' a populated filter.", result);
    }
}