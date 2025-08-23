package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that a newly created (empty) filter has a cardinality of 0.
     * Cardinality is the number of enabled bits in the filter.
     */
    @Test
    public void newlyCreatedFilterShouldHaveZeroCardinality() {
        // Arrange: Create a new, empty filter with a valid shape.
        // The specific shape values are not critical for this test.
        Shape shape = Shape.fromKM(10, 100); // 10 hash functions, 100 bits
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Act: Get the cardinality of the new filter.
        int cardinality = filter.cardinality();

        // Assert: The cardinality of an empty filter must be 0.
        assertEquals("A new, empty filter should have a cardinality of 0.", 0, cardinality);
    }
}