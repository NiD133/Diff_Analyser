package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * Tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that the getShape() method returns the same Shape instance
     * that was used to construct the filter.
     */
    @Test
    public void getShape_shouldReturnShapeFromConstructor() {
        // Arrange: Create a shape and a Bloom filter with that shape.
        // The specific parameters for the shape are not important for this test.
        Shape expectedShape = Shape.fromNMK(10, 7, 72);
        SparseBloomFilter bloomFilter = new SparseBloomFilter(expectedShape);

        // Act: Retrieve the shape from the filter.
        Shape actualShape = bloomFilter.getShape();

        // Assert: The retrieved shape should be the exact same instance
        // as the one provided to the constructor.
        assertSame("The returned shape should be the same instance as the one used in the constructor.",
                expectedShape, actualShape);
    }
}