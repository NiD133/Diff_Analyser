package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;

/**
 * Tests for the {@link SimpleBloomFilter} class.
 */
public class SimpleBloomFilterTest {

    /**
     * Tests that calling the contains() method with a null IndexExtractor
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void contains_withNullIndexExtractor_shouldThrowNullPointerException() {
        // Arrange: Create a Bloom filter with an arbitrary shape.
        // The specific dimensions of the shape do not affect this test.
        Shape shape = Shape.fromKM(10, 100);
        SimpleBloomFilter bloomFilter = new SimpleBloomFilter(shape);

        // Act: Attempt to check for containment using a null IndexExtractor.
        // The assertion is handled by the @Test(expected=...) annotation, which
        // verifies that a NullPointerException is thrown.
        bloomFilter.contains((IndexExtractor) null);
    }
}