package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link SimpleBloomFilter}.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that a newly created (empty) filter has zero characteristics.
     * The number of characteristics is the count of enabled bits in the filter.
     */
    @Test
    public void testCharacteristicsOfNewEmptyFilterIsZero() {
        // Arrange: Create a shape and a new, empty Bloom filter.
        Shape shape = Shape.fromKM(4, 4);
        SimpleBloomFilter filter = new SimpleBloomFilter(shape);

        // Act: Get the number of characteristics from the empty filter.
        int characteristics = filter.characteristics();

        // Assert: The number of characteristics should be 0.
        assertEquals("An empty filter should have zero characteristics", 0, characteristics);
    }
}