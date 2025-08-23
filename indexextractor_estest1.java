package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests the implementation of the {@code asIndexArray} method from the {@link IndexExtractor} interface.
 * This test specifically verifies its behavior on an empty {@link SimpleBloomFilter}.
 */
public class IndexExtractorTest {

    /**
     * Verifies that calling asIndexArray() on a newly created (and thus empty)
     * Bloom filter returns an empty integer array.
     */
    @Test
    public void asIndexArray_onEmptyFilter_returnsEmptyArray() {
        // Arrange: Create an empty Bloom filter. The specific shape is not
        // critical for this test as long as it's valid.
        Shape shape = Shape.fromKM(1856, 1856);
        IndexExtractor emptyFilter = new SimpleBloomFilter(shape);

        // Act: Retrieve the indices from the empty filter.
        int[] indices = emptyFilter.asIndexArray();

        // Assert: The resulting array of indices should be empty.
        assertEquals("An empty filter should produce an empty index array", 0, indices.length);
    }
}