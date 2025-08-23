package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the {@link SimpleBloomFilter} class.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that the copy() method creates a new, independent instance of the
     * Bloom filter with the same state as the original.
     */
    @Test
    public void copyShouldReturnAnIndependentInstanceWithSameState() {
        // Arrange: Create an original Bloom filter with a specific shape.
        final Shape shape = Shape.fromNMK(78, 78, 2);
        final SimpleBloomFilter originalFilter = new SimpleBloomFilter(shape);

        // Act: Create a copy of the original filter.
        final SimpleBloomFilter copiedFilter = originalFilter.copy();

        // Assert: Verify that the copy is a new object but has identical state.
        
        // 1. The copied object should be a different instance in memory.
        assertNotSame("The copied filter should be a new instance.", originalFilter, copiedFilter);

        // 2. The copied filter's state should be identical to the original's.
        assertEquals("Copied filter should have the same shape.", originalFilter.getShape(), copiedFilter.getShape());
        assertEquals("Copied filter should have the same cardinality.", originalFilter.cardinality(), copiedFilter.cardinality());
        assertArrayEquals("Copied filter should have the same bitmap data.", originalFilter.asBitMapArray(), copiedFilter.asBitMapArray());
    }
}