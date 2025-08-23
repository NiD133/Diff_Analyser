package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that calling clear() on a newly created (and thus empty)
     * filter results in a filter with zero characteristics.
     */
    @Test
    public void testClearOnNewEmptyFilterShouldResultInZeroCharacteristics() {
        // Arrange: Create a new, empty Bloom filter.
        final Shape shape = Shape.fromKM(23, 23);
        final SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);

        // Act: Clear the filter.
        bloomFilter.clear();

        // Assert: The number of set bits (characteristics) should be zero.
        assertEquals("A cleared empty filter should have zero characteristics", 0, bloomFilter.characteristics());
    }
}