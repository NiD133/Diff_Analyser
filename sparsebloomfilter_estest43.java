package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that a newly constructed SparseBloomFilter reports itself as empty.
     */
    @Test
    public void testNewFilterIsEmpty() {
        // Arrange: Create a shape and a new filter. The specific shape
        // parameters are not important for this test.
        Shape shape = Shape.fromKM(10, 100);
        SparseBloomFilter bloomFilter = new SparseBloomFilter(shape);

        // Act & Assert: A filter that has had no elements merged into it
        // should be considered empty.
        assertTrue("A newly created filter should be empty", bloomFilter.isEmpty());
    }
}