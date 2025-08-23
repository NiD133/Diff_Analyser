package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the SimpleBloomFilter class.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that the copy() method creates a new SimpleBloomFilter instance
     * that is a distinct object but has the same state (shape, cardinality, and bit map)
     * as the original.
     */
    @Test
    public void copyShouldCreateADistinctButEqualFilter() {
        // Arrange: Create a shape and a bloom filter.
        // We use simple, clear values for the shape's parameters.
        Shape shape = Shape.fromNM(10, 100); // 10 items, 100 bits
        SimpleBloomFilter originalFilter = new SimpleBloomFilter(shape);

        // To make the test more robust, merge a hasher to modify the filter's state.
        // This ensures we are not just copying an empty filter.
        Hasher hasher = new SimpleHasher(new byte[]{1, 2, 3});
        originalFilter.merge(hasher);

        // Act: Create a copy of the filter.
        SimpleBloomFilter copiedFilter = originalFilter.copy();

        // Assert: The copy should be a different object but have identical contents.
        assertNotSame("The copied filter should be a new object instance.", originalFilter, copiedFilter);
        assertEquals("The copied filter should have the same shape.", originalFilter.getShape(), copiedFilter.getShape());
        assertEquals("The copied filter should have the same cardinality.", originalFilter.cardinality(), copiedFilter.cardinality());
        assertArrayEquals("The copied filter should have the same bit map.", originalFilter.asBitMapArray(), copiedFilter.asBitMapArray());
    }
}