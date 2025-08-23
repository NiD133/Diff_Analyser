package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that a NullPointerException is thrown when processBitMaps is called with a null consumer.
     * The method contract requires a non-null predicate to process the bit maps.
     */
    @Test(expected = NullPointerException.class)
    public void processBitMaps_withNullConsumer_throwsNullPointerException() {
        // Arrange: Create a filter with any valid shape.
        // The specific dimensions of the shape do not matter for this test.
        Shape shape = Shape.fromNM(10, 100);
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Act: Call the method under test with a null argument.
        filter.processBitMaps(null);

        // Assert: The test will pass only if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}